package com.time.timemanager.authentication.services.passwordreset;

import com.time.timemanager.authentication.*;
import com.time.timemanager.authentication.dtos.PasswordResetInitRequest;
import com.time.timemanager.authentication.dtos.PasswordResetSubmitRequest;
import com.time.timemanager.authentication.exceptions.AccountInactiveException;
import com.time.timemanager.authentication.exceptions.AccountUnconfirmedException;
import com.time.timemanager.config.ApiResponseMapper;
import com.time.timemanager.mail.EmailService;
import com.time.timemanager.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {
    private final JwtUtil jwtUtil;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<?> requestPasswordReset(final PasswordResetInitRequest request) {
        final Optional<User> userOpt = this.userRepository.findByEmail(request.email());
        if (userOpt.isEmpty()) {
            return ResponseEntity.ok(ApiResponseMapper.successfulResponse("If the email exists, a reset link will be sent"));
        }

        this.checkUserStatus(userOpt.get().getStatus());

        final String token = this.jwtUtil.generatePasswordResetToken(request.email());

        CompletableFuture.runAsync(() -> this.emailService.sendPasswordResetEmail(request.email(), userOpt.get().getUsername(), token));

        return ResponseEntity.ok(ApiResponseMapper.successfulResponse("If the email exists, a reset link will be sent"));
    }

    @Override
    public ResponseEntity<?> resetPassword(final PasswordResetSubmitRequest request) {
        final String email;
        try {
            email = this.jwtUtil.getUsernameFromToken(request.token());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponseMapper.errorResponse("Invalid or expired token"));
        }

        final Optional<User> userOpt = this.userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponseMapper.errorResponse("User not found"));
        }

        final User user = userOpt.get();
        if (user.getPassword().equals(request.newPassword())) {
            return ResponseEntity.badRequest().body(ApiResponseMapper.errorResponse("New password cannot be the same as the old one"));
        }

        user.setPassword(this.passwordEncoder.encode(request.newPassword()));
        this.userRepository.save(user);

        return ResponseEntity.ok(ApiResponseMapper.successfulResponse("Password updated successfully"));
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
}
