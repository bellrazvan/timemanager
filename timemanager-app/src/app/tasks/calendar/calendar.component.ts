import {Component, OnInit} from '@angular/core';
import { Task, TaskService } from '../task.service';
import {NgForOf, NgIf, NgStyle} from '@angular/common';
import {TaskDetailComponent} from '../task-detail/task-detail.component';

@Component({
  selector: 'app-calendar',
  imports: [
    NgIf,
    NgForOf,
    NgStyle,
    TaskDetailComponent,
  ],
  templateUrl: './calendar.component.html',
  styleUrl: './calendar.component.css'
})
export class CalendarComponent implements OnInit {
  tasks: Task[] = [];
  loading = false;
  error = '';
  currentMonth: number = new Date().getMonth();
  currentYear: number = new Date().getFullYear();
  calendarDays: {
    date: Date;
    tasks: Task[];
    isOtherMonth: boolean
  }[] = [];
  monthNames = [
    'January', 'February', 'March', 'April', 'May', 'June',
    'July', 'August', 'September', 'October', 'November', 'December'
  ];
  showTaskDetailModal = false;
  selectedTask: Task | undefined = undefined;
  openedFromCalendar = false;

  constructor(private taskService: TaskService) {}

  ngOnInit(): void {
    const now = new Date();
    this.currentMonth = now.getMonth();
    this.currentYear = now.getFullYear();
    this.fetchTasks();
  }

  fetchTasks() {
    this.loading = true;
    this.error = '';
    this.taskService.getTasks().subscribe({
      next: (tasks) => {
        this.tasks = tasks;
        this.generateCalendar();
        this.loading = false;
      },
      error: () => {
        this.error = 'Failed to load tasks';
        this.loading = false;
      }
    });
  }

  generateCalendar() {
    const year = this.currentYear;
    const month = this.currentMonth;

    const firstDay = new Date(year, month, 1);
    const lastDay = new Date(year, month + 1, 0);

    const firstDayOfWeek = firstDay.getDay();
    const lastDayOfWeek = lastDay.getDay();

    this.calendarDays = [];

    if (firstDayOfWeek > 0) {
      const prevMonth = month === 0 ? 11 : month - 1;
      const prevYear = month === 0 ? year - 1 : year;
      const prevMonthLastDay = new Date(prevYear, prevMonth + 1, 0).getDate();
      for (let i = firstDayOfWeek - 1; i >= 0; i--) {
        const date = new Date(prevYear, prevMonth, prevMonthLastDay - i);
        this.calendarDays.push({ date, tasks: [], isOtherMonth: true });
      }
    }

    for (let day = 1; day <= lastDay.getDate(); day++) {
      const date = new Date(year, month, day);
      const dayTasks = this.tasks.filter(task =>
        new Date(task.dueDate).toDateString() === date.toDateString()
      );
      this.calendarDays.push({ date, tasks: dayTasks, isOtherMonth: false });
    }

    if (lastDayOfWeek < 6) {
      const nextMonth = month === 11 ? 0 : month + 1;
      const nextYear = month === 11 ? year + 1 : year;
      for (let i = 1; i <= 6 - lastDayOfWeek; i++) {
        const date = new Date(nextYear, nextMonth, i);
        this.calendarDays.push({ date, tasks: [], isOtherMonth: true });
      }
    }
  }

  prevMonth() {
    if (this.currentMonth === 0) {
      this.currentMonth = 11;
      this.currentYear--;
    } else {
      this.currentMonth--;
    }
    this.generateCalendar();
  }

  nextMonth() {
    if (this.currentMonth === 11) {
      this.currentMonth = 0;
      this.currentYear++;
    } else {
      this.currentMonth++;
    }
    this.generateCalendar();
  }

  getCalendarWeeks() {
    const weeks = [];
    let week: ({
        isOtherMonth: any; date: Date; tasks: Task[];
    } | null)[] = [];
    this.calendarDays.forEach(day => {
      week.push(day);
      if (week.length === 7) {
        weeks.push(week);
        week = [];
      }
    });
    if (week.length > 0) {
      while (week.length < 7) week.push(null);
      weeks.push(week);
    }
    return weeks;
  }

  getTaskDotStyles(task: Task): {[key: string]: string} {
    let fill = '#f5f5f5';
    switch (task.priority?.toUpperCase()) {
      case 'HIGH': fill = '#e53935'; break;
      case 'MEDIUM': fill = '#fb8c00'; break;
      case 'LOW': fill = '#43a047'; break;
    }

    let border = '#222';
    const status = task.status?.toUpperCase();
    if (status === 'IN_PROGRESS') border = '#1976d2';
    else if (status === 'DONE') border = '#616161';

    const dueDate = new Date(task.dueDate);
    const now = new Date();
    if ((status === 'TODO' || status === 'IN_PROGRESS') && dueDate < now) {
      border = '#e53935';
    }

    return {
      'background-color': fill,
      'border': `3px solid ${border}`,
      'box-shadow': `0 0 2px 1px ${border}33`
    };
  }

  isToday(date?: Date): boolean {
    if (!date) return false;
    const today = new Date();
    return (
      date.getDate() === today.getDate() &&
      date.getMonth() === today.getMonth() &&
      date.getFullYear() === today.getFullYear()
    );
  }

  openTaskDetail(task: Task) {
    this.taskService.getTaskById(task.id).subscribe({
      next: (fetchedTask) => {
        this.selectedTask = fetchedTask;
        this.showTaskDetailModal = true;
        this.openedFromCalendar = true;
      },
      error: () => {
      }
    });
  }

  closeTaskDetail() {
    this.showTaskDetailModal = false;
    this.selectedTask = undefined;
    this.openedFromCalendar = false;
  }
}
