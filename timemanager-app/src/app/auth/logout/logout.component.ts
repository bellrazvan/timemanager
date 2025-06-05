import {Component, OnInit} from '@angular/core';
import {AuthService} from '../auth.service';
import {Router} from '@angular/router';
import {CommonModule} from '@angular/common';
import {ReactiveFormsModule} from '@angular/forms';

@Component({
  selector: 'app-logout',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './logout.component.html',
  styleUrl: './logout.component.css'
})
export class LogoutComponent implements OnInit {
  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.authService.logout().subscribe({
      next: () => {
        localStorage.removeItem('accessToken');
        this.router.navigate(['/login']);
      },
      error: () => {
        localStorage.removeItem('accessToken');
        this.router.navigate(['/login']);
      }
    });
  }
}
