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

  private employeeURL: string = 'https://localhost:8080/employee';

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
    
    return this.http.post(this.employeeURL + '/add', formData)
    .pipe(map(emp => {
      localStorage.setItem('employee', JSON.stringify(emp));
      return emp;
    }));
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

  public updateEmployee(id: number, newEmail: string, newPassword: string) {

    let changeData = new HttpParams()
      .append('id', id);
    
    if(newEmail != null) {
      changeData = changeData.append('email', newEmail);
    }
    if(newPassword != null) {
      changeData = changeData.append('password', newPassword);
    }

    return this.http.put(this.employeeURL + '/update', changeData);
  }


  public addSkillToEmployee(id: number, skill: string) {
    let data = new HttpParams()
      .append('id', id)
      .append('skill', skill);
    
    return this.http.post(this.employeeURL + '/addskill', data);
  }

  public addJobAdViewToEmploye(id: number) {
    let data = new HttpParams()
      .append('id', id);
    
    return this.http.post(this.employeeURL + '/addjobadview', data);
  }


  public getEmployees(): Observable<Employee[]> {
    return this.http.get<Employee[]>(this.employeeURL + '/all');
  }

  public getEmployeesBySearch(input: string) {
    return this.http.get(this.employeeURL + '/search/' + input);
  }

  public getEmployeePicture(id: number) {
    return this.http.get(this.employeeURL + '/' + String(id) + '/picture');
  }


  public getEmployee(id: number) {
    return this.http.get<Employee>(this.employeeURL + '/' + String(id));
  }


  public getEmployeeJobs(id: number) {
    return this.http.get(this.employeeURL + '/' + String(id) + '/jobs');
  }

  public getEmployeeCertificates(id: number) {
    return this.http.get(this.employeeURL + '/' + String(id) + '/certificates');
  }

  public getEmployeeSkills(id: number) {
    return this.http.get(this.employeeURL + '/' + String(id) + '/skills');
  }

  public getEmployeeConnections(id: number) {
    return this.http.get(this.employeeURL + '/' + String(id) + '/connections');
  }

  public getEmployeePendingConnections(id: number) {
    return this.http.get(this.employeeURL + '/' + String(id) + '/connections/pending');
  }

  public getEmployeeMessages(id: number) {
    return this.http.get(this.employeeURL + '/' + String(id) + '/messages');
  }

  public getEmployeePosts(id: number) {
    return this.http.get(this.employeeURL + '/' + String(id) + '/posts');
  }

  public getEmployeeFeedPosts(id: number) {
    return this.http.get(this.employeeURL + '/' + String(id) + '/postsfeed');
  }

  public getEmployeeJobAds(id: number) {
    return this.http.get(this.employeeURL + '/' + String(id) + '/jobads');
  }

  public getEmployeeLikes(id: number) {
    return this.http.get(this.employeeURL + '/' + String(id) + '/likes');
  }

  public getEmployeeComments(id: number) {
    return this.http.get(this.employeeURL + '/' + String(id) + '/comments');
  }

}
