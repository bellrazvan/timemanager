package com.time.timemanager.tasks.crud.update;

import com.time.timemanager.tasks.dtos.TaskCreateRequest;
import com.time.timemanager.tasks.dtos.TaskResponse;
import com.time.timemanager.tasks.dtos.TaskUpdateRequest;

public interface TaskUpdateService {
    /**
     * Updates an existing task with the specified ID using the provided update request.
     *
     * @param id the unique identifier of the task to be updated
     * @param request the details of the task update encapsulated in a TaskUpdateRequest object
     * @param email the email address of the user requesting the task update
     * @return a TaskResponse object containing the updated task details
     */
    TaskResponse updateTask(final Long id, final TaskUpdateRequest request, final String email);
}
