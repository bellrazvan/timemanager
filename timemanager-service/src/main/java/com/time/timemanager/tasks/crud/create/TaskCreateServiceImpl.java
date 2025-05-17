package com.time.timemanager.tasks.crud.create;

import com.time.timemanager.authentication.User;
import com.time.timemanager.authentication.UserRepository;
import com.time.timemanager.tasks.Task;
import com.time.timemanager.tasks.TaskRepository;
import com.time.timemanager.tasks.dtos.TaskCreateRequest;
import com.time.timemanager.tasks.dtos.TaskMapper;
import com.time.timemanager.tasks.dtos.TaskResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskCreateServiceImpl implements TaskCreateService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;

    @Override
    public TaskResponse createTask(final TaskCreateRequest request, final String email) {
        final User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email: " + email + " not found"));
        final Task task = this.taskMapper.toTask(request);
        task.setUser(user);

        return this.taskMapper.toTaskResponse(this.taskRepository.save(task));
    }
}
