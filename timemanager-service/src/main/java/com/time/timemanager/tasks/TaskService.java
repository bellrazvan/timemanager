package com.time.timemanager.tasks;

import com.time.timemanager.tasks.dtos.TaskCreateRequest;
import com.time.timemanager.tasks.dtos.TaskListResponse;
import com.time.timemanager.tasks.dtos.TaskResponse;
import com.time.timemanager.tasks.dtos.TaskUpdateRequest;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    @CacheEvict(value = "userDetails", key = "#auth.name")
    TaskResponse createTask(final TaskCreateRequest request, final String email);

    @CacheEvict(value = "userDetails", key = "#auth.name")
    List<TaskListResponse> getTasks(final String email);

    @Cacheable("tasks")
    Optional<TaskResponse> getTaskById(final Long id, final String email);

    @CacheEvict(value = "userDetails", key = "#auth.name")

    List<TaskResponse> getTasksByStatus(final String status, final String email);

    @CacheEvict(value = "userDetails", key = "#auth.name")
    TaskResponse updateTask(final Long id, final TaskUpdateRequest request, final String email);

    @CacheEvict(value = "userDetails", key = "#auth.name")
    void deleteTask(final Long id, final String email);
}
