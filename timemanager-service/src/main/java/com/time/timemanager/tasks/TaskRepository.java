package com.time.timemanager.tasks;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserId(Long userId);

    List<Task> findByUserIdAndStatus(Long userId, String status);

    List<Task> findByUserIdAndDueDateBetween(Long userId, LocalDate startDate, LocalDate endDate);

    List<Task> findByUserIdAndDueDateBeforeAndStatusNot(Long userId, LocalDate dueDate, String status);

    List<Task> findByDueDateBetween(LocalDate begin, LocalDate end);
}
