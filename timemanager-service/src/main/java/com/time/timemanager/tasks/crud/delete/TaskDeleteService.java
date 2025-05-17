package com.time.timemanager.tasks.crud.delete;

import com.time.timemanager.tasks.dtos.TaskResponse;
import com.time.timemanager.tasks.dtos.TaskUpdateRequest;

public interface TaskDeleteService {
    /**
     * Deletes a task associated with the specified task ID and email address.
     *
     * @param id the unique identifier of the task to be deleted
     * @param email the email address of the user requesting the task deletion
     */
    void deleteTask(final Long id, final String email);
}
