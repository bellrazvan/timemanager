import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Task } from '../task.service';
import {DatePipe, NgIf} from '@angular/common';
import { statusOptions, priorityOptions, categoryOptions} from '../../shared/task-options';

@Component({
  selector: 'app-task-detail',
  imports: [
    DatePipe,
    NgIf
  ],
  templateUrl: './task-detail.component.html',
  styleUrl: './task-detail.component.css',
  standalone: true
})
export class TaskDetailComponent {
  @Input() openedFromCalendar: boolean = false;
  @Input() task: Task | undefined = undefined;
  @Output() close = new EventEmitter<void>();
  @Output() edit = new EventEmitter<Task>();

  getStatusLabel(status: string | undefined) {
    const found = statusOptions.find((opt) => opt.value === status);
    return found ? found.label : status || '-';
  }

  getPriorityLabel(priority: string | undefined) {
    const found = priorityOptions.find((opt) => opt.value === priority);
    return found ? found.label : priority || '-';
  }

  getCategoryLabel(category: string | undefined) {
    const found = categoryOptions.find((opt) => opt.value === category);
    return found ? found.label : category || '-';
  }
}
