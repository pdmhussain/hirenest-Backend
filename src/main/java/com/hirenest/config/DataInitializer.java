
package com.hirenest.config;

import com.hirenest.entity.User;
import com.hirenest.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner createTestUsers(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {
            createUser(
                    userRepository, passwordEncoder,
                    "hr@hirenest.com", "Hr@12345", "HR"
            );

            createUser(
                    userRepository, passwordEncoder,
                    "sunny@gmail.com", "Emp@12345", "EMPLOYEE"
            );

            createUser(
                    userRepository, passwordEncoder,
                    "admin@hirenest.com", "Admin@12345", "ADMIN"
            );
        };
    }

    private void createUser(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            String email,
            String password,
            String role) {

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            user = new User(
                    email,
                    passwordEncoder.encode(password),
                    role
            );

            user.setActive(true);
            user.setEmailVerified(true);
            userRepository.save(user);

            System.out.println("Created test user: " + email
                    + " | Role: " + role);
        } else {
            System.out.println("User already exists: " + email);
        }
    }
}
