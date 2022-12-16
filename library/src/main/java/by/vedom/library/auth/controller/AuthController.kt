//package by.vedom.library.auth.controller
//
//import by.vedom.library.auth.dto.ErrorDTO
//import by.vedom.library.auth.entity.User
//import by.vedom.library.auth.service.UserDetailsImpl
//import by.vedom.library.auth.service.UserDetailsServiceImpl
//import by.vedom.library.auth.service.UserService
//import by.vedom.library.auth.utils.CookieUtils
//import by.vedom.library.auth.utils.JwtUtils
//import lombok.AllArgsConstructor
//import lombok.extern.java.Log
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.http.HttpHeaders
//import org.springframework.http.HttpStatus
//import org.springframework.http.ResponseEntity
//import org.springframework.security.access.prepost.PreAuthorize
//import org.springframework.security.authentication.AuthenticationManager
//import org.springframework.security.authentication.DisabledException
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
//import org.springframework.security.core.context.SecurityContextHolder
//import org.springframework.security.crypto.password.PasswordEncoder
//import org.springframework.web.bind.annotation.*
//import javax.validation.Valid
//
//@RestController
//@RequestMapping("/auth")
//@Log
//open class AuthController(
//    private val userService: UserService,
////    private val userDetailsService: UserDetailsServiceImpl,
//    private val authenticationManager: AuthenticationManager,
//    private val jwtUtils: JwtUtils,
//    private val cookieUtils: CookieUtils,
//    private var encoder: PasswordEncoder
//) {
//
//    @PostMapping("/test-no-auth")
//    fun testNoAuth(): String {
//        return "OK-NO-AUTH"
//    }
//
//    @PostMapping("/test-with-auth")
//    @PreAuthorize("hasAuthority('USER')")
//    fun testWithAuth(): String {
//        return "OK-With-AUTH"
//    }
//
//    @PutMapping("/register")
//    fun register(@Valid @RequestBody user: User): ResponseEntity<*> {
////
////        if (userService.userExistsByEmailOrUsername(user.email, user.username)) {
////            throw UserOrEmailAlreadyExistsException("USer or email already exists")
////        }
////
////        val userRole = userService.findByName(DEFAULT_ROLE)
//
//
//        userService.save(user)
//        return ResponseEntity("", HttpStatus.OK)
//    }
//
//    @PostMapping("/activate-account")
//    fun activateUser(@RequestBody uuid: String): ResponseEntity<Boolean> {
//        userService.findActivityByUuid(uuid)
//        val updateCount = userService.activate(uuid)
//        return ResponseEntity.ok(updateCount == 1)
//    }
//
//    @PostMapping("/login")
//    fun login(@Valid @RequestBody user: User): ResponseEntity<User> {
//        val authentication = authenticationManager.authenticate(
//            UsernamePasswordAuthenticationToken(user.username, user.password)
//        )
//        SecurityContextHolder.getContext().authentication = authentication
//
//        val userDetails = authentication.principal as UserDetailsImpl
//
//        if (!userDetails.isActivated) {
//            throw DisabledException("User disabled")
//        }
//
//        val jwt = jwtUtils.createToken(userDetails.user)
//
//        val cookie = cookieUtils.createJwtCookie(jwt)
//        val responseHeader = HttpHeaders()
//        responseHeader.add(HttpHeaders.SET_COOKIE, cookie.toString())
//
//        return ResponseEntity.ok().headers(responseHeader).body(userDetails.user)
//    }
//
//    @PostMapping("/auto")
//    fun autoLogin(): ResponseEntity<User> {
//        val userDetailsImpl = SecurityContextHolder.getContext().authentication.principal as UserDetailsImpl
//        return ResponseEntity.ok().body(userDetailsImpl.user)
//    }
//
//    @PostMapping("/logout")
//    @PreAuthorize("hasAuthority('USER')")
//    fun logout(): ResponseEntity<*> {
//        val cookie = cookieUtils.deleteJwtCookie();
//        val responseHeader = HttpHeaders()
//        responseHeader.add(HttpHeaders.SET_COOKIE, cookie.toString());
//        return ResponseEntity.ok().headers(responseHeader).body("")
//    }
//
//    @PostMapping("/update-password")
//    @PreAuthorize("hasAuthority('USER')")
//    fun updateUserPassword(@RequestBody password: String): ResponseEntity<Boolean> {
//        val authentication = SecurityContextHolder.getContext().authentication
//        val userDetails = authentication.principal as UserDetailsImpl
//        val updateCount = userService.updateUserPassword(encoder.encode(password), userDetails.username)
//        return ResponseEntity.ok(updateCount == 1)
//    }
//
////    @PostMapping("/reset-activate-email")
////    fun resendActivateEmail(@RequestBody userNameOrEmail: String): ResponseEntity<*> {
////        val userDetails = userDetailsService.loadUserByUsername(userNameOrEmail) as UserDetailsImpl
////        val activity = userService.findActivityByUserId(userDetails.id)
////        emailService.sendActivationEmail(userDetails.email, userDetails.username, activity.uuid)
////        return ResponseEntity.ok().body("Email activation letter resent")
////    }
////
////    @PostMapping("send-reset-password-email")
////    fun sendEmailResetPassword(@RequestBody email: String): ResponseEntity<*> {
////        val userDetails = userDetailsService.loadUserByUsername(email) as UserDetailsImpl
////        val user = userDetails.user as User
////        emailService.sendResetPasswordEmail(user.email, jwtUtils.createEmailResetToken(user))
////        return ResponseEntity.ok().body("Reset email letter sent")
////    }
//
//    @ExceptionHandler
//    fun handleException(e: Exception): ResponseEntity<ErrorDTO> {
//        return ResponseEntity(ErrorDTO(e.javaClass.simpleName, e.message), HttpStatus.BAD_REQUEST)
//    }
//}