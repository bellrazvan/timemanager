package com.time.timemanager.authentication;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Finds a User by their email address.
     *
     * @param email the email address of the User to find
     * @return an Optional containing the found User, or empty if no User was found
     */
    @Cacheable("users")
    Optional<User> findByEmail(String email);

    /**
     * Checks if a User exists with the given email address.
     *
     * @param email the email address to check
     * @return true if a User exists with the given email, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Finds a list of Users with a specific status who have been inactive since a given date.
     *
     * @param status the status of the Users to find
     * @param inactiveSince the date before which the Users must have been inactive
     * @return a list of Users matching the specified status and inactivity date
     */
    List<User> findByStatusAndInactiveSinceBefore(UserStatus status, LocalDate inactiveSince);

    /**
     * Finds a User by their confirmation token.
     *
     * @param token the confirmation token of the User to find
     * @return an Optional containing the found User, or empty if no User was found
     */
    Optional<User> findByConfirmationToken(String token);
}
