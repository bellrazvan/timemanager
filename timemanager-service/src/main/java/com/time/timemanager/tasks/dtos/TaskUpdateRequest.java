package com.time.timemanager.tasks.dtos;

import com.time.timemanager.tasks.Category;
import com.time.timemanager.tasks.Priority;
import com.time.timemanager.tasks.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record TaskUpdateRequest(
        @NotBlank(message = "Title is required")
        @Size(min = 3, message = "Title must be at least 3 characters long")
        String title,
        String description,
        Priority priority,
        Status status,
        Category category,
        LocalDate dueDate,
        Boolean notificationBeforeDueDate,
        Boolean notificationOverdue
) {}

