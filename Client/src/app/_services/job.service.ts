import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Job } from '../_models/job/job';

@Injectable({
  providedIn: 'root'
})
export class JobService {

  private jobURL: string = 'https://localhost:8080/job';
  
  private httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  }

  constructor(private http: HttpClient, private router: Router) { }

  public addJobToEmployee(job: Job, employee_id: number) {

    let param = new HttpParams()
      .append('employee_id', employee_id);

    return this.http.post(this.jobURL + '/add',  job, {params: param});

  }
  
  
}
