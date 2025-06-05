import { Component, Inject, PLATFORM_ID } from '@angular/core';
import {Router, RouterLink, RouterLinkActive, RouterOutlet} from '@angular/router';
import { isPlatformBrowser, CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, CommonModule, RouterLink, RouterLinkActive],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'timemanager-app';

  constructor(
    @Inject(PLATFORM_ID) private platformId: Object,
    private router: Router
  ) {}

  get isAuthenticated(): boolean {
    if (isPlatformBrowser(this.platformId)) {
      return !!localStorage.getItem('accessToken');
    }
    return false;
  }

  get isAuthPage(): boolean {
    const url = this.router.url;
    return url === '/login' ||
      url === '/register' ||
      url === '/logout' ||
      url === '/registration-confirmation' ||
      url.startsWith('/confirm') ||
      url === '/reactivate-user';
  }
}
