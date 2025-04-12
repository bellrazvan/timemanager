package com.time.timemanager.tasks;

import com.time.timemanager.authentication.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task, Authentication auth) {
        final String email = ((UserDetails) auth.getPrincipal()).getUsername(); //TODO make TaskController more compact + change TaskService implementation
        final Task createdTask = this.taskService.createTask(task, email);

        return ResponseEntity.ok(createdTask);
    }

    @GetMapping
    public ResponseEntity<List<Task>> getTasks(Authentication auth) {
        final User user = (User) auth.getPrincipal();
        final List<Task> tasks = this.taskService.getTasks(user.getId());

        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        final Task task = this.taskService.getTaskById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task " + id + " not found"));

        return ResponseEntity.ok(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task, Authentication auth) {
        final User user = (User) auth.getPrincipal();
        final Task updatedTask = this.taskService.updateTask(id, task, user);

        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id, Authentication auth) {
        final User user = (User) auth.getPrincipal();
        this.taskService.deleteTask(id, user);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Task>> getTasksByStatus(@PathVariable String status, Authentication auth) {
        final User user = (User) auth.getPrincipal();
        final List<Task> tasks = this.taskService.getTasksByStatus(user.getId(), status.toUpperCase(Locale.ROOT));
        return ResponseEntity.ok(tasks);
    }

    @PutMapping("/bulk")
    public ResponseEntity<List<Task>> bulkUpdateTasks(@RequestBody List<Task> tasks, Authentication auth) {
        final User user = (User) auth.getPrincipal();
        final List<Task> updatedTasks = tasks.stream()
                .map(task -> this.taskService.updateTask(task.getId(), task, user))
                .collect(Collectors.toList());
        return ResponseEntity.ok(updatedTasks);
    }
}
