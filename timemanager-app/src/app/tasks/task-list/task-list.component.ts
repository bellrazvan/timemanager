import { Component, OnInit } from '@angular/core';
import { TaskService, Task } from '../task.service';
import {NgForOf, NgIf} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {TaskCreateComponent} from '../task-create/task-create.component';
import {TaskDetailComponent} from '../task-detail/task-detail.component';
import {TaskEditComponent} from '../task-edit/task-edit.component';

@Component({
  selector: 'app-task-list',
  templateUrl: './task-list.component.html',
  imports: [
    NgForOf,
    NgIf,
    FormsModule,
    TaskCreateComponent,
    TaskDetailComponent,
    TaskEditComponent
  ],
  styleUrl: './task-list.component.css'
})
export class TaskListComponent implements OnInit {
  tasks: Task[] = [];
  loading = false;
  error = '';
  showCreateTaskModal = false;
  showTaskDetailModal = false;
  selectedTask: Task | undefined = undefined;
  showEditTaskModal = false;
  taskToEdit: Task | undefined = undefined;
  statusOptions = [
    { value: 'TODO', label: 'To Do' },
    { value: 'IN_PROGRESS', label: 'In Progress' },
    { value: 'DONE', label: 'Done' }
  ];

  constructor(private taskService: TaskService) {}

  ngOnInit(): void {
    this.fetchTasks();
  }

  fetchTasks() {
    this.loading = true;
    this.error = '';
    this.taskService.getTasks().subscribe({
      next: (tasks) => {
        this.tasks = tasks;
        this.loading = false;
      },
      error: () => {
        this.error = 'Failed to load tasks';
        this.loading = false;
      }
    });
  }

  openCreateTask() {
    this.showCreateTaskModal = true;
  }

  closeCreateTask() {
    this.showCreateTaskModal = false;
  }

  onTaskCreated() {
    this.fetchTasks();
    this.showCreateTaskModal = false;
  }

  openTaskDetail(task: Task) {
    this.loading = true;
    this.taskService.getTaskById(task.id).subscribe({
      next: (fetchedTask) => {
        this.selectedTask = fetchedTask;
        this.showTaskDetailModal = true;
        this.loading = false;
      },
      error: () => {
        this.error = 'Failed to load task details';
        this.loading = false;
      }
    });
  }

  closeTaskDetail() {
    this.showTaskDetailModal = false;
    this.selectedTask = undefined;
  }

  openEditTask(task: Task) {
    this.taskToEdit = { ...task };
    this.showEditTaskModal = true;
    this.showTaskDetailModal = false;
  }

  onTaskEdited(updatedTask: Task) {
    this.fetchTasks();
    this.closeEditTask();
    this.selectedTask = updatedTask;
    this.showTaskDetailModal = true;
  }

  closeEditTask() {
    this.showEditTaskModal = false;
    this.taskToEdit = undefined;
    this.showTaskDetailModal = true;
  }

  onStatusChange(task: Task, event: Event) {
    const newStatus = (event.target as HTMLSelectElement).value;
    if (task.status === newStatus) return;
    const updatedTask = { ...task, status: newStatus };
    this.taskService.updateTask(updatedTask).subscribe({
      next: (updated) => {
        task.status = updated.status;
      },
      error: () => {
        alert('Failed to update status');
      }
    });
  }

  deleteTask(task: Task) {
    this.taskService.deleteTask(task.id).subscribe({
      next: () => {
        this.tasks = this.tasks.filter(t => t.id !== task.id);
      },
      error: () => this.error = 'Failed to delete task'
    });
  }
}
