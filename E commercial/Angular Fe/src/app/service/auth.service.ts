import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../enviroments/enviroments';



export class AuthService {
    private apiBaseUrl = environment.apiUrl;
  
    constructor(private http: HttpClient) { }
  
    // Corrected function name and parameter usage
    authenticate(loginType: 'facebook' | 'google'): Observable<string> {
      debugger
      return this.http.get(
        `${this.apiBaseUrl}/users/auth/social-login?login_type=${loginType}`, 
        { responseType: 'text' }
      );
    }
  
    exchangeCodeForToken(code: string, loginType: 'facebook' | 'google'): Observable<any> {
      const params = new HttpParams()
        .set('code', code)
        .set('login_type', loginType);
  
      return this.http.get<any>(`${this.apiBaseUrl}/users/auth/social/callback`, { params });
    }
  }