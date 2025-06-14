package com.time.timemanager.tasks;

import com.time.timemanager.tasks.dtos.TaskListResponse;
import com.time.timemanager.tasks.services.create.TaskCreateService;
import com.time.timemanager.tasks.services.delete.TaskDeleteService;
import com.time.timemanager.tasks.dtos.TaskCreateRequest;
import com.time.timemanager.tasks.dtos.TaskResponse;
import com.time.timemanager.tasks.dtos.TaskUpdateRequest;
import com.time.timemanager.tasks.services.read.TaskReadService;
import com.time.timemanager.tasks.services.update.TaskUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskCreateService taskCreateService;
    private final TaskUpdateService taskUpdateService;
    private final TaskDeleteService taskDeleteService;
    private final TaskReadService taskReadService;

    @Override
    public TaskResponse createTask(final TaskCreateRequest request, final String email) {
        return this.taskCreateService.createTask(request, email);
    }

    @Override
    public List<TaskListResponse> getTasks(final String email) {
        return this.taskReadService.getTasks(email);
    }

    @Override
    public Optional<TaskResponse> getTaskById(final Long id, final String email) {
        return this.taskReadService.getTaskById(id, email);
    }

    @Override
    public List<TaskResponse> getTasksByStatus(final String status, final String email) {
        return this.taskReadService.getTasksByStatus(status, email);
    }

    @Override
    public TaskResponse updateTask(final Long id, final TaskUpdateRequest request, final String email) {
        return this.taskUpdateService.updateTask(id, request, email);
    }

    @Override
    public void deleteTask(final Long id, final String email) {
        this.taskDeleteService.deleteTask(id, email);
    }
}