package com.time.timemanager.tasks;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserEmail(String email);

    List<Task> findByStatusAndUserEmail(String status, String email);

    Optional<Task> findByIdAndUserEmail(Long id, String email);

    List<Task> findByDueDateAndNotificationBeforeDueDate(LocalDate dueDate, boolean notificationBeforeDueDate);

    List<Task> findByDueDateBeforeAndNotificationOverdue(LocalDate dueDate, boolean notificationOverdue);
}