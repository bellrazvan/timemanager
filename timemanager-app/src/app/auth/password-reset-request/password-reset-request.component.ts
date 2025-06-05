import { Component } from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {AuthService} from '../auth.service';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-password-reset-request',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './password-reset-request.component.html'
})
export class PasswordResetRequestComponent {
  resetForm: FormGroup;
  message = '';
  error = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService
  ) {
    this.resetForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]]
    });
  }

  onSubmit() {
    if (this.resetForm.invalid) return;
    this.authService.requestPasswordReset(this.resetForm.value)
      .subscribe({
        next: (res: any) => {
          this.message = res.message || 'If the email exists, a reset link will be sent.';
          this.error = '';
        },
        error: (err) => {
          this.error = err.error?.message || 'Something went wrong.';
          this.message = '';
        }
      });
  }
}

