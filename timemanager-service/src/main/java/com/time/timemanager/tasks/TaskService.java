package com.time.timemanager.tasks;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.time.timemanager.authentication.User;
import com.time.timemanager.authentication.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private static final Logger LOG = LogManager.getLogger(TaskService.class);

    public Task createTask(Task task, String username) {
        final User user = this.userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Task title cannot be empty");
        }
        task.setUser(user);
        task.setStatus("TODO"); // default status
        if (task.getPriority() == null) {
            task.setPriority("LOW"); // default priority
        }
        return this.taskRepository.save(task);
    }

    public List<Task> getTasks(Long userId) {
        return this.taskRepository.findByUserId(userId);
    }

    public Optional<Task> getTaskById(Long id) {
        return this.taskRepository.findById(id);
    }

    public Task updateTask(Long id, Task updatedTask, User user) {
        final Task task = this.taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task " + id + " not found"));

        if (!task.getUser().getId().equals(user.getId())) {
            throw new SecurityException("User " + user.getId() + " doesn't have permission to update task " + task.getId());
        }

        try {
            this.objectMapper.updateValue(task, updatedTask);
        } catch (JsonMappingException e) {
            LOG.error("Error updating task " + id + ": " + e.getMessage());
        }
        return this.taskRepository.save(task);
    }

    public void deleteTask(Long id, User user) {
        final Task task = this.taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task " + id + " not found"));
        if (!task.getUser().getId().equals(user.getId())) {
            throw new SecurityException("User " + user.getId() + " doesn't have permission to delete task " + task.getId());
        }
        this.taskRepository.delete(task);
    }

    public boolean isOverdue(Task task) {
        return task.getDueDate() != null
                && task.getDueDate().isBefore(LocalDate.now())
                && !"COMPLETED".equals(task.getStatus());
    }

    public List<Task> getTasksByStatus(Long userId, String status) {
        return this.taskRepository.findByUserIdAndStatus(userId, status);
    }

    @Scheduled(fixedRate = 86400000) // runs daily
    public void checkReminders() {
        final List<Task> upcomingTasks = this.taskRepository.findByDueDateBetween(
                LocalDate.now(), LocalDate.now().plusDays(1));
        upcomingTasks.forEach(task -> {
            //TODO add logic for email notifications - JavaMailSender
            LOG.warn("Reminder: " + task.getTitle() + " due tomorrow!");
        });
    }
}
