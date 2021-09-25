import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  private adminURL: string = 'https://localhost:8080/admin';

  constructor(private http: HttpClient, private router: Router) { }

  public loginAdmin(email: string, password: string) {

    let loginData = new HttpParams()
      .append('email', email)
      .append('password', password);

    return this.http.post(this.adminURL + '/login', loginData)
    .pipe(map(adm => {
      localStorage.setItem('admin', JSON.stringify(adm)) ;
      return adm;
    }));

  }

  public logoutAdmin() {
    localStorage.removeItem('admin');
    this.router.navigate(['']);
  }


}
