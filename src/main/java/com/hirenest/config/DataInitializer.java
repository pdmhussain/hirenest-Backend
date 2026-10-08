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
    CommandLineRunner createTestUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            User user = userRepository.findByEmail("hr@hirenest.com").orElse(null);
            if (user == null) {
                user = new User("hr@hirenest.com", passwordEncoder.encode("Hr@12345"), "HR");
            }
            user.setActive(true);
            user.setEmailVerified(true);
            userRepository.save(user);
            System.out.println("Test user ready: hr@hirenest.com / Hr@12345");
        };
    }
}
