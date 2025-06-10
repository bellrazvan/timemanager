import { Component, Input, Output, EventEmitter, OnChanges, SimpleChanges } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { TaskService, Task } from '../task.service';
import { NgForOf, NgIf } from '@angular/common';

@Component({
  selector: 'app-task-edit',
  imports: [ReactiveFormsModule, NgIf, NgForOf],
  templateUrl: './task-edit.component.html',
  styleUrl: './task-edit.component.css',
  standalone: true,
})
export class TaskEditComponent implements OnChanges {
  @Input() task: Task | undefined = undefined;
  @Output() close = new EventEmitter<void>();
  @Output() taskEdited = new EventEmitter<Task>();

  taskForm: FormGroup;
  statusOptions = [
    { value: 'TODO', label: 'To Do' },
    { value: 'IN_PROGRESS', label: 'In Progress' },
    { value: 'DONE', label: 'Done' }
  ];
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

  constructor(private fb: FormBuilder, private taskService: TaskService) {
    this.taskForm = this.fb.group({
      title: ['', [Validators.required, Validators.minLength(3)]],
      description: [''],
      status: [null],
      priority: [null],
      category: [null],
      dueDate: [null],
      notificationBeforeDueDate: [false],
      notificationOverdue: [false]
    });
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['task'] && this.task) {
      this.taskForm.patchValue({
        title: this.task.title,
        status: this.task.status,
        description: this.task.description,
        priority: this.task.priority,
        category: this.task.category,
        dueDate: this.task.dueDate ? this.task.dueDate.slice(0, 10) : null,
        notificationBeforeDueDate: this.task.notificationBeforeDueDate,
        notificationOverdue: this.task.notificationOverdue
      });
    }
  }

  onSubmit() {
    if (this.taskForm.invalid || !this.task) return;
    this.loading = true;
    this.error = '';

    const formValue = this.taskForm.value;
    const updatedTask: Task = {
      ...this.task,
      ...formValue,
      dueDate: formValue.dueDate ? this.formatDate(formValue.dueDate) : null
    };

    this.taskService.updateTask(updatedTask).subscribe({
      next: (task) => {
        this.taskEdited.emit(task);
        this.close.emit();
        this.loading = false;
      },
      error: (err) => {
        this.error = err.error?.error || 'Failed to update task.';
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
