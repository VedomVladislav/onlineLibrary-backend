package by.vedom.library.auth.service;

import by.vedom.library.auth.entity.Activity;
import by.vedom.library.auth.entity.Role;
import by.vedom.library.auth.entity.User;
import by.vedom.library.auth.exception.RoleNotFoundException;
import by.vedom.library.auth.exception.UserAlreadyActivatedException;
import by.vedom.library.auth.exception.UserOrEmailAlreadyExistsException;
import by.vedom.library.auth.repository.ActivityRepository;
import by.vedom.library.auth.repository.RoleRepository;
import by.vedom.library.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserService {

    private static final String DEFAULT_ROLE = "USER";
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final ActivityRepository activityRepository;

    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository,
                       ActivityRepository activityRepository, PasswordEncoder passwordEncoder,
                       EmailService emailService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.activityRepository = activityRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public void save(User user) throws UserOrEmailAlreadyExistsException, RoleNotFoundException {
        if (userExistsByEmailOrUsername(user.getEmail(), user.getUsername())) {
            throw new UserOrEmailAlreadyExistsException("User or email already exists");
        }
        Role userRole = roleRepository.findByName(DEFAULT_ROLE)
                        .orElseThrow(() -> new RoleNotFoundException("Default Role USER not found"));
        user.getRoles().add(userRole);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);

        Activity activity = new Activity();
        activity.setUser(user);
        activity.setUuid(UUID.randomUUID().toString());
        activityRepository.save(activity);

        emailService.sendActivationEmail(user.getEmail(), user.getUsername(), activity.getUuid());
    }

    public boolean userExistsByEmailOrUsername(String email, String userName) {
        return userRepository.existsByEmail(email) || userRepository.existsByUsername(userName);
    }

    public Optional<Role> findByName(String role) {
        return roleRepository.findByName(role);
    }

    public Activity findActivityByUuid(String uuid) throws UserAlreadyActivatedException {
        Activity activity = activityRepository.findByUuid(uuid)
                .orElseThrow(() -> new UsernameNotFoundException("Activity Not Found with uuid: " + uuid));
        if (activity.isActivated()) {
            throw new UserAlreadyActivatedException("User already activated");
        }
        return activity;
    }

    public Activity findActivityByUserId(Long id) throws UserAlreadyActivatedException {
        Activity activity = activityRepository.findByUserId(id)
                .orElseThrow(() -> new UsernameNotFoundException("Activity Not Found with id: " + id));
        if (activity.isActivated()) {
            throw new UserAlreadyActivatedException("User already activated");
        }
        return activity;
    }

    public int updateUserPassword(String password, String email) {
        return userRepository.updateUserPassword(password, email);
    }

    public int activate(String uuid) {
        return activityRepository.changeActivated(uuid, true);
    }

    public int deactivate(String uuid) {
        return activityRepository.changeActivated(uuid, false);
    }
}
