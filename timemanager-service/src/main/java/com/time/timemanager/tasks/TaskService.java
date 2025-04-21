package com.time.timemanager.tasks;

import com.time.timemanager.authentication.User;
import com.time.timemanager.authentication.UserRepository;
import com.time.timemanager.tasks.dtos.TaskCreateRequest;
import com.time.timemanager.tasks.dtos.TaskMapper;
import com.time.timemanager.tasks.dtos.TaskResponse;
import com.time.timemanager.tasks.dtos.TaskUpdateRequest;
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
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private static final Logger LOG = LogManager.getLogger(TaskService.class);

    public TaskResponse createTask(TaskCreateRequest request, String email) {
        final User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email: " + email + " not found"));
        final Task task = this.taskMapper.toTask(request);
        task.setUser(user);

        task.setStatus(Status.TODO); // default status
        if (task.getPriority() == null) {
            task.setPriority(Priority.LOW); // default priority
        }
        if (task.getCategory() == null) {
            task.setCategory(Category.OTHER); //default category
        }

        return this.taskMapper.toTaskResponse(this.taskRepository.save(task));
    }

    public List<TaskResponse> getTasks(String email) {
        return this.taskRepository.findByUserEmail(email).stream()
                .map(this.taskMapper::toTaskResponse)
                .toList();
    }

    public Optional<TaskResponse> getTaskById(Long id, String email) {
        return this.taskRepository.findByIdAndUserEmail(id, email)
                .map(this.taskMapper::toTaskResponse);
    }

    public TaskResponse updateTask(Long id, TaskUpdateRequest request, String email) {
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

    public void deleteTask(Long id, String email) {
        final User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email: " + email + " not found"));
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
                && !Status.DONE.equals(task.getStatus());
    }

    public List<TaskResponse> getTasksByStatus(String status, String email) {
        return this.taskRepository.findByStatusAndUserEmail(status, email).stream()
                .map(this.taskMapper::toTaskResponse)
                .toList();
    }

    @Scheduled(fixedRate = 86400000) // runs daily
    public void checkReminders(String email) {
        final List<TaskResponse> upcomingTasks = this.taskRepository.findByUserEmailAndDueDateBetween(
                email, LocalDate.now(), LocalDate.now().plusDays(1))
                .stream()
                .map(this.taskMapper::toTaskResponse)
                .toList();

        upcomingTasks.forEach(task -> {
            //TODO add logic for email notifications - JavaMailSender
            LOG.warn("Reminder: " + task.title() + " due tomorrow!");
        });
    }
}
