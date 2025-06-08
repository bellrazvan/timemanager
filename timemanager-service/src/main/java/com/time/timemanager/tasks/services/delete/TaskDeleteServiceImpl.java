package com.time.timemanager.tasks.services.delete;

import com.time.timemanager.authentication.User;
import com.time.timemanager.authentication.UserRepository;
import com.time.timemanager.tasks.Task;
import com.time.timemanager.tasks.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskDeleteServiceImpl implements TaskDeleteService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Override
    public void deleteTask(final Long id, final String email) {
        final User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email: " + email + " not found."));
        final Task task = this.taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task " + id + " not found."));

        if (!task.getUser().getId().equals(user.getId())) {
            throw new SecurityException("User " + user.getId() + " doesn't have permission to delete task " + task.getId() + ".");
        }

        this.taskRepository.delete(task);
    }
}
