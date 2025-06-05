import { Component } from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {AuthService} from '../auth.service';
import {CommonModule} from '@angular/common';
import {Router, RouterLink} from '@angular/router';

@Component({
  selector: 'app-register',
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './register.component.html'
})
export class RegisterComponent {
  registerForm: FormGroup;
  error: string = '';

  constructor(private fb: FormBuilder,
              private authService: AuthService,
              private router: Router
  ) {
    this.registerForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      username: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  onSubmit(): void {
    if (this.registerForm.invalid) {
      return;
    }

    this.authService.register(this.registerForm.value).subscribe({
      next: (): void => {
        this.router.navigate(['/registration-confirmation']);
      },
      error: (err: any): void => {
        this.error = err.error?.message || 'Registration failed.';
      },
    });
  }
}
