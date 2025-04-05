package com.time.timemanager.authentication;

import com.time.timemanager.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AuthService implements UserDetailsService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public User register(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public String login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        return jwtUtil.generateToken(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), new ArrayList<>()
        );
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public void requestPasswordReset(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Email not found"));
        String resetToken = UUID.randomUUID().toString(); //TODO save resetToken in a separate table or field (ex: resetToken)
        System.out.println("Reset link: /reset-password?token=" + resetToken); //TODO send email with reset link - JavaMailSender
    }

    public void resetPassword(String token, String newPassword) {
        //TODO validate token and update password
        User user = null; //TODO find user by token from temp storage
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public User updateProfile(Long userId, User updatedUser) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (updatedUser.getEmail() != null && !user.getEmail().equals(updatedUser.getEmail())) {
            if (userRepository.existsByEmail(updatedUser.getEmail())) {
                throw new IllegalArgumentException("Email already in use");
            }
            user.setEmail(updatedUser.getEmail());
        }
        if (updatedUser.getUsername() != null) user.setUsername(updatedUser.getUsername());
        return userRepository.save(user);
    }

    public void deleteAccount(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        userRepository.delete(user);
    }
}