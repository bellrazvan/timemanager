package com.time.timemanager.tasks.dtos;

import com.time.timemanager.tasks.Category;
import com.time.timemanager.tasks.Priority;
import com.time.timemanager.tasks.Status;
import com.time.timemanager.tasks.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public Task toTask(TaskCreateRequest request) {
        return Task.builder()
                .id(null)
                .title(request.title())
                .description(request.description())
                .priority(request.priority() != null ? request.priority() : Priority.LOW)
                .category(request.category() != null ? request.category() : Category.OTHER)
                .status(Status.TODO)
                .dueDate(request.dueDate())
                .notificationBeforeDueDate(request.notificationBeforeDueDate() != null ? request.notificationBeforeDueDate() : false)
                .notificationOverdue(request.notificationOverdue() != null ? request.notificationOverdue() : false)
                .build();
    }

    public TaskResponse toTaskResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getPriority(),
                task.getStatus(),
                task.getCategory(),
                task.getDueDate(),
                task.getNotificationBeforeDueDate(),
                task.getNotificationOverdue()
        );
    }

    public void updateTaskFromRequest(TaskUpdateRequest request, Task task) {
        task.setTitle(request.title() != null ? request.title() : task.getTitle());
        task.setDescription(request.description() != null ? request.description() : task.getDescription());
        task.setPriority(request.priority() != null ? request.priority() : task.getPriority());
        task.setCategory(request.category() != null ? request.category() : task.getCategory());
        task.setStatus(request.status() != null ? request.status() : task.getStatus());
        task.setDueDate(request.dueDate() != null ? request.dueDate() : task.getDueDate());
        task.setNotificationBeforeDueDate(request.notificationBeforeDueDate() != null ? request.notificationBeforeDueDate() : task.getNotificationBeforeDueDate());
        task.setNotificationOverdue(request.notificationOverdue() != null ? request.notificationOverdue() : task.getNotificationOverdue());
    }
}