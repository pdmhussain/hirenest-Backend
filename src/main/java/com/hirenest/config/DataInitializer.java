
package com.hirenest.config;

import com.hirenest.entity.User;
import com.hirenest.repository.UserRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initializeAdmin(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${hirenest.bootstrap-admin.username:}") String username,
            @Value("${hirenest.bootstrap-admin.email:}") String email,
            @Value("${hirenest.bootstrap-admin.password:}") String password) {

        return args -> {

            boolean adminExists = userRepository.findAll()
                    .stream()
                    .anyMatch(user -> "ADMIN".equals(user.getRole()));

            if (adminExists) {
                System.out.println(
                        "HireNest ADMIN already exists. No bootstrap account created.");
                return;
            }

            if (username.isBlank() || email.isBlank() || password.isBlank()) {
                System.out.println(
                        "No ADMIN exists. Configure HIRENEST_BOOTSTRAP_ADMIN_USERNAME, "
                        + "HIRENEST_BOOTSTRAP_ADMIN_EMAIL and "
                        + "HIRENEST_BOOTSTRAP_ADMIN_PASSWORD "
                        + "to create the initial ADMIN account.");
                return;
            }

            if (password.length() < 12) {
                throw new IllegalStateException(
                        "The initial ADMIN password must be at least 12 characters.");
            }

            String normalizedUsername = username.trim();
            String normalizedEmail = email.trim()
                    .toLowerCase(java.util.Locale.ROOT);

            if (userRepository.existsByEmail(normalizedEmail)) {
                throw new IllegalStateException(
                        "The configured ADMIN email already belongs to an existing account.");
            }

            if (userRepository.existsByUsername(normalizedUsername)) {
                throw new IllegalStateException(
                        "The configured ADMIN username is already in use.");
            }

            User admin = new User(
                    normalizedUsername,
                    normalizedUsername, // Initial full name
                    normalizedEmail,
                    passwordEncoder.encode(password),
                    "ADMIN"
            );

            admin.setActive(true);
            admin.setEmailVerified(true);
            admin.setMustChangePassword(false);

            userRepository.save(admin);

            System.out.println(
                    "Initial HireNest ADMIN account created successfully.");
        };
    }
}
