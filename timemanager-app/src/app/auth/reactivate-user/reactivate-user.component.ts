import { Component } from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {AuthService} from '../auth.service';
import {ActivatedRoute, Router, RouterLink} from '@angular/router';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-reactivate-user',
  templateUrl: './reactivate-user.component.html',
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
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
    private router: Router,
    private activatedRoute: ActivatedRoute
  ) {
    this.reactivateForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });

    this.activatedRoute.queryParams.subscribe(params => {
      if (params['email']) {
        this.reactivateForm.patchValue({ email: params['email'] });
      }
    });
  }

  onSubmit(): void {
    if (this.reactivateForm.invalid) return;
    this.loading = true;
    this.authService.reactivateUser(this.reactivateForm.value).subscribe({
      next: (res: any) => {
        this.message = res.success || 'Account activated successfully. You can now log in.';
        this.error = '';
        this.loading = false;
        setTimeout(() => this.router.navigate(['/login']), 2000);
      },
      error: (err) => {
        this.error = err.error?.error || 'Account reactivation failed.';
        this.message = '';
        this.loading = false;
      }
    });
  }
}
