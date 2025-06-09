import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = '/api/auth';

  constructor(private http: HttpClient) {}

  login(data: { email: string, password: string }): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, data);
  }

  register(data: { username: string; email: string; password: string }): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, data);
  }

  confirmAccount(token: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/confirm?token=${encodeURIComponent(token)}`, null);
  }

  refreshToken(): Observable<any> {
    return this.http.post(`${this.apiUrl}/refresh`, {}, { withCredentials: true });
  }

  logout(): Observable<any> {
    return this.http.post(`${this.apiUrl}/logout`, {}, { withCredentials: true });
  }

  requestPasswordReset(data: { email: string }): Observable<any> {
    return this.http.post(`${this.apiUrl}/reset-request`, data);
  }

  resetPassword(data: { token: string, newPassword: string }): Observable<any> {
    return this.http.post(`${this.apiUrl}/reset`, data);
  }

  deleteAccount(): Observable<any> {
    return this.http.delete(`${this.apiUrl}/delete-account`, { withCredentials: true });
  }

  reactivateUser(data: { email: string, password: string }): Observable<any> {
    return this.http.post(`${this.apiUrl}/activate-user`, data);
  }

  getUserDetails(): Observable<any> {
    return this.http.get(`${this.apiUrl}/user-details`, { withCredentials: true });
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('accessToken');
  }
}

