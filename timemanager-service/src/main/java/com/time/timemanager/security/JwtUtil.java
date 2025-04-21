package com.time.timemanager.security;

import com.time.timemanager.authentication.CustomUserDetails;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;
    private static final Logger LOG = LogManager.getLogger(JwtUtil.class);

    public String generateAccessToken(CustomUserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15)) // 15 min
                .signWith(SignatureAlgorithm.HS256, this.secret)
                .compact();
    }

    public String generateRefreshToken(CustomUserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)) // 7 days
                .signWith(SignatureAlgorithm.HS256, this.secret)
                .compact();
    }

    public String generatePasswordResetToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setExpiration(new Date(System.currentTimeMillis() + 15 * 60 * 1000)) // 15 min
                .signWith(SignatureAlgorithm.HS256, this.secret)
                .compact();
    }

    private boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(this.secret)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException ex) {
            LOG.error("Token expired: " + ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            LOG.error("Unsupported JWT: " + ex.getMessage());
        } catch (MalformedJwtException ex) {
            LOG.error("Malformed JWT: " + ex.getMessage());
        } catch (SignatureException ex) {
            LOG.error("Invalid signature: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            LOG.error("Empty or null token: " + ex.getMessage());
        }
        return false;
    }

    public boolean isInvalidToken(String token) {
        return !this.validateToken(token);
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(this.secret)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
