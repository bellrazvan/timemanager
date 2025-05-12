package com.time.timemanager.tasks;

import com.time.timemanager.tasks.dtos.TaskCreateRequest;
import com.time.timemanager.tasks.dtos.TaskResponse;
import com.time.timemanager.tasks.dtos.TaskUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {
    private final TaskControllerHelper taskControllerHelper;

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@RequestBody final TaskCreateRequest request, final Authentication auth) {
        return this.taskControllerHelper.createTask(request, auth);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getTasks(final Authentication auth) {
        return this.taskControllerHelper.getTasks(auth);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable final Long id, final Authentication auth) {
        return this.taskControllerHelper.getTaskById(id, auth);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable final Long id, @RequestBody final TaskUpdateRequest request, final Authentication auth) {
        return this.taskControllerHelper.updateTask(id, request, auth);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable final Long id, final Authentication auth) {
        return this.taskControllerHelper.deleteTask(id, auth);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<TaskResponse>> getTasksByStatus(@PathVariable final String status, final Authentication auth) {
        return this.taskControllerHelper.getTasksByStatus(status, auth);
    }
}
