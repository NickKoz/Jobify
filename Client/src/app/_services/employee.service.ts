import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Employee } from '../_models/employee/employee';
import { Observable } from 'rxjs';
import { first, map } from 'rxjs/operators'
import { Router } from '@angular/router';


@Injectable({
  providedIn: 'root'
})
export class EmployeeService {

  private employeeURL: string = 'http://localhost:8080/employee';

  private httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  }

  constructor(private http: HttpClient, private router: Router) {}


  public registerEmployee(employee: Employee, profilePicture: File) {

    const employeeJson = JSON.stringify(employee);
    const employeeData = new Blob([employeeJson], {
      type: 'application/json'
    });

    const formData = new FormData();
    formData.append('employee', employeeData);

    if(profilePicture) {
      formData.append('picture', profilePicture, profilePicture.name);
    }
    
    return this.http.post(this.employeeURL + '/add', formData);
  }


  public loginEmployee(email: string, password: string) {

    let loginData = new HttpParams()
      .append('email', email)
      .append('password', password);

    return this.http.post(this.employeeURL + '/login', loginData)
    .pipe(map(emp => {
      localStorage.setItem('employee', JSON.stringify(emp));
      return emp;
    }));
  }


  public logoutEmployee() {
    localStorage.removeItem('employee');
    this.router.navigate(['']);
  }


  public getEmployees(): Observable<Employee[]> {
    return this.http.get<Employee[]>(this.employeeURL + '/all');
  }


  public getEmployeePicture(id: number) {
    return this.http.get(this.employeeURL + '/' + String(id) + '/picture');
  }


  public getEmployee(id: number) {
    return this.http.get<Employee>(this.employeeURL + '/' + String(id));
  }


}
