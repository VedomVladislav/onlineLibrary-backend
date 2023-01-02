package by.vedom.library.auth.controller;

import by.vedom.library.auth.dto.ErrorDTO;
import by.vedom.library.auth.entity.Activity;
import by.vedom.library.auth.entity.Role;
import by.vedom.library.auth.entity.User;
import by.vedom.library.auth.exception.RoleNotFoundException;
import by.vedom.library.auth.exception.UserAlreadyActivatedException;
import by.vedom.library.auth.exception.UserOrEmailAlreadyExistsException;
import by.vedom.library.auth.service.EmailService;
import by.vedom.library.auth.service.UserDetailsImpl;
import by.vedom.library.auth.service.UserDetailsServiceImpl;
import by.vedom.library.auth.service.UserService;
import by.vedom.library.auth.utils.CookieUtils;
import by.vedom.library.auth.utils.JwtUtils;
import lombok.extern.java.Log;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@Log
public class AuthController {

    public static final String DEFAULT_ROLE = "USER";
    private final UserService userService;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final CookieUtils cookieUtils;
    private final EmailService emailService;
    private final UserDetailsServiceImpl userDetailsService;

    public AuthController(UserService userService, PasswordEncoder encoder,
                          AuthenticationManager authenticationManager, JwtUtils jwtUtils,
                          CookieUtils cookieUtils, EmailService emailService,
                          UserDetailsServiceImpl userDetailsService
    ) {
        this.userService = userService;
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.cookieUtils = cookieUtils;
        this.emailService = emailService;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/test-no-auth")
    public String testNoAuth() {
        return "OK-no-auth";
    }

    @PostMapping("/test-with-auth")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String testWithAuth() {
        return "OK-with-auth";
    }

    @PostMapping("/logout")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> logout() {
        HttpCookie cookie = cookieUtils.deleteJwtCookie();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok().headers(responseHeaders).build();
    }

    @PutMapping("/register")
    public ResponseEntity<HttpStatus> register(@Valid @RequestBody User user) throws UserOrEmailAlreadyExistsException, RoleNotFoundException {

        if (userService.userExistsByEmailOrUsername(user.getUsername(), user.getEmail())) {
            throw new UserOrEmailAlreadyExistsException("User or email already exists");
        }

        Role userRole = userService.findByName(DEFAULT_ROLE)
                        .orElseThrow(() -> new RoleNotFoundException("Default Role USER not found."));
        user.getRoles().add(userRole);

        user.setPassword(encoder.encode(user.getPassword()));

        userService.save(user);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/activate-account")
    public ResponseEntity<Boolean> activateUser(@RequestBody String uuid) {
        int updatedCount = userService.activate(uuid);
        return ResponseEntity.ok(updatedCount == 1);
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@NotNull @Valid @RequestBody User user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        if (!userDetails.isActivated()) {
            throw new DisabledException("User disabled");
        }
        String jwt = jwtUtils.createAccessToken(userDetails.getUser());
        HttpCookie cookie = cookieUtils.createJwtCookie(jwt);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok().headers(responseHeaders).body(userDetails.getUser());
    }

    @PostMapping("/resend-activate-email")
    public ResponseEntity<HttpStatus> resendActivateEmail(@RequestBody String userNameOrEmail) throws UserAlreadyActivatedException {
        UserDetailsImpl user = (UserDetailsImpl) userDetailsService.loadUserByUsername(userNameOrEmail);

        Activity activity = userService.findActivityByUserId(user.getId());

        emailService.sendActivationEmail(user.getEmail(), user.getUsername(), activity.getUuid());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/send-reset-password-email")
    public ResponseEntity<?> sendEmailResetPassword(@RequestBody String userNameOrEmail) {
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(userNameOrEmail);
        User user = userDetails.getUser();
        emailService.sendResetPasswordEmail(user.getEmail(), jwtUtils.createEmailResetToken(user));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/update-password")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Boolean> updatePassword(@RequestBody String password) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        int updatedCount = userService.updateUserPassword(encoder.encode(password), user.getEmail());
        return ResponseEntity.ok(updatedCount == 1);
    }

    @PostMapping("/auto")
    public ResponseEntity<User> autoLogin() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok().body(userDetails.getUser());

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleExceptions(Exception ex) {
        return new ResponseEntity<>(
                new ErrorDTO(ex.getClass().getSimpleName(), ex.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

}
