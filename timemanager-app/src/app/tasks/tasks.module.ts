import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TaskListComponent } from './task-list/task-list.component';
import {provideHttpClient} from '@angular/common/http';

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    TaskListComponent,
  ],
  providers: [
    provideHttpClient()
  ],
  exports: [TaskListComponent]
})
export class TasksModule { }

