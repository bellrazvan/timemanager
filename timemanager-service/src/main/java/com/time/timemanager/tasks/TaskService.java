package com.time.timemanager.tasks;

import com.time.timemanager.authentication.User;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public Task createTask(Task task, User user) {
        if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Task title cannot be empty");
        }
        task.setUser(user);
        task.setStatus("TODO"); // default status
        if (task.getPriority() == null) task.setPriority("MEDIUM"); // default priority
        return taskRepository.save(task);
    }

    public List<Task> getTasks(Long userId) {
        return taskRepository.findByUserId(userId);
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public Task updateTask(Long id, Task updatedTask, User user) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        if (!task.getUser().getId().equals(user.getId())) {
            throw new SecurityException("You can only update your own tasks");
        }

        if (updatedTask.getTitle() != null) task.setTitle(updatedTask.getTitle());
        if (updatedTask.getDescription() != null) task.setDescription(updatedTask.getDescription());
        if (updatedTask.getDueDate() != null) task.setDueDate(updatedTask.getDueDate());
        if (updatedTask.getStatus() != null) task.setStatus(updatedTask.getStatus());
        if (updatedTask.getPriority() != null) task.setPriority(updatedTask.getPriority());
        if (updatedTask.getCategory() != null) task.setCategory(updatedTask.getCategory());

        return taskRepository.save(task);
    }

    public void deleteTask(Long id, User user) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        if (!task.getUser().getId().equals(user.getId())) {
            throw new SecurityException("You can only delete your own tasks");
        }
        taskRepository.delete(task);
    }

    public boolean isOverdue(Task task) {
        return task.getDueDate() != null && task.getDueDate().isBefore(LocalDate.now()) && !"COMPLETED".equals(task.getStatus());
    }

    public List<Task> getTasksByStatus(Long userId, String status) {
        return taskRepository.findByUserIdAndStatus(userId, status);
    }

    public void adjustPriorityBasedOnDueDate(Task task) {
        if (task.getDueDate() != null) {
            long daysUntilDue = LocalDate.now().until(task.getDueDate()).getDays();
            if (daysUntilDue <= 1) task.setPriority("HIGH");
            else if (daysUntilDue <= 3) task.setPriority("MEDIUM");
            else task.setPriority("LOW");
            taskRepository.save(task);
        }
    }

    @Scheduled(fixedRate = 86400000) // runs daily
    public void checkReminders() {
        List<Task> upcomingTasks = taskRepository.findByDueDateBetween(
                LocalDate.now(), LocalDate.now().plusDays(1));
        upcomingTasks.forEach(task -> {
            //TODO add logic for email notifications - JavaMailSender
            System.out.println("Reminder: " + task.getTitle() + " due tomorrow!");
        });
    }
}
