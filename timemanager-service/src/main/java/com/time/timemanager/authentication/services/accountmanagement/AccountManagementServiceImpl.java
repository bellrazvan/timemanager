package com.time.timemanager.authentication.services.accountmanagement;

import com.time.timemanager.authentication.*;
import com.time.timemanager.authentication.dtos.ReactivateUserRequest;
import com.time.timemanager.authentication.dtos.UserDetailsResponse;
import com.time.timemanager.config.ApiResponseMapper;
import com.time.timemanager.tasks.Status;
import com.time.timemanager.tasks.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountManagementServiceImpl implements AccountManagementService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<?> deleteUser(final Authentication auth) {
        try {
            final User userOpt = this.getUserFromAuth(auth);
            userOpt.setStatus(UserStatus.INACTIVE);
            userOpt.setInactiveSince(LocalDate.now());
            this.userRepository.save(userOpt);
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponseMapper.errorResponse(e.getMessage()));
        }

        SecurityContextHolder.clearContext();
        ResponseCookie cookie = this.deleteRefreshTokenCookie();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(ApiResponseMapper.successfulResponse("Account deactivated successfully, you have 30 days until your account is deleted."));
    }

    @Override
    public ResponseEntity<?> reactivateUser(final ReactivateUserRequest request) {
        final Optional<User> userOpt = this.userRepository.findByEmail(request.email());
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponseMapper.errorResponse("User not found."));
        }

        if (userOpt.get().getStatus() != UserStatus.INACTIVE) {
            return ResponseEntity.badRequest().body(ApiResponseMapper.errorResponse("User is not inactive."));
        }

        if (!userOpt.get().getEmail().equals(request.email()) &&
                !this.passwordEncoder.matches(request.password(), userOpt.get().getPassword())) {
            return ResponseEntity.badRequest().body(ApiResponseMapper.errorResponse("Invalid credentials."));
        }

        userOpt.get().setStatus(UserStatus.ACTIVE);
        userOpt.get().setInactiveSince(null);
        this.userRepository.save(userOpt.get());

        return ResponseEntity.ok(ApiResponseMapper.successfulResponse("Account activated successfully."));
    }

    @Override
    public ResponseEntity<?> getUserDetails(final Authentication auth) {
        try {
            final User user = this.getUserFromAuth(auth);
            final List<Task> taskList = user.getTasks();
            final long totalTasks = taskList.size();
            final long totalDoneTasks = taskList.stream()
                    .filter(task ->
                            task.getStatus() == Status.DONE)
                    .count();
            final long totalInProgressTasks = taskList.stream()
                    .filter(task ->
                            task.getStatus() == Status.IN_PROGRESS)
                    .count();
            final long totalToDoTasks = taskList.stream()
                    .filter(task ->
                            task.getStatus() == Status.TODO)
                    .count();
            final UserDetailsResponse userDetailsResponse = new UserDetailsResponse(
                    user.getUsername(),
                    user.getEmail(),
                    totalTasks,
                    totalDoneTasks,
                    totalInProgressTasks,
                    totalToDoTasks
            );
            return ResponseEntity.ok(userDetailsResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponseMapper.errorResponse(e.getMessage()));
        }
    }

    private User getUserFromAuth(final Authentication auth) throws IllegalArgumentException {
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            throw new IllegalArgumentException("Authentication required.");
        }

        final String email = auth.getName();
        final Optional<User> userOpt = this.userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found.");
        }
        return userOpt.get();
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
