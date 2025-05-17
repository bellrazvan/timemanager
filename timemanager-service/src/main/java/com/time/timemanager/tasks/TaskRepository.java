package com.time.timemanager.tasks;

import com.time.timemanager.authentication.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    /**
     * Retrieves a list of tasks associated with the specified user email.
     *
     * @param email the email of the user whose tasks are to be retrieved
     * @return a list of tasks associated with the given user email
     */
    List<Task> findByUserEmail(String email);

    /**
     * Retrieves a list of tasks that match the specified status and are associated with the given user email.
     *
     * @param status the status of the tasks to be retrieved
     * @param email  the email of the user whose tasks are to be retrieved
     * @return a list of tasks matching the specified status and user email
     */
    List<Task> findByStatusAndUserEmail(String status, String email);

    /**
     * Retrieves an optional task by its ID and the associated user email.
     *
     * @param id    the ID of the task to be retrieved
     * @param email the email of the user associated with the task
     * @return an optional containing the task if found, or empty if not found
     */
    Optional<Task> findByIdAndUserEmail(Long id, String email);

    /**
     * Retrieves a list of tasks that are due on the specified date and have the specified notification setting
     * for notifications before the due date.
     *
     * @param dueDate                   the due date of the tasks to be retrieved
     * @param notificationBeforeDueDate the notification setting for tasks before the due date
     * @return a list of tasks matching the specified due date and notification setting
     */
    List<Task> findByDueDateAndNotificationBeforeDueDate(LocalDate dueDate, boolean notificationBeforeDueDate);

    /**
     * Retrieves a list of tasks that are due before the specified date and have the specified notification setting
     * for overdue notifications.
     *
     * @param dueDate          the due date before which the tasks should be retrieved
     * @param notificationOverdue the notification setting for overdue tasks
     * @return a list of tasks matching the specified due date and notification setting
     */
    List<Task> findByDueDateBeforeAndNotificationOverdue(LocalDate dueDate, boolean notificationOverdue);

    /**
     * Deletes all tasks associated with the specified user.
     *
     * @param user the user whose tasks are to be deleted
     */
    void deleteByUser(User user);
}
