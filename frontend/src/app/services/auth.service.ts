import { Injectable, computed, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { environment } from '../../environments/environment';

export interface LoginResponse {
  token: string;
  username: string;
  roles: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly TOKEN_KEY = 'ecoride_token';
  private readonly USER_KEY = 'ecoride_user';
  private readonly baseUrl = `${environment.apiUrl}/api/auth`;

  /** Usuario actualmente logueado (o null). Se inicializa desde localStorage. */
  readonly currentUser = signal<string | null>(localStorage.getItem(this.USER_KEY));

  /** Señal derivada: true si hay un usuario logueado. */
  readonly isLoggedIn = computed(() => this.currentUser() !== null);

  constructor(private http: HttpClient) {}

  login(username: string, password: string): Observable<LoginResponse> {
    return this.http
      .post<LoginResponse>(`${this.baseUrl}/login`, { username, password })
      .pipe(
        tap(res => {
          localStorage.setItem(this.TOKEN_KEY, res.token);
          localStorage.setItem(this.USER_KEY, res.username);
          this.currentUser.set(res.username);
        })
      );
  }

  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.USER_KEY);
    this.currentUser.set(null);
  }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }
}
