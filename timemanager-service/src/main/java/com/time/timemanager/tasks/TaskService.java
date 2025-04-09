package com.time.timemanager.tasks;

import com.time.timemanager.authentication.User;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private static final String LOW = "LOW";
    private static final String MEDIUM = "MEDIUM";
    private static final String HIGH = "HIGH";
    private static final int HIGH_PRIORITY_THRESHOLD = 1;
    private static final int MEDIUM_PRIORITY_THRESHOLD = 3;

    public Task createTask(Task task, User user) {
        if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Task title cannot be empty");
        }
        task.setUser(user);
        task.setStatus("TODO"); // default status
        if (task.getPriority() == null)
            task.setPriority(MEDIUM); // default priority
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

        if (updatedTask.getTitle() != null)
            task.setTitle(updatedTask.getTitle());
        if (updatedTask.getDescription() != null)
            task.setDescription(updatedTask.getDescription());
        if (updatedTask.getDueDate() != null)
            task.setDueDate(updatedTask.getDueDate());
        if (updatedTask.getStatus() != null)
            task.setStatus(updatedTask.getStatus());
        if (updatedTask.getPriority() != null)
            task.setPriority(updatedTask.getPriority());
        if (updatedTask.getCategory() != null)
            task.setCategory(updatedTask.getCategory());

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
        return task.getDueDate() != null && task.getDueDate().isBefore(LocalDate.now()) && !"COMPLETED".equals(task.getStatus());
    }

    public List<Task> getTasksByStatus(Long userId, String status) {
        return this.taskRepository.findByUserIdAndStatus(userId, status);
    }

    public void adjustPriorityBasedOnDueDate(Task task) {
        if (task.getDueDate() != null) {
            long daysUntilDue = LocalDate.now().until(task.getDueDate()).getDays();

            if (daysUntilDue <= HIGH_PRIORITY_THRESHOLD)
                task.setPriority(HIGH);
            else if (daysUntilDue <= MEDIUM_PRIORITY_THRESHOLD)
                task.setPriority(MEDIUM);
            else
                task.setPriority(LOW);
            this.taskRepository.save(task);
        }
    }

    @Scheduled(fixedRate = 86400000) // runs daily
    public void checkReminders() {
        final List<Task> upcomingTasks = this.taskRepository.findByDueDateBetween(
                LocalDate.now(), LocalDate.now().plusDays(1));
        upcomingTasks.forEach(task -> {
            //TODO add logic for email notifications - JavaMailSender
            System.out.println("Reminder: " + task.getTitle() + " due tomorrow!");
        });
    }
}
