import { Component } from '@angular/core';
import {AuthService} from '../auth.service';
import {Router} from '@angular/router';
import {CommonModule} from '@angular/common';
import {ReactiveFormsModule} from '@angular/forms';

@Component({
  selector: 'app-delete-account',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './delete-account.component.html',
  styleUrl: './delete-account.component.css'
})
export class DeleteAccountComponent {
  message: string = '';
  error: string = '';
  loading: boolean = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  onDeleteAccount(): void {
    this.loading = true;
    this.authService.deleteAccount().subscribe({
      next: (res: any) => {
        this.message = res.message || 'Account deactivated successfully. You have 30 days until your account is deleted.';
        localStorage.removeItem('accessToken');
        setTimeout(() => this.router.navigate(['/login']), 2000);
      },
      error: (err) => {
        this.error = err.error?.message || 'Account deletion failed.';
        this.loading = false;
      }
    });
  }
}
