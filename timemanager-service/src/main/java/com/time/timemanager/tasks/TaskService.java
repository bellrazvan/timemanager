package com.time.timemanager.tasks;

import com.time.timemanager.tasks.dtos.TaskCreateRequest;
import com.time.timemanager.tasks.dtos.TaskListResponse;
import com.time.timemanager.tasks.dtos.TaskResponse;
import com.time.timemanager.tasks.dtos.TaskUpdateRequest;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    TaskResponse createTask(final TaskCreateRequest request, final String email);

    List<TaskListResponse> getTasks(final String email);

    Optional<TaskResponse> getTaskById(final Long id, final String email);

    List<TaskResponse> getTasksByStatus(final String status, final String email);

    TaskResponse updateTask(final Long id, final TaskUpdateRequest request, final String email);

    void deleteTask(final Long id, final String email);
}
