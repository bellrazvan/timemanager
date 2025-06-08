package com.time.timemanager.authentication.dtos;

public record UserDetailsResponse (
        String username,

        String email,

        long totalTasks,

        long totalDoneTasks,

        long totalInProgressTasks,

        long totalToDoTasks
) {}
