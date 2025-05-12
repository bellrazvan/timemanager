package com.time.timemanager.authentication;

import com.time.timemanager.authentication.dtos.*;
import com.time.timemanager.config.exceptions.AccountInactiveException;
import com.time.timemanager.config.exceptions.AccountUnconfirmedException;
import com.time.timemanager.mail.EmailService;
import com.time.timemanager.security.JwtUtil;
import com.time.timemanager.tasks.TaskRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtUtil jwtUtil;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private static final Logger LOG = LogManager.getLogger(AuthService.class);

//    public ResponseEntity<?> register(final RegisterRequest request) {
//        if (this.userRepository.existsByEmail(request.email())) {
//            return ResponseEntity.badRequest().body("Email already in use");
//        }
//
//        final User user = User.builder()
//                .username(request.username())
//                .email(request.email())
//                .password(this.passwordEncoder.encode(request.password()))
//                .status(UserStatus.ACTIVE)
//                .build();
//        this.userRepository.save(user);
//
//        return ResponseEntity.ok("User registered successfully");
//    }

    public ResponseEntity<?> register(final RegisterRequest request) {
        if (this.userRepository.existsByEmail(request.email())) {
            return ResponseEntity.badRequest().body("Email already in use");
        }

        final String token = UUID.randomUUID().toString();
        final User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(this.passwordEncoder.encode(request.password()))
                .status(UserStatus.UNCONFIRMED)
                .confirmationToken(token)
                .build();
        this.userRepository.save(user);

        this.emailService.sendConfirmationEmail(request.email(), user.getUsername(), token);

        return ResponseEntity.ok("User registered successfully. Please check your email to confirm your account.");
    }

    public ResponseEntity<?> confirmAccount(final String token) {
        final Optional<User> userOpt = this.userRepository.findByConfirmationToken(token);
        if (userOpt.isPresent()) {
            final User user = userOpt.get();
            user.setStatus(UserStatus.ACTIVE);
            user.setConfirmationToken(null);
            this.userRepository.save(user);
            return ResponseEntity.ok("Account confirmed successfully.");
        }
        return ResponseEntity.badRequest().body("Invalid confirmation token.");
    }


    public ResponseEntity<?> login(final LoginRequest request) {
        final Authentication auth = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        final CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();

        this.checkUserStatus(user.getStatus());

        final String accessToken = this.jwtUtil.generateAccessToken(user);
        final String refreshToken = this.jwtUtil.generateRefreshToken(user);

        final ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/auth/refresh")
                .maxAge(Duration.ofDays(7))
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(Map.of("accessToken", accessToken));
    }

    public ResponseEntity<?> refreshToken(final String refreshToken) {
        if (refreshToken == null || this.jwtUtil.isInvalidToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }

        final String username = this.jwtUtil.getUsernameFromToken(refreshToken);
        final CustomUserDetails user = this.customUserDetailsService.loadUserByUsername(username);

        final String newAccessToken = this.jwtUtil.generateAccessToken(user);
        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }

    public ResponseEntity<?> logout() {
        final ResponseCookie deleteCookie = this.deleteRefreshTokenCookie();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                .body("Successfully logged out");
    }

    public ResponseEntity<?> requestPasswordReset(@Valid @RequestBody final PasswordResetInitRequest request) {
        final Optional<User> userOpt = this.userRepository.findByEmail(request.email());
        if (userOpt.isEmpty()) {
            return ResponseEntity.ok("If the email exists, a reset link will be sent");
        }

        this.checkUserStatus(userOpt.get().getStatus());

        final String token = this.jwtUtil.generatePasswordResetToken(request.email());

        this.emailService.sendPasswordResetEmail(request.email(), userOpt.get().getUsername(), token);

        return ResponseEntity.ok("If the email exists, a reset link will be sent");
    }

    public ResponseEntity<?> resetPassword(@Valid @RequestBody final PasswordResetSubmitRequest request) {
        final String email;
        try {
            email = this.jwtUtil.getUsernameFromToken(request.token());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid or expired token");
        }

        final Optional<User> userOpt = this.userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        final User user = userOpt.get();
        user.setPassword(this.passwordEncoder.encode(request.newPassword()));
        this.userRepository.save(user);

        return ResponseEntity.ok("Password updated successfully");
    }

    public ResponseEntity<?> deleteUser(final Authentication auth) {
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required");
        }

        final String email = auth.getName();
        final Optional<User> userOpt = this.userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }
        userOpt.get().setStatus(UserStatus.INACTIVE);
        userOpt.get().setInactiveSince(LocalDate.now());
        this.userRepository.save(userOpt.get());

        SecurityContextHolder.clearContext();
        ResponseCookie cookie = this.deleteRefreshTokenCookie();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Account deactivated successfully, you have 30 days until your account is deleted");
    }

    public ResponseEntity<?> reactivateUser(final ReactivateUserRequest request) {
        final Optional<User> userOpt = this.userRepository.findByEmail(request.email());
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        userOpt.get().setStatus(UserStatus.ACTIVE);
        userOpt.get().setInactiveSince(null);
        this.userRepository.save(userOpt.get());

        return ResponseEntity.ok("Account activated successfully");
    }

    @Scheduled(cron = "0 0 8 * * *")
    public void deleteInactiveUsers() {
        final List<User> inactiveUsers = this.getInactiveUsersForOverThirtyDays();

        inactiveUsers.forEach(user -> {
            this.taskRepository.deleteByUser(user);
            this.userRepository.delete(user);
            LOG.info("Deleted inactive user: {}", user.getEmail());
        });

        LOG.info("Completed inactive user cleanup. Removed {} accounts.", inactiveUsers.size());
    }

    private List<User> getInactiveUsersForOverThirtyDays() {
        final LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);
        return this.userRepository.findByStatusAndInactiveSinceBefore(UserStatus.INACTIVE, thirtyDaysAgo);
    }

    private void checkUserStatus(final UserStatus status) throws AccountInactiveException, AccountUnconfirmedException {
        switch (status) {
            case INACTIVE:
                throw new AccountInactiveException("Account is inactive");
            case UNCONFIRMED:
                throw new AccountUnconfirmedException("Account is not confirmed yet");
            default:
                break;
        }
    }

    private ResponseCookie deleteRefreshTokenCookie() {
        return ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/auth/refresh")
                .maxAge(0)
                .build();
    }
}
