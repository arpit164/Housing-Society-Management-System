package com.asworld.hsms_backend.config;

import com.asworld.hsms_backend.model.User;
import com.asworld.hsms_backend.model.enums.Gender;
import com.asworld.hsms_backend.model.enums.Role;
import com.asworld.hsms_backend.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;

@Slf4j
@Configuration
@Profile("!test")
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "users")
public class UserConfig {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Setter
    private Map<String, Credential> credentials;

    @Setter
    @Getter
    public static class Credential {
        private String username;
        private String password;
    }

    @Bean
    CommandLineRunner initUsers(UserRepository userRepository) {
        return args -> {
            createUser(Role.MASTER_ADMIN, "Master", "Admin", "master@gmail.com", "9898989898", Gender.MALE, true);
            createUser(Role.ADMIN, "Normal", "Admin", "admin@hsms.com", "8787878787", Gender.FEMALE, true);
            createUser(Role.RESIDENT, "Test", "Resident", "resident@hsms.com", "7676767676", Gender.MALE, false);
        };
    }

    private void createUser(Role role, String firstName, String lastName, String email, String mobile,
                                       Gender gender, boolean approved) {

        Credential cred = credentials.get(role.name());
        if (cred == null) {
            log.warn("No credentials defined for role {}", role);
            return;
        }

        String username = cred.getUsername();
        String password = cred.getPassword();

        userRepository.findByUsername(username).ifPresentOrElse(
                user -> log.info("User '{}' already exists, skipping.", username),
                () -> {
                    User newUser = User.builder()
                            .firstName(firstName)
                            .lastName(lastName)
                            .username(username)
                            .email(email)
                            .mobileNumber(mobile)
                            .password(passwordEncoder.encode(password))
                            .gender(gender)
                            .role(role)
                            .approved(approved)
                            .build();

                    userRepository.save(newUser);
                    log.info("{} created with username: {} and email: {} ", role, username, email);
                }
        );
    }
}
