import {Component, EventEmitter, Output} from '@angular/core';
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
  @Output() close = new EventEmitter<void>();
  message: string = '';
  error: string = '';
  loading: boolean = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  confirmDelete(): void {
    this.loading = true;
    this.authService.deleteAccount().subscribe({
      next: (res: any) => {
        this.message = res.success || 'Account deactivated successfully. You have 30 days until your account is deleted.';
        localStorage.removeItem('accessToken');
        setTimeout(() => {
          this.close.emit();
          this.router.navigate(['/login']);
        }, 2000);
      },
      error: (err) => {
        this.error = err.error?.error || 'Account deletion failed.';
        this.loading = false;
      }
    });
  }

  cancelDelete(): void {
    this.close.emit();
  }
}

