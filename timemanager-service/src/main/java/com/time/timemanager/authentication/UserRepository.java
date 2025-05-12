package com.time.timemanager.authentication;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findByStatusAndInactiveSinceBefore(UserStatus status, LocalDate inactiveSince);
    Optional<User> findByConfirmationToken(String token);
}