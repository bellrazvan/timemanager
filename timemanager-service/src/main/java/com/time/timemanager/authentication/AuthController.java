package com.time.timemanager.authentication;

import com.time.timemanager.authentication.dtos.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody final RegisterRequest request) {
       return this.authService.register(request);
    }

    @PostMapping("/confirm")
    public ResponseEntity<?> confirmAccount(@RequestParam String token) {
        return this.authService.confirmAccount(token);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        return this.authService.login(request);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@CookieValue(name = "refreshToken", required = false) final String refreshToken) {
        return this.authService.refreshToken(refreshToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
       return this.authService.logout();
    }

    @PostMapping("/reset-request")
    public ResponseEntity<?> requestPasswordReset(@Valid @RequestBody final PasswordResetInitRequest request) {
        return this.authService.requestPasswordReset(request);
    }

    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody final PasswordResetSubmitRequest request) {
        return this.authService.resetPassword(request);
    }

    @DeleteMapping("/delete-account")
    public ResponseEntity<?> deleteAccount(final Authentication auth) {
        return this.authService.deleteUser(auth);
    }

    @PostMapping("/activate-user")
    public ResponseEntity<?> reactivateUser(@Valid @RequestBody final ReactivateUserRequest request) {
        return this.authService.reactivateUser(request);
    }

    @GetMapping("/user-status")
    public ResponseEntity<?> getUserStatus(@RequestParam final String email) {
        return this.authService.getUserStatus(email);
    }

    @GetMapping("/user-details")
    public ResponseEntity<?> getUserDetails(final Authentication auth) {
        return this.authService.getUserDetails(auth);
    }
}