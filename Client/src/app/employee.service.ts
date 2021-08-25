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
    headers: new HttpHeaders({'Content-Type': 'multipart/form-data'})
  }

  constructor(private http: HttpClient) {}


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


  public loginEmployee() {
    
  }


  public getEmployees(): Observable<Employee[]> {
    return this.http.get<Employee[]>(this.employeeURL + '/all');
  }

  

}
