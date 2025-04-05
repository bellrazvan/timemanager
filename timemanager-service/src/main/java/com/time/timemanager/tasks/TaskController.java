package com.time.timemanager.tasks;

import com.time.timemanager.authentication.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task, Authentication auth) {
        User user = (User) auth.getPrincipal();
        Task createdTask = taskService.createTask(task, user);
        return ResponseEntity.ok(createdTask);
    }

    @GetMapping
    public ResponseEntity<List<Task>> getTasks(Authentication auth) {
        User user = (User) auth.getPrincipal();
        List<Task> tasks = taskService.getTasks(user.getId());
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Task task = taskService.getTaskById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        return ResponseEntity.ok(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task, Authentication auth) {
        User user = (User) auth.getPrincipal();
        Task updatedTask = taskService.updateTask(id, task, user);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id, Authentication auth) {
        User user = (User) auth.getPrincipal();
        taskService.deleteTask(id, user);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Task>> getTasksByStatus(@PathVariable String status, Authentication auth) {
        User user = (User) auth.getPrincipal();
        List<Task> tasks = taskService.getTasksByStatus(user.getId(), status.toUpperCase());
        return ResponseEntity.ok(tasks);
    }

    @PutMapping("/bulk")
    public ResponseEntity<List<Task>> bulkUpdateTasks(@RequestBody List<Task> tasks, Authentication auth) {
        User user = (User) auth.getPrincipal();
        List<Task> updatedTasks = tasks.stream()
                .map(task -> taskService.updateTask(task.getId(), task, user))
                .collect(Collectors.toList());
        return ResponseEntity.ok(updatedTasks);
    }
}
