package com.time.timemanager.authentication.services.accountmanagement;

import com.time.timemanager.authentication.dtos.ReactivateUserRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface AccountManagementService {
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

    /**
     * Retrieves the details of the currently authenticated user.
     *
     * @param auth the authentication object containing the user's authentication information
     * @return a ResponseEntity containing the user details or an appropriate error response
     */
    ResponseEntity<?> getUserDetails(final Authentication auth);
}
