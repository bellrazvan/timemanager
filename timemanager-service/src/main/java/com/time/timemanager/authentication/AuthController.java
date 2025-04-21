package com.time.timemanager.authentication;

import com.time.timemanager.authentication.dtos.LoginRequest;
import com.time.timemanager.authentication.dtos.PasswordResetInitRequest;
import com.time.timemanager.authentication.dtos.PasswordResetSubmitRequest;
import com.time.timemanager.authentication.dtos.RegisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
       return this.authService.register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        return this.authService.login(request);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@CookieValue(name = "refreshToken", required = false) String refreshToken) {
        return this.authService.refreshToken(refreshToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
       return this.authService.logout();
    }

    @PostMapping("/reset-request")
    public ResponseEntity<?> requestPasswordReset(@Valid @RequestBody PasswordResetInitRequest request) {
        return this.authService.requestPasswordReset(request);
    }

    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody PasswordResetSubmitRequest request) {
        return this.authService.resetPassword(request);
    }
}