package com.time.timemanager.authentication;

import com.time.timemanager.authentication.dtos.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface AuthService {
    /**
     * Registers a new user with the provided registration details.
     *
     * @param request the registration request containing user details such as username, password, and email.
     * @return a ResponseEntity containing the result of the registration process, which may include success or error messages.
     */
    ResponseEntity<?> register(final RegisterRequest request);

    /**
     * Confirms the user's account using the provided token.
     *
     * This method validates the token and activates the user's account if the token is valid.
     *
     * @param token the token used to confirm the user's account, typically sent via email.
     * @return a ResponseEntity containing the result of the account confirmation process,
     *         which may include success or error messages.
     */
    ResponseEntity<?> confirmAccount(final String token);

    /**
     * Authenticates a user based on the provided login request.
     *
     * This method processes the login request, validates the user's credentials,
     * and returns a response indicating the success or failure of the login attempt.
     *
     * @param request the login request containing the user's credentials, such as username and password.
     * @return a ResponseEntity containing the result of the login process,
     *         which may include success or error messages.
     */
    ResponseEntity<?> login(final LoginRequest request);

    /**
     * Refreshes the authentication token using the provided refresh token.
     *
     * This method validates the refresh token and issues a new access token if the refresh token is valid.
     *
     * @param refreshToken the refresh token used to obtain a new access token.
     * @return a ResponseEntity containing the result of the token refresh process,
     *         which may include the new access token or an error message if the refresh token is invalid.
     */
    ResponseEntity<?> refreshToken(final String refreshToken);

    /**
     * Logs out the currently authenticated user.
     *
     * This method invalidates the user's session and any associated tokens,
     * effectively logging the user out of the application.
     *
     * @return a ResponseEntity indicating the result of the logout process,
     *         which may include a success message or an error message if the logout fails.
     */
    ResponseEntity<?> logout();

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

    /**
     * Deletes a user from the system based on the provided authentication information.
     *
     * This method verifies the user's identity using the provided authentication object
     * and proceeds to delete the user's account if the user is authorized to perform this action.
     *
     * @param auth the authentication object containing the user's credentials and details.
     * @return a ResponseEntity indicating the result of the delete operation,
     *         which may include a success message or an error message if the deletion fails.
     */
    ResponseEntity<?> deleteUser(final Authentication auth);

    /**
     * Reactivates a user account based on the provided reactivation request.
     *
     * This method processes the reactivation request, verifies the user's identity,
     * and reactivates the user's account if the request is valid and the user is eligible for reactivation.
     *
     * @param request the reactivation request containing the necessary information
     *                such as the user's ID and any required verification details.
     * @return a ResponseEntity indicating the result of the reactivation process,
     *         which may include a success message or an error message if the reactivation fails.
     */
    ResponseEntity<?> reactivateUser(final ReactivateUserRequest request);
}

