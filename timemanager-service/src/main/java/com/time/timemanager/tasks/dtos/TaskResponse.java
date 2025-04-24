package com.time.timemanager.tasks.dtos;

import com.time.timemanager.tasks.Category;
import com.time.timemanager.tasks.Priority;
import com.time.timemanager.tasks.Status;

import java.time.LocalDate;

public record TaskResponse(
        Long id,
        String title,
        String description,
        Priority priority,
        Status status,
        Category category,
        LocalDate dueDate,
        Boolean notificationBeforeDueDate,
        Boolean notificationOverdue
) {}
