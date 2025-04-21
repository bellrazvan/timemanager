package com.time.timemanager.tasks.dtos;

import com.time.timemanager.tasks.Status;
import com.time.timemanager.tasks.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public Task toTask(TaskCreateRequest request) {
        final Task task = new Task();
        task.setId(null);
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setPriority(request.priority());
        task.setCategory(request.category());
        task.setStatus(Status.TODO);
        task.setDueDate(request.dueDate());
        return task;
    }

    public TaskResponse toTaskResponse(Task task) {
        final String email = task.getUser() != null ? task.getUser().getEmail() : null;

        return new TaskResponse(
                task.getId(),
                email,
                task.getTitle(),
                task.getDescription(),
                task.getPriority(),
                task.getStatus(),
                task.getCategory(),
                task.getDueDate()
        );
    }

    public void updateTaskFromRequest(TaskUpdateRequest request, Task task) {
        task.setTitle(request.title() != null ? request.title() : task.getTitle());
        task.setDescription(request.description() != null ? request.description() : task.getDescription());
        task.setPriority(request.priority() != null ? request.priority() : task.getPriority());
        task.setCategory(request.category() != null ? request.category() : task.getCategory());
        task.setStatus(request.status() != null ? request.status() : task.getStatus());
        task.setDueDate(request.dueDate() != null ? request.dueDate() : task.getDueDate());
    }
}