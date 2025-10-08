package com.ecommerce.backend.service;

import com.ecommerce.backend.entity.User;
import com.ecommerce.backend.enums.Role;
import com.ecommerce.backend.enums.UserStatus;
import com.ecommerce.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.debug("Attempting to load user by email: {}", email);
        
        Optional<User> userOpt = userRepository.findByEmailAndDeletedAtIsNull(email);
        if (userOpt.isEmpty()) {
            // Try without deleted check as fallback
            userOpt = userRepository.findByEmail(email);
            if (userOpt.isEmpty()) {
                log.error("User not found with email: {}", email);
                throw new UsernameNotFoundException("User not found with email: " + email);
            } else {
                User user = userOpt.get();
                if (user.isDeleted()) {
                    log.error("User found but is deleted: {}", email);
                    throw new UsernameNotFoundException("User account is deleted: " + email);
                }
            }
        }
        
        User user = userOpt.get();
        log.debug("User found: {} with status: {} and emailVerified: {}", 
                user.getEmail(), user.getStatus(), user.getEmailVerified());
        
        return user;
    }

    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEmailVerificationToken(UUID.randomUUID().toString());
        user.setStatus(UserStatus.PENDING_VERIFICATION);
        
        User savedUser = userRepository.save(user);
        log.info("Created new user with email: {}", user.getEmail());
        return savedUser;
    }

    public User updateUser(Long id, User userDetails) {
        User user = getUserById(id);
        
        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());
        user.setPhone(userDetails.getPhone());
        user.setDateOfBirth(userDetails.getDateOfBirth());
        
        if (userDetails.getEmail() != null && !userDetails.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(userDetails.getEmail())) {
                throw new RuntimeException("Email already exists");
            }
            user.setEmail(userDetails.getEmail());
            user.setEmailVerified(false);
            user.setEmailVerificationToken(UUID.randomUUID().toString());
        }
        
        User updatedUser = userRepository.save(user);
        log.info("Updated user with id: {}", id);
        return updatedUser;
    }

    public User changePassword(Long id, String currentPassword, String newPassword) {
        User user = getUserById(id);
        
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        User updatedUser = userRepository.save(user);
        log.info("Changed password for user with id: {}", id);
        return updatedUser;
    }

    public User verifyEmail(String token) {
        User user = userRepository.findByEmailVerificationToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid verification token"));
        
        user.setEmailVerified(true);
        user.setEmailVerificationToken(null);
        user.setStatus(UserStatus.ACTIVE);
        
        User verifiedUser = userRepository.save(user);
        log.info("Verified email for user: {}", user.getEmail());
        return verifiedUser;
    }

    public void initiatePasswordReset(String email) {
        Optional<User> userOpt = userRepository.findByEmailAndDeletedAtIsNull(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setPasswordResetToken(UUID.randomUUID().toString());
            user.setPasswordResetExpiresAt(LocalDateTime.now().plusHours(1));
            userRepository.save(user);
            log.info("Initiated password reset for user: {}", email);
            // TODO: Send password reset email
        }
    }

    public User resetPassword(String token, String newPassword) {
        User user = userRepository.findByPasswordResetToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid password reset token"));
        
        if (user.getPasswordResetExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Password reset token has expired");
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordResetToken(null);
        user.setPasswordResetExpiresAt(null);
        
        User updatedUser = userRepository.save(user);
        log.info("Reset password for user: {}", user.getEmail());
        return updatedUser;
    }

    public User updateUserStatus(Long id, UserStatus status) {
        User user = getUserById(id);
        user.setStatus(status);
        User updatedUser = userRepository.save(user);
        log.info("Updated status for user with id: {} to {}", id, status);
        return updatedUser;
    }

    public User updateUserRole(Long id, Role role) {
        User user = getUserById(id);
        user.setRole(role);
        User updatedUser = userRepository.save(user);
        log.info("Updated role for user with id: {} to {}", id, role);
        return updatedUser;
    }

    public void deleteUser(Long id) {
        User user = getUserById(id);
        user.softDelete();
        userRepository.save(user);
        log.info("Soft deleted user with id: {}", id);
    }

    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .filter(user -> !user.isDeleted())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmailAndDeletedAtIsNull(email);
    }

    @Transactional(readOnly = true)
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findByDeletedAtIsNull(pageable);
    }

    @Transactional(readOnly = true)
    public Page<User> searchUsers(String search, Pageable pageable) {
        return userRepository.findBySearchTerm(search, pageable);
    }

    @Transactional(readOnly = true)
    public List<User> getUsersByRole(Role role) {
        return userRepository.findByRole(role);
    }

    @Transactional(readOnly = true)
    public List<User> getUsersByStatus(UserStatus status) {
        return userRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public long getTotalUsersCount() {
        return userRepository.countActiveUsers();
    }

    @Transactional(readOnly = true)
    public long getUsersCountByRole(Role role) {
        return userRepository.countByRole(role);
    }
}