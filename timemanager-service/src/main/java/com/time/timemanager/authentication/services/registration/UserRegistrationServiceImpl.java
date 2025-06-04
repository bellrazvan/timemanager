package com.time.timemanager.authentication.services.registration;

import com.time.timemanager.authentication.*;
import com.time.timemanager.authentication.dtos.RegisterRequest;
import com.time.timemanager.config.ApiResponseMapper;
import com.time.timemanager.mail.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class UserRegistrationServiceImpl implements UserRegistrationService {
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<?> register(final RegisterRequest request) {
        if (this.userRepository.existsByEmail(request.email())) {
            return ResponseEntity.badRequest().body(ApiResponseMapper.errorResponse("Email already in use"));
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

        CompletableFuture.runAsync(() -> this.emailService.sendConfirmationEmail(request.email(), user.getUsername(), token));

        return ResponseEntity.ok(ApiResponseMapper.successfulResponse("User registered successfully. Please check your email to confirm your account."));
    }

    @Override
    public ResponseEntity<?> confirmAccount(final String token) {
        final Optional<User> userOpt = this.userRepository.findByConfirmationToken(token);
        if (userOpt.isPresent()) {
            final User user = userOpt.get();
            user.setStatus(UserStatus.ACTIVE);
            user.setConfirmationToken(null);
            this.userRepository.save(user);
            return ResponseEntity.ok(ApiResponseMapper.successfulResponse("Account confirmed successfully."));
        }
        return ResponseEntity.badRequest().body(ApiResponseMapper.errorResponse("Invalid confirmation token."));
    }
}
