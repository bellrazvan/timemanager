import { Component, EventEmitter, Output } from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {TaskService} from '../task.service';
import {NgForOf, NgIf} from '@angular/common';
import { priorityOptions, categoryOptions } from '../../shared/task-options';

@Component({
  selector: 'app-task-create',
  templateUrl: './task-create.component.html',
  imports: [
    ReactiveFormsModule,
    NgIf,
    NgForOf
  ],
  styleUrl: './task-create.component.css'
})
export class TaskCreateComponent {
  @Output() close = new EventEmitter<void>();
  @Output() taskCreated = new EventEmitter<any>();

  taskForm: FormGroup;
  loading = false;
  error = '';

  constructor(
    private fb: FormBuilder,
    private taskService: TaskService
  ) {
    this.taskForm = this.fb.group({
      title: ['', [Validators.required, Validators.minLength(3)]],
      description: [''],
      priority: [null],
      category: [null],
      dueDate: [null],
      notificationBeforeDueDate: [false],
      notificationOverdue: [false]
    });
  }

  onSubmit() {
    if (this.taskForm.invalid) return;

    this.loading = true;
    this.error = '';

    const formValue = this.taskForm.value;
    const payload = {
      ...formValue,
      dueDate: formValue.dueDate
        ? this.formatDate(formValue.dueDate)
        : null
    };


    this.taskService.addTask(payload).subscribe({
      next: (task) => {
        this.taskCreated.emit(task);
        this.close.emit();
        this.loading = false;
      },
      error: (err) => {
        this.error = err.error?.error || 'Failed to create task.';
        this.loading = false;
      }
    });
  }

  onCancel() {
    this.close.emit();
  }

  private formatDate(date: string | Date): string {
    if (!date) return '';
    if (typeof date === 'string') return date;
    return date.toISOString().slice(0, 10);
  }

  protected readonly priorityOptions = priorityOptions;
  protected readonly categoryOptions = categoryOptions;
}

