import { Component, OnInit } from '@angular/core';
import { TaskService, Task } from '../task.service';
import {NgForOf, NgIf} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {TaskCreateComponent} from '../task-create/task-create.component';

@Component({
  selector: 'app-task-list',
  templateUrl: './task-list.component.html',
  imports: [
    NgForOf,
    NgIf,
    FormsModule,
    TaskCreateComponent
  ],
  styleUrl: './task-list.component.css'
})
export class TaskListComponent implements OnInit {
  tasks: Task[] = [];
  loading = false;
  error = '';
  showCreateTaskModal = false;

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
      error: (err) => {
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

  onTaskCreated(task: any) {
    this.fetchTasks();
    this.showCreateTaskModal = false;
  }

  markDone(task: Task) {
    this.taskService.updateTask({ ...task, status: 'DONE' }).subscribe({
      next: (updated) => {
        task.status = updated.status;
      },
      error: () => this.error = 'Failed to update task'
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
