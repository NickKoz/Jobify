import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Employee } from './employee';
import { Observable } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class EmployeeService {

  private employeeURL: string = 'http://localhost:8080/employee';

  private httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  }

  constructor(private http: HttpClient) {}


  public registerEmployee(employee: Employee) {
    return this.http.post<Employee>(this.employeeURL + '/add', JSON.stringify(employee), this.httpOptions);
    // return this.http.post<Employee>(this.employeeURL, employee);
  }


  public getEmployees(): Observable<Employee[]> {
    return this.http.get<Employee[]>(this.employeeURL + '/all');
  }

  

}
