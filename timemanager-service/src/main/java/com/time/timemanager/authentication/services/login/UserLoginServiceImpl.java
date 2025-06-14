package com.time.timemanager.authentication.services.login;

import com.time.timemanager.authentication.*;
import com.time.timemanager.authentication.dtos.LoginRequest;
import com.time.timemanager.authentication.exceptions.AccountInactiveException;
import com.time.timemanager.authentication.exceptions.AccountUnconfirmedException;
import com.time.timemanager.config.ApiResponseMapper;
import com.time.timemanager.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class UserLoginServiceImpl implements UserLoginService {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    public ResponseEntity<?> login(final LoginRequest request) {
        final Authentication auth = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        final CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();

        try {
            this.checkUserStatus(user.getStatus());
        } catch (AccountInactiveException | AccountUnconfirmedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponseMapper.errorResponse(e.getMessage()));
        }

        final String accessToken = this.jwtUtil.generateAccessToken(user);
        final String refreshToken = this.jwtUtil.generateRefreshToken(user);

        final ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/api/auth/refresh")
                .maxAge(Duration.ofDays(7))
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(ApiResponseMapper.accessTokenResponse(accessToken));
    }

    @Override
    public ResponseEntity<?> refreshToken(final String refreshToken) {
        if (refreshToken == null || this.jwtUtil.isInvalidToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponseMapper.errorResponse("Invalid refresh token."));
        }

        final String username = this.jwtUtil.getUsernameFromToken(refreshToken);

        final CustomUserDetails user;
        try {
            user = this.customUserDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.badRequest().body(ApiResponseMapper.errorResponse(e.getMessage()));
        }

        final String newAccessToken = this.jwtUtil.generateAccessToken(user);
        return ResponseEntity.ok(ApiResponseMapper.accessTokenResponse(newAccessToken));
    }

    @Override
    public ResponseEntity<?> logout() {
        final ResponseCookie deleteCookie = this.deleteRefreshTokenCookie();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                .body(ApiResponseMapper.successfulResponse("Successfully logged out."));
    }

    private void checkUserStatus(final UserStatus status) throws AccountInactiveException, AccountUnconfirmedException {
        switch (status) {
            case INACTIVE:
                throw new AccountInactiveException("Account is inactive.");
            case UNCONFIRMED:
                throw new AccountUnconfirmedException("Account is not confirmed yet.");
            default:
                break;
        }
    }

    private ResponseCookie deleteRefreshTokenCookie() {
        return ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/api/auth/refresh")
                .maxAge(0)
                .build();
    }
}
