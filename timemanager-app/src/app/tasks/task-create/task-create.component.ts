import { Component, EventEmitter, Output } from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {TaskService} from '../task.service';
import {NgForOf, NgIf} from '@angular/common';

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
  priorityOptions = [
    { value: 'LOW', label: 'Low' },
    { value: 'MEDIUM', label: 'Medium' },
    { value: 'HIGH', label: 'High' }
  ];
  categoryOptions = [
    { value: 'WORK', label: 'Work' },
    { value: 'PERSONAL', label: 'Personal' },
    { value: 'HEALTH', label: 'Health' },
    { value: 'FINANCE', label: 'Finance' },
    { value: 'EDUCATION', label: 'Education' },
    { value: 'SHOPPING', label:'Shopping' },
    { value: 'HOME', label: 'Home' },
    { value: 'SOCIAL', label: 'Social' },
    { value: 'TRAVEL', label: 'Travel' },
    { value: 'HOBBY', label: 'Hobby' },
    { value: 'ERRANDS', label: 'Errands' },
    { value: 'MEETINGS', label: 'Meetings' },
    { value: 'GOALS', label: 'Goals' },
    { value: 'PROJECTS', label: 'Projects' },
    { value: 'OTHER', label: 'Others' }
  ];
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
}

