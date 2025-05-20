package com.time.timemanager.tasks.services.read;

import com.time.timemanager.tasks.TaskRepository;
import com.time.timemanager.tasks.dtos.TaskMapper;
import com.time.timemanager.tasks.dtos.TaskResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskReadServiceImpl implements TaskReadService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Override
    public List<TaskResponse> getTasks(final String email) {
        return this.taskRepository.findByUserEmail(email).stream()
                .map(this.taskMapper::toTaskResponse)
                .toList();
    }

    @Override
    public Optional<TaskResponse> getTaskById(final Long id, final String email) {
        return this.taskRepository.findByIdAndUserEmail(id, email)
                .map(this.taskMapper::toTaskResponse);
    }

    @Override
    public List<TaskResponse> getTasksByStatus(final String status, final String email) {
        return this.taskRepository.findByStatusAndUserEmail(status, email).stream()
                .map(this.taskMapper::toTaskResponse)
                .toList();
    }
}
