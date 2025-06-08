import { Component } from '@angular/core';
import {AuthService} from '../auth.service';
import {CommonModule} from '@angular/common';
import {ReactiveFormsModule} from '@angular/forms';

@Component({
  selector: 'app-refresh-token',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './refresh-token.component.html',
  styleUrl: './refresh-token.component.css'
})
export class RefreshTokenComponent {
  message = '';
  error = '';
  loading = false;

  constructor(
    private authService: AuthService,
  ) {}

  onRefresh() {
    this.loading = true;
    this.authService.refreshToken().subscribe({
      next: (res: any) => {
        localStorage.setItem('accessToken', res.accessToken);
        this.message = 'Access token refreshed successfully.';
        this.error = '';
        this.loading = false;
      },
      error: (err) => {
        this.error = err.error?.error || 'Token refresh failed.';
        this.message = '';
        this.loading = false;
      }
    });
  }
}
