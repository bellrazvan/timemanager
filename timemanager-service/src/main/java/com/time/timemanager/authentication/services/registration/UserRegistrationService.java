package com.time.timemanager.authentication.services.registration;

import com.time.timemanager.authentication.dtos.RegisterRequest;
import org.springframework.http.ResponseEntity;

public interface UserRegistrationService {
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
}
