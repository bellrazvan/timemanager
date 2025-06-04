package com.time.timemanager.authentication.services.accountmanagement;

import com.time.timemanager.authentication.*;
import com.time.timemanager.authentication.dtos.ReactivateUserRequest;
import com.time.timemanager.config.ApiResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountManagementServiceImpl implements AccountManagementService {
    private final UserRepository userRepository;

    @Override
    public ResponseEntity<?> deleteUser(final Authentication auth) {
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponseMapper.errorResponse("Authentication required"));
        }

        final String email = auth.getName();
        final Optional<User> userOpt = this.userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponseMapper.errorResponse("User not found"));
        }
        userOpt.get().setStatus(UserStatus.INACTIVE);
        userOpt.get().setInactiveSince(LocalDate.now());
        this.userRepository.save(userOpt.get());

        SecurityContextHolder.clearContext();
        ResponseCookie cookie = this.deleteRefreshTokenCookie();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(ApiResponseMapper.successfulResponse("Account deactivated successfully, you have 30 days until your account is deleted"));
    }

    @Override
    public ResponseEntity<?> reactivateUser(final ReactivateUserRequest request) {
        final Optional<User> userOpt = this.userRepository.findByEmail(request.email());
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponseMapper.errorResponse("User not found"));
        }

        userOpt.get().setStatus(UserStatus.ACTIVE);
        userOpt.get().setInactiveSince(null);
        this.userRepository.save(userOpt.get());

        return ResponseEntity.ok(ApiResponseMapper.successfulResponse("Account activated successfully"));
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
