import { Component } from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import { AuthService } from '../auth.service';
import {Router, RouterLink} from '@angular/router';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-login',
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  loginForm: FormGroup;
  error: string = '';
  showReactivateLink: boolean = false;
  inactiveEmail: string = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  onSubmit(): void {
    if (this.loginForm.invalid) {
      return;
    }

    this.authService.login(this.loginForm.value).subscribe({
      next: (res:any) => {
        localStorage.setItem('accessToken', res.accessToken);
        this.router.navigate(['/dashboard']);
      },
      error: (err: any) => {
        this.handleError(err);
      }
    });
  }

  handleError(err: any) {
    const errorMsg = err.error?.error || 'Invalid email or password.';
    if (errorMsg === 'Account is inactive.') {
      this.error = errorMsg;
      this.showReactivateLink = true;
      this.inactiveEmail = this.loginForm.value.email;
    } else {
      this.error = errorMsg;
      this.showReactivateLink = false;
    }
  }

  goToReactivate() {
    this.router.navigate(['/reactivate-user'], {
      queryParams: { email: this.inactiveEmail }
    });
  }
}
