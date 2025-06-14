package com.time.timemanager.tasks.services.read;

import com.time.timemanager.tasks.dtos.TaskListResponse;
import com.time.timemanager.tasks.dtos.TaskResponse;

import java.util.List;
import java.util.Optional;

public interface TaskReadService {
    /**
     * Retrieves a list of tasks associated with the specified email address.
     *
     * @param email the email address of the user whose tasks are to be retrieved
     * @return a List of TaskResponse objects representing the tasks associated with the given email
     */
    List<TaskListResponse> getTasks(final String email);

    /**
     * Retrieves a task associated with the specified task ID and email address.
     *
     * @param id the unique identifier of the task to be retrieved
     * @param email the email address of the user requesting the task
     * @return an Optional containing the TaskResponse object if the task is found, or an empty Optional if not
     */
    Optional<TaskResponse> getTaskById(final Long id, final String email);

    /**
     * Retrieves a list of tasks associated with the specified status and email address.
     *
     * @param status the status of the tasks to be retrieved (e.g., "completed", "pending")
     * @param email the email address of the user whose tasks are to be retrieved
     * @return a List of TaskResponse objects representing the tasks with the specified status
     */
    List<TaskResponse> getTasksByStatus(final String status, final String email);
}
