import { Component } from '@angular/core';
import {AuthService} from '../auth.service';
import {CommonModule} from '@angular/common';
import {DeleteAccountComponent} from '../delete-account/delete-account.component';

@Component({
  selector: 'app-user-profile',
  standalone: true,
  imports: [CommonModule, DeleteAccountComponent],
  templateUrl: './user-profile.component.html',
  styleUrl: './user-profile.component.css'
})
export class UserProfileComponent {
  userData: any = null;
  loading = true;
  error = '';
  showDeleteAccountModal = false;

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.authService.getUserDetails().subscribe({
      next: (res: any) => {
        this.userData = res;
        this.loading = false;
      },
      error: (err) => {
        this.error = err.error?.error || 'Failed to load user details.';
        this.loading = false;
      }
    });
  }

  openDeleteAccountModal() {
    this.showDeleteAccountModal = true;
  }

  closeDeleteAccountModal() {
    this.showDeleteAccountModal = false;
  }
}
