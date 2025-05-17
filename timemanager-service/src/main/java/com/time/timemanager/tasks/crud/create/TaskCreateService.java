package com.time.timemanager.tasks.crud.create;

import com.time.timemanager.tasks.dtos.TaskCreateRequest;
import com.time.timemanager.tasks.dtos.TaskResponse;

import java.util.List;
import java.util.Optional;

public interface TaskCreateService {
    /**
     * Creates a new task based on the provided request and associated with the specified email.
     *
     * @param request the details of the task to be created, encapsulated in a TaskCreateRequest object
     * @param email the email address of the user creating the task
     * @return a TaskResponse object containing the details of the created task
     */
    TaskResponse createTask(final TaskCreateRequest request, final String email);
}
