package com.time.timemanager.tasks;

import com.time.timemanager.config.ApiResponseMapper;
import com.time.timemanager.tasks.dtos.TaskCreateRequest;
import com.time.timemanager.tasks.dtos.TaskResponse;
import com.time.timemanager.tasks.dtos.TaskUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TaskControllerHelper {
    private final TaskService taskService;

    public ResponseEntity<TaskResponse> createTask(TaskCreateRequest request, Authentication auth) {
        final String email = auth.getName();
        final TaskResponse createdTask = this.taskService.createTask(request, email);

        return ResponseEntity.ok(createdTask);
    }

    public ResponseEntity<List<TaskResponse>> getTasks(Authentication auth) {
        final String email = auth.getName();
        final List<TaskResponse> tasks = this.taskService.getTasks(email);

        return ResponseEntity.ok(tasks);
    }

    public ResponseEntity<TaskResponse> getTaskById(Long id, Authentication auth) {
        final String email = auth.getName();
        final TaskResponse task = this.taskService.getTaskById(id, email)
                .orElseThrow(() -> new IllegalArgumentException("Task " + id + " not found"));

        return ResponseEntity.ok(task);
    }

    public ResponseEntity<TaskResponse> updateTask(Long id, TaskUpdateRequest request, Authentication auth) {
        final String email = auth.getName();
        final TaskResponse updatedTask = this.taskService.updateTask(id, request, email);

        return ResponseEntity.ok(updatedTask);
    }

    public ResponseEntity<Map<String, String>> deleteTask(Long id, Authentication auth) {
        final String email = auth.getName();
        this.taskService.deleteTask(id, email);

        return ResponseEntity.ok().body(ApiResponseMapper.successfulResponse("Deleted task successfully"));
    }

    public ResponseEntity<List<TaskResponse>> getTasksByStatus(String status, Authentication auth) {
        final String email = auth.getName();
        final List<TaskResponse> tasks = this.taskService.getTasksByStatus(status.toUpperCase(Locale.ROOT), email);

        return ResponseEntity.ok(tasks);
    }
}
