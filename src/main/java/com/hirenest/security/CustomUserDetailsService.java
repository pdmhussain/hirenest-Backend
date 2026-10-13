
package com.hirenest.security;

import com.hirenest.entity.User;
import com.hirenest.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Set<String> ALLOWED_ROLES =
            Set.of("ADMIN", "HR", "USER");

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        if (email == null || email.isBlank()) {
            throw new UsernameNotFoundException("Email is required");
        }

        String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);

        User user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        if (!user.isActive()) {
            throw new UsernameNotFoundException("User is inactive");
        }

        if (!user.isEmailVerified()) {
            throw new UsernameNotFoundException("Email is not verified");
        }

        String role = user.getRole();

        if (role == null || role.isBlank()) {
            throw new UsernameNotFoundException("User has an invalid role");
        }

        role = role.trim().toUpperCase(Locale.ROOT);

        if (!ALLOWED_ROLES.contains(role)) {
            throw new UsernameNotFoundException("User has an invalid role");
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(normalizedEmail)
                .password(user.getPassword())
                .roles(role)
                .build();
    }
}
