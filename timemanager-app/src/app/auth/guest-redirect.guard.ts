import {CanActivateFn, Router} from '@angular/router';
import {inject} from '@angular/core';
import {AuthService} from './auth.service';

export const guestRedirectGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  const isAuthenticated = inject(AuthService).isLoggedIn();
  if (isAuthenticated) {
    router.navigate(['/dashboard']);
    return false;
  }
  return true;
};
