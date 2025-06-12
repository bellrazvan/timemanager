import { Component, OnInit } from '@angular/core';
import { TaskService, Task } from '../task.service';
import {NgForOf, NgIf} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {TaskCreateComponent} from '../task-create/task-create.component';
import {TaskDetailComponent} from '../task-detail/task-detail.component';
import {TaskEditComponent} from '../task-edit/task-edit.component';
import { statusOptions } from '../../shared/task-options';

const PAGE_SIZE = 11;
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
  pagedTasks: Task[] = [];
  filteredTasks: Task[] = [];
  loading = false;
  error = '';
  showCreateTaskModal = false;
  showTaskDetailModal = false;
  selectedTask: Task | undefined = undefined;
  showEditTaskModal = false;
  taskToEdit: Task | undefined = undefined;
  statusFilter: string = '';


  page = 1;
  pageSize = PAGE_SIZE;
  totalPages = 1;

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
        this.applyFilter();
        this.totalPages = Math.ceil(this.tasks.length / this.pageSize) || 1;
        this.page = 1;
        this.updatePagedTasks();
        this.loading = false;
      },
      error: () => {
        this.error = 'Failed to load tasks';
        this.loading = false;
      }
    });
  }

  applyFilter() {
    this.filteredTasks = this.statusFilter
      ? this.tasks.filter(task => task.status === this.statusFilter)
      : this.tasks.slice();
    this.totalPages = Math.ceil(this.filteredTasks.length / this.pageSize) || 1;
    this.page = 1;
    this.updatePagedTasks();
  }

  onStatusFilterChange() {
    this.applyFilter();
  }

  updatePagedTasks() {
    const start = (this.page - 1) * this.pageSize;
    const end = start + this.pageSize;
    this.pagedTasks = this.filteredTasks.slice(start, end);
  }

  goToPage(page: number) {
    if (page < 1 || page > this.totalPages) return;
    this.page = page;
    this.updatePagedTasks();
  }

  nextPage() {
    this.goToPage(this.page + 1);
  }

  prevPage() {
    this.goToPage(this.page - 1);
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

  openTaskDetail(task: Task) {
    this.taskService.getTaskById(task.id).subscribe({
      next: (fetchedTask) => {
        this.selectedTask = fetchedTask;
        this.showTaskDetailModal = true;
      },
      error: () => {
        this.error = 'Failed to load task details';
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
        this.totalPages = Math.ceil(this.tasks.length / this.pageSize) || 1;
        if (this.page > this.totalPages) this.page = this.totalPages;
        this.updatePagedTasks();
      },
      error: () => this.error = 'Failed to delete task'
    });
  }

  protected readonly statusOptions = statusOptions;
}
