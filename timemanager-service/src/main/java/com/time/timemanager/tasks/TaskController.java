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
    public ResponseEntity<TaskResponse> createTask(@RequestBody TaskCreateRequest request, Authentication auth) {
        return this.taskControllerHelper.createTask(request, auth);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getTasks(Authentication auth) {
        return this.taskControllerHelper.getTasks(auth);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id, Authentication auth) {
        return this.taskControllerHelper.getTaskById(id, auth);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id, @RequestBody TaskUpdateRequest request, Authentication auth) {
        return this.taskControllerHelper.updateTask(id, request, auth);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id, Authentication auth) {
        return this.taskControllerHelper.deleteTask(id, auth);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<TaskResponse>> getTasksByStatus(@PathVariable String status, Authentication auth) {
        return this.taskControllerHelper.getTasksByStatus(status, auth);
    }

// USED FOR TESTING REMINDERS
//    @GetMapping("/trigger-reminders")
//    public void testReminders() {
//        taskService.checkReminders();
//    }
//
//    @GetMapping("/trigger-overdue")
//    public void testOverdue() {
//        taskService.checkOverdueTasks();
//    }
}
