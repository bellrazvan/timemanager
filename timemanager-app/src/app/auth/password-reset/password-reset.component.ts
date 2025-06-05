import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import {AuthService} from '../auth.service';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-password-reset',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './password-reset.component.html'
})
export class PasswordResetComponent implements OnInit {
  resetForm: FormGroup;
  message = '';
  error = '';
  token = '';
  email = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.resetForm = this.fb.group({
      newPassword: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.token = params['token'] || '';
      this.email = params['email'] || '';
    });
  }

  onSubmit() {
    if (this.resetForm.invalid) return;
    this.authService.resetPassword(this.resetForm.value)
      .subscribe({
        next: (res: any) => {
          this.message = res.message || 'Password updated successfully.';
          this.error = '';
          setTimeout(() => this.router.navigate(['/login']), 2000);
        },
        error: (err) => {
          this.error = err.error?.message || 'Password reset failed.';
          this.message = '';
        }
      });
  }
}

