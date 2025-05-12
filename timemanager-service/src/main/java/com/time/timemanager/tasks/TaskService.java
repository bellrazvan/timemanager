package com.time.timemanager.tasks;

import com.time.timemanager.authentication.User;
import com.time.timemanager.authentication.UserRepository;
import com.time.timemanager.mail.EmailService;
import com.time.timemanager.tasks.dtos.TaskCreateRequest;
import com.time.timemanager.tasks.dtos.TaskMapper;
import com.time.timemanager.tasks.dtos.TaskResponse;
import com.time.timemanager.tasks.dtos.TaskUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final EmailService emailService;

    public TaskResponse createTask(final TaskCreateRequest request, final String email) {
        final User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email: " + email + " not found"));
        final Task task = this.taskMapper.toTask(request);
        task.setUser(user);

        return this.taskMapper.toTaskResponse(this.taskRepository.save(task));
    }

    public List<TaskResponse> getTasks(final String email) {
        return this.taskRepository.findByUserEmail(email).stream()
                .map(this.taskMapper::toTaskResponse)
                .toList();
    }

    public Optional<TaskResponse> getTaskById(final Long id, final String email) {
        return this.taskRepository.findByIdAndUserEmail(id, email)
                .map(this.taskMapper::toTaskResponse);
    }

    public TaskResponse updateTask(final Long id, final TaskUpdateRequest request, final String email) {
        final User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email: " + email + " not found"));
        final Task task = this.taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task " + id + " not found"));

        if (!task.getUser().getId().equals(user.getId())) {
            throw new SecurityException("User " + user.getId() + " doesn't have permission to update task " + task.getId());
        }

        this.taskMapper.updateTaskFromRequest(request, task);

        return this.taskMapper.toTaskResponse(this.taskRepository.save(task));
    }

    public void deleteTask(final Long id, final String email) {
        final User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email: " + email + " not found"));
        final Task task = this.taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task " + id + " not found"));

        if (!task.getUser().getId().equals(user.getId())) {
            throw new SecurityException("User " + user.getId() + " doesn't have permission to delete task " + task.getId());
        }

        this.taskRepository.delete(task);
    }

    public List<TaskResponse> getTasksByStatus(final String status, final String email) {
        return this.taskRepository.findByStatusAndUserEmail(status, email).stream()
                .map(this.taskMapper::toTaskResponse)
                .toList();
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