package com.time.timemanager.authentication;

import com.time.timemanager.authentication.dtos.LoginRequest;
import com.time.timemanager.authentication.dtos.PasswordResetInitRequest;
import com.time.timemanager.authentication.dtos.PasswordResetSubmitRequest;
import com.time.timemanager.authentication.dtos.RegisterRequest;
import com.time.timemanager.mail.EmailService;
import com.time.timemanager.security.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    @Value("${frontend.url}")
    private String FRONTEND_URL;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;

    public ResponseEntity<?> register(RegisterRequest request) {
        if (this.userRepository.existsByEmail(request.email())) {
            return ResponseEntity.badRequest().body("Email already in use");
        }

        final User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(this.passwordEncoder.encode(request.password()))
                .build();
        this.userRepository.save(user);

        return ResponseEntity.ok("User registered successfully");
    }

    public ResponseEntity<?> login(LoginRequest request) {
        final Authentication auth = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        final CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
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

    public ResponseEntity<?> refreshToken(String refreshToken) {
        if (refreshToken == null || this.jwtUtil.isInvalidToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }

        final String username = this.jwtUtil.getUsernameFromToken(refreshToken);
        final CustomUserDetails user = this.customUserDetailsService.loadUserByUsername(username);

        final String newAccessToken = this.jwtUtil.generateAccessToken(user);
        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }

    public ResponseEntity<?> logout() {
        final ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/auth/refresh")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                .body("Successfully logged out");
    }

    public ResponseEntity<?> requestPasswordReset(@Valid @RequestBody PasswordResetInitRequest request) {
        final Optional<User> userOpt = this.userRepository.findByEmail(request.email());
        if (userOpt.isEmpty()) {
            return ResponseEntity.ok("If the email exists, a reset link will be sent");
        }

        final String token = this.jwtUtil.generatePasswordResetToken(request.email());
        final String resetLink = FRONTEND_URL + "/reset-password?token=" + token;

        this.emailService.sendPasswordResetEmail(request.email(), userOpt.get().getUsername(), resetLink);

        return ResponseEntity.ok("If the email exists, a reset link will be sent");
    }

    public ResponseEntity<?> resetPassword(@Valid @RequestBody PasswordResetSubmitRequest request) {
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
}
