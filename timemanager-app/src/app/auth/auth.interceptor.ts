import {HttpErrorResponse, HttpInterceptorFn} from '@angular/common/http';
import {catchError, switchMap} from 'rxjs/operators';
import {inject} from '@angular/core';
import {AuthService} from './auth.service';
import {throwError} from 'rxjs';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const token = localStorage.getItem('accessToken');
  let authReq = req;
  if (token) {
    authReq = req.clone({
      setHeaders: { Authorization: `Bearer ${token}` }
    });
  }

  return next(authReq).pipe(
    catchError((error: HttpErrorResponse) => {
      if ((error.status === 401 || error.status === 403) && !authReq.url.endsWith('/auth/refresh')) {
        return authService.refreshToken().pipe(
          switchMap((res: any) => {
            localStorage.setItem('accessToken', res.accessToken);
            const newReq = authReq.clone({
              setHeaders: { Authorization: `Bearer ${res.accessToken}` }
            });
            return next(newReq);
          }),
          catchError(refreshError => {
            return throwError(() => refreshError);
          })
        );
      }
      return throwError(() => error);
    })
  );
};
