package com.asworld.hsms_backend.config;

import com.asworld.hsms_backend.model.User;
import com.asworld.hsms_backend.model.enums.Role;
import com.asworld.hsms_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
@Profile("!test")
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${master.admin.username}")
    private String username;
    @Value("${master.admin.password}")
    private String password;
    @Value("${master.admin.email}")
    private String email;
    @Value("${master.admin.mobile}")
    private String mobile;


    @Bean
    CommandLineRunner initMasterAdmin() {
        return args -> {
            if (userRepository.findByUsername(username).isEmpty()) {
                User master = User.builder()
                    .firstName("Master")
                    .lastName("Admin")
                    .username(username)
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .mobileNumber(mobile)
                    .role(Role.MASTER_ADMIN)
                    .approved(true)
                    .build();

                userRepository.save(master);
                log.info("Master admin created with username:{} & email:{}", username, email);
            }
        };
    }
}
