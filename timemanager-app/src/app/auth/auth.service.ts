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
    return this.http.post(`${this.apiUrl}/refresh`, null, { withCredentials: true });
  }

  logout(): Observable<any> {
    return this.http.post(`${this.apiUrl}/logout`, null, { withCredentials: true });
  }

  requestPasswordReset(email: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/reset-request`, { email });
  }

  resetPassword(data: { token: string, newPassword: string }): Observable<any> {
    return this.http.post(`${this.apiUrl}/reset`, data);
  }

  deleteAccount(): Observable<any> {
    return this.http.delete(`${this.apiUrl}/delete-account`);
  }

  reactivateUser(email: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/activate-user`, { email });
  }
}

