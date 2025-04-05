package com.time.timemanager.authentication;

import com.time.timemanager.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        User registeredUser = authService.register(user);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> credentials) {
        String token = authService.login(credentials.get("username"), credentials.get("password"));
        return ResponseEntity.ok(token);
    }

    @PostMapping("/reset-password-request")
    public ResponseEntity<Void> requestPasswordReset(@RequestBody Map<String, String> request) {
        authService.requestPasswordReset(request.get("email"));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestParam String token, @RequestBody Map<String, String> request) {
        authService.resetPassword(token, request.get("newPassword"));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/profile")
    public ResponseEntity<User> updateProfile(@RequestBody User updatedUser, Authentication auth) {
        User user = (User) auth.getPrincipal();
        User updated = authService.updateProfile(user.getId(), updatedUser);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/account")
    public ResponseEntity<Void> deleteAccount(Authentication auth) {
        User user = (User) auth.getPrincipal();
        authService.deleteAccount(user.getId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refreshToken(@RequestHeader("Authorization") String token) {
        String newToken = jwtUtil.refreshToken(token.substring(7));
        return ResponseEntity.ok(newToken);
    }
}
