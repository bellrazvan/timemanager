import {Component, OnDestroy, OnInit, Inject, PLATFORM_ID} from '@angular/core';
import {ActivatedRoute, RouterLink} from '@angular/router';
import {AuthService} from '../auth.service';
import {ReactiveFormsModule} from '@angular/forms';
import {CommonModule, isPlatformBrowser} from '@angular/common';
import {Subscription} from 'rxjs';

@Component({
  selector: 'app-confirm-account',
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './confirm-account.component.html',
  styleUrl: './confirm-account.component.css'
})
export class ConfirmAccountComponent implements OnInit, OnDestroy {
  message: string = '';
  error: string = '';
  private subscription: Subscription | null = null;
  private hasAttemptedConfirmation: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private authService: AuthService,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.subscription = this.route.queryParams.subscribe(params => {
        const token = params['token'];
        if (token && !this.hasAttemptedConfirmation) {
          this.hasAttemptedConfirmation = true;
          this.confirmAccount(token);
        }
      });
    }
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  private confirmAccount(token: string): void {
    this.authService.confirmAccount(token).subscribe({
      next: () => {
        this.message = 'Your account has been confirmed successfully!';
      },
      error: (err) => {
        this.error = err.error?.message || 'Failed to confirm your account. The token may be invalid or expired.';
      }
    });
  }
}
