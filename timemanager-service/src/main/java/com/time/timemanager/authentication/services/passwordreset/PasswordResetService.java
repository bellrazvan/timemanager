package com.time.timemanager.authentication.services.passwordreset;

import com.time.timemanager.authentication.dtos.PasswordResetInitRequest;
import com.time.timemanager.authentication.dtos.PasswordResetSubmitRequest;
import org.springframework.http.ResponseEntity;

public interface PasswordResetService {
    /**
     * Initiates the password reset process for a user.
     *
     * This method accepts a request containing the necessary information to
     * initiate a password reset, such as the user's email address. It will
     * send a password reset link or instructions to the user's email if the
     * request is valid.
     *
     * @param request the password reset initialization request containing
     *                the user's email address and any other required information.
     * @return a ResponseEntity indicating the result of the password reset
     *         request, which may include a success message or an error message
     *         if the request fails.
     */
    ResponseEntity<?> requestPasswordReset(final PasswordResetInitRequest request);

    /**
     * Resets the password for a user based on the provided request.
     *
     * This method processes the password reset request, validates the information,
     * and updates the user's password if the request is valid.
     *
     * @param request the password reset request containing the necessary information
     *                such as the user's email and the new password.
     * @return a ResponseEntity indicating the result of the password reset process,
     *         which may include a success message or an error message if the request fails.
     */
    ResponseEntity<?> resetPassword(final PasswordResetSubmitRequest request);
}
