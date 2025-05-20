package com.time.timemanager.mail;

import com.time.timemanager.authentication.User;
import com.time.timemanager.authentication.UserRepository;
import com.time.timemanager.authentication.UserStatus;
import com.time.timemanager.tasks.Status;
import com.time.timemanager.tasks.Task;
import com.time.timemanager.tasks.TaskRepository;
import com.time.timemanager.tasks.dtos.TaskMapper;
import com.time.timemanager.tasks.dtos.TaskResponse;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduledTasksServiceImpl implements ScheduledTasksService {
    private static final Logger LOG = LogManager.getLogger(ScheduledTasksServiceImpl.class);
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final TaskMapper taskMapper;

    @Scheduled(cron = "0 0 8 * * *")
    public void deleteInactiveUsers() {
        final List<User> inactiveUsers = this.getInactiveUsersForOverThirtyDays();

        inactiveUsers.forEach(user -> {
            this.taskRepository.deleteByUser(user);
            this.userRepository.delete(user);
            LOG.info("Deleted inactive user: {}", user.getEmail());
        });

        LOG.info("Completed inactive user cleanup. Removed {} accounts.", inactiveUsers.size());
    }

    private List<User> getInactiveUsersForOverThirtyDays() {
        final LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);
        return this.userRepository.findByStatusAndInactiveSinceBefore(UserStatus.INACTIVE, thirtyDaysAgo);
    }

    @Scheduled(cron = "0 0 8 * * *")
    public void checkReminders() {
        final LocalDate reminderDate = LocalDate.now().plusDays(1);
        final List<Task> tasks = this.taskRepository.findByDueDateAndNotificationBeforeDueDate(
                reminderDate,
                true
        );
        this.processTasks(tasks, false);
    }

    @Scheduled(cron = "0 0 8 * * *")
    public void checkOverdueTasks() {
        final List<Task> tasks = this.taskRepository.findByDueDateBeforeAndNotificationOverdue(
                LocalDate.now(),
                true
        );
        this.processTasks(tasks, true);
    }

    private void processTasks(final List<Task> tasks, final boolean isOverdue) {
        tasks.forEach(task -> {
            final String email = task.getUser().getEmail();
            final String name = task.getUser().getUsername();
            final TaskResponse response = this.taskMapper.toTaskResponse(task);

            if (isOverdue && this.isNotDone(task)) {
                this.emailService.sendReminderOverdue(email, name, response);
            } else if (!isOverdue && this.isNotDone(task)) {
                this.emailService.sendReminderDueTomorrow(email, name, response);
            }
        });
    }

    private boolean isNotDone(final Task task) {
        return !task.getStatus().equals(Status.DONE);
    }
}
