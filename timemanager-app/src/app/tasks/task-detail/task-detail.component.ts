import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Task } from '../task.service';
import {DatePipe, NgIf} from '@angular/common';

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
  @Input() task: Task | undefined = undefined;
  @Output() close = new EventEmitter<void>();
  @Output() edit = new EventEmitter<Task>();

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

  getStatusLabel(status: string | undefined) {
    const found = this.statusOptions.find((opt) => opt.value === status);
    return found ? found.label : status || '-';
  }

  getPriorityLabel(priority: string | undefined) {
    const found = this.priorityOptions.find((opt) => opt.value === priority);
    return found ? found.label : priority || '-';
  }

  getCategoryLabel(category: string | undefined) {
    const found = this.categoryOptions.find((opt) => opt.value === category);
    return found ? found.label : category || '-';
  }
}
