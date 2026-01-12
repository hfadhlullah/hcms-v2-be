package com.example.hcms.auth.service;

import com.example.hcms.auth.domain.User;
import com.example.hcms.auth.dto.CreateUserRequest;
import com.example.hcms.auth.dto.UpdateUserRequest;
import com.example.hcms.auth.dto.UserResponse;
import com.example.hcms.auth.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.lang.NonNull;
import java.util.Set;
import java.util.UUID;

/**
 * Service for managing users (authentication and authorization only).
 * Member/employee profile management has been moved to MemberService.
 */
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Create a new user account for authentication
     * Note: Use MemberService to create the associated member profile
     *
     * @param request the create user request
     * @return the created user response
     */
    public UserResponse createUser(@NonNull CreateUserRequest request) {
        // Generate username from name or email
        String username = generateUsername(request);

        // Generate a temporary password (user will be invited)
        String tempPassword = UUID.randomUUID().toString().substring(0, 8);
        String passwordHash = passwordEncoder.encode(tempPassword);

        // Create user entity with minimal fields
        User user = new User();
        user.setEmail(request.getEmail() != null ? request.getEmail() : username + "@temp.local");
        user.setUsername(username);
        user.setPasswordHash(passwordHash);
        user.setStatus(User.UserStatus.INACTIVE); // INACTIVE until they accept invitation

        User savedUser = userRepository.save(user);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getEmail(),
                null, // firstName stored in Member, not User
                null, // lastName stored in Member, not User
                Set.of("EMPLOYEE") // Default role
        );
    }

    /**
     * Get all users with pagination
     *
     * @param pageable pagination info
     * @return page of user responses
     */
    @Transactional(readOnly = true)
    public Page<UserResponse> getAllUsers(@NonNull Pageable pageable) {
        return userRepository.findAll(pageable)
                .map((@NonNull User user) -> new UserResponse(
                        user.getId(),
                        user.getEmail(),
                        null, // firstName stored in Member, not User
                        null, // lastName stored in Member, not User
                        user.getRoles().stream()
                                .map(ur -> ur.getRole().name())
                                .collect(java.util.stream.Collectors.toSet())));
    }

    /**
     * Generate a unique username from the request
     */
    private String generateUsername(CreateUserRequest request) {
        String baseName = request.getName().toLowerCase().replaceAll("[^a-z0-9]", "");
        String username = baseName;
        int counter = 1;

        while (userRepository.findByUsername(username).isPresent()) {
            username = baseName + counter;
            counter++;
        }

        return username;
    }

    /**
     * Reset a user's password
     *
     * @param userId   the user ID
     * @param password the new password, or null to generate by system
     * @return the generated password if generateBySystem is true, otherwise null
     */
    public String resetPassword(@NonNull Long userId, String password) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String newPassword;
        if (password != null && !password.isEmpty()) {
            newPassword = password;
        } else {
            // Generate a random password
            newPassword = UUID.randomUUID().toString().substring(0, 12);
        }

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        user.setStatus(User.UserStatus.ACTIVE); // Activate user if they were invited
        userRepository.save(user);

        return newPassword;
    }

    /**
     * Update an existing user's authentication credentials
     * Note: Use MemberService to update member profile information
     *
     * @param id      user ID
     * @param request update request
     * @return updated user response
     */
    public UserResponse updateUser(@NonNull Long id, UpdateUserRequest request) {
        User user = java.util.Objects.requireNonNull(
                userRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("User not found")));

        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }

        User savedUser = userRepository.save(user);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getEmail(),
                null, // firstName stored in Member, not User
                null, // lastName stored in Member, not User
                savedUser.getRoles().stream()
                        .map(ur -> ur.getRole().name())
                        .collect(java.util.stream.Collectors.toSet()));
    }

    /**
     * Delete a user account
     *
     * @param id user ID
     */
    public void deleteUser(@NonNull Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }
}
