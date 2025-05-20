package com.time.timemanager.authentication.services.login;

import com.time.timemanager.authentication.dtos.LoginRequest;
import org.springframework.http.ResponseEntity;

public interface UserLoginService {
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
}
