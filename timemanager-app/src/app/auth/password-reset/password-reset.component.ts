import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {AuthService} from '../auth.service';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-password-reset',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './password-reset.component.html'
})
export class PasswordResetComponent implements OnInit {
  resetForm: FormGroup;
  message: string = '';
  error: string = '';
  token: string = '';

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
    });
  }

  onSubmit() {
    if (this.resetForm.invalid) return;
    const data = {
      token: this.token,
      newPassword: this.resetForm.value.newPassword
    }
    this.authService.resetPassword(data)
      .subscribe({
        next: (res: any) => {
          this.message = res.success + '<br>Redirecting to Login...';
          this.error = '';
          setTimeout(() => this.router.navigate(['/login']), 2000);
        },
        error: (err) => {
          this.error = err.error?.error || 'Password reset failed.';
          this.message = '';
        }
      });
  }
}
