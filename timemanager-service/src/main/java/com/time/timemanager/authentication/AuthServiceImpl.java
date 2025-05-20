package com.time.timemanager.authentication;

import com.time.timemanager.authentication.dtos.*;
import com.time.timemanager.authentication.services.accountmanagement.AccountManagementService;
import com.time.timemanager.authentication.services.login.UserLoginService;
import com.time.timemanager.authentication.services.passwordreset.PasswordResetService;
import com.time.timemanager.authentication.services.registration.UserRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AccountManagementService accountManagementService;
    private final UserRegistrationService userRegistrationService;
    private final PasswordResetService passwordResetService;
    private final UserLoginService userLoginService;

    @Override
    public ResponseEntity<?> register(final RegisterRequest request) {
        return this.userRegistrationService.register(request);
    }

    @Override
    public ResponseEntity<?> confirmAccount(final String token) {
        return this.userRegistrationService.confirmAccount(token);
    }

    @Override
    public ResponseEntity<?> login(final LoginRequest request) {
        return this.userLoginService.login(request);
    }

    @Override
    public ResponseEntity<?> refreshToken(final String refreshToken) {
        return this.userLoginService.refreshToken(refreshToken);
    }

    @Override
    public ResponseEntity<?> logout() {
        return this.userLoginService.logout();
    }

    @Override
    public ResponseEntity<?> requestPasswordReset(final PasswordResetInitRequest request) {
        return this.passwordResetService.requestPasswordReset(request);
    }

    @Override
    public ResponseEntity<?> resetPassword(final PasswordResetSubmitRequest request) {
        return this.passwordResetService.resetPassword(request);
    }

    @Override
    public ResponseEntity<?> deleteUser(final Authentication auth) {
        return this.accountManagementService.deleteUser(auth);
    }

    @Override
    public ResponseEntity<?> reactivateUser(final ReactivateUserRequest request) {
        return this.accountManagementService.reactivateUser(request);
    }
}
