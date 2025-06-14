package com.time.timemanager.tasks.dtos;

import com.time.timemanager.tasks.Priority;
import com.time.timemanager.tasks.Status;

import java.time.LocalDate;


public record TaskListResponse (
        Long id,
        String title,
        Priority priority,
        Status status,
        LocalDate dueDate
)
{}
