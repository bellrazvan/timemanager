package com.time.timemanager.mail;

public interface ScheduledTasksService {
    /**
     * Deletes users who have been inactive for a specified period.
     */
    public void deleteInactiveUsers();

    /**
     * Checks and processes reminders for users.
     */
    public void checkReminders();

    /**
     * Checks for tasks that are overdue and processes them accordingly.
     */
    public void checkOverdueTasks();
}
