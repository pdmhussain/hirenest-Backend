
package com.hirenest.service.impl;

import com.hirenest.dto.UserRequest;
import com.hirenest.entity.User;
import com.hirenest.repository.UserRepository;
import com.hirenest.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Prevent non-admin users from assigning the ADMIN role.
     */
    private void validateRoleAssignment(String requestedRole) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        boolean isAdmin = authentication != null
                && authentication.getAuthorities().stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_ADMIN"));

        if ("ADMIN".equalsIgnoreCase(requestedRole) && !isAdmin) {
            throw new AccessDeniedException(
                    "Only ADMIN can assign the ADMIN role"
            );
        }
    }

    @Override
    public User createUser(UserRequest request) {

        // Validate the requested role
        validateRoleAssignment(request.getRole());

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException(
                    "User with this email already exists"
            );
        }

        User user = new User();

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());

        // Encrypt password before saving
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        user.setRole(request.getRole());
        user.setStatus(request.getStatus());
        user.setActive(request.isActive());
        user.setEmailVerified(request.isEmailVerified());

        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "User not found with id: " + id
                        )
                );
    }

    @Override
    public User updateUser(Long id, UserRequest request) {

        // Validate the requested role before modifying the user
        validateRoleAssignment(request.getRole());

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "User not found with id: " + id
                        )
                );

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        user.setStatus(request.getStatus());
        user.setActive(request.isActive());
        user.setEmailVerified(request.isEmailVerified());

        // Encrypt password only when a new password is provided
        if (request.getPassword() != null
                && !request.getPassword().isBlank()) {

            user.setPassword(
                    passwordEncoder.encode(request.getPassword())
            );
        }

        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "User not found with id: " + id
                        )
                );

        userRepository.delete(user);
    }
}

