package com.time.timemanager.tasks.services.update;

import com.time.timemanager.authentication.User;
import com.time.timemanager.authentication.UserRepository;
import com.time.timemanager.tasks.Task;
import com.time.timemanager.tasks.TaskRepository;
import com.time.timemanager.tasks.dtos.TaskMapper;
import com.time.timemanager.tasks.dtos.TaskResponse;
import com.time.timemanager.tasks.dtos.TaskUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class TaskUpdateServiceImpl implements TaskUpdateService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;

    @Override
    public TaskResponse updateTask(final Long id, final TaskUpdateRequest request, final String email) {
        final User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email: " + email + " not found."));
        final Task task = this.taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task " + id + " not found."));

        if (!task.getUser().getId().equals(user.getId())) {
            throw new SecurityException("User " + user.getId() + " doesn't have permission to update task " + task.getId() + ".");
        }

        this.taskMapper.updateTaskFromRequest(request, task);

        return this.taskMapper.toTaskResponse(this.taskRepository.save(task));
    }
}
