import { Component } from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {AuthService} from '../auth.service';
import {Router} from '@angular/router';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-reactivate-user',
  templateUrl: './reactivate-user.component.html',
  imports: [CommonModule, ReactiveFormsModule],
  styleUrl: './reactivate-user.component.css'
})
export class ReactivateUserComponent {
  reactivateForm: FormGroup;
  message: string = '';
  error: string = '';
  loading: boolean = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.reactivateForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]]
    });
  }

  onSubmit(): void {
    if (this.reactivateForm.invalid) return;
    this.loading = true;
    const { email } = this.reactivateForm.value;
    this.authService.reactivateUser(email).subscribe({
      next: (res: any) => {
        this.message = res.message || 'Account activated successfully. You can now log in.';
        this.error = '';
        this.loading = false;
        setTimeout(() => this.router.navigate(['/login']), 2000);
      },
      error: (err) => {
        this.error = err.error?.message || 'Account reactivation failed.';
        this.message = '';
        this.loading = false;
      }
    });
  }
}
