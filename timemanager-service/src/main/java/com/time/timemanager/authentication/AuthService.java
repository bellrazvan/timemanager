package com.time.timemanager.authentication;

import com.time.timemanager.authentication.dtos.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface AuthService {
    ResponseEntity<?> register(final RegisterRequest request);

    ResponseEntity<?> confirmAccount(final String token);

    ResponseEntity<?> login(final LoginRequest request);

    ResponseEntity<?> refreshToken(final String refreshToken);

    ResponseEntity<?> logout();

    ResponseEntity<?> requestPasswordReset(final PasswordResetInitRequest request);

    ResponseEntity<?> resetPassword(final PasswordResetSubmitRequest request);

    ResponseEntity<?> deleteUser(final Authentication auth);

    ResponseEntity<?> reactivateUser(final ReactivateUserRequest request);

    ResponseEntity<?> getUserStatus(final String email);

    ResponseEntity<?> getUserDetails(final Authentication auth);
}

