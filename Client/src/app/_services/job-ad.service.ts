import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { JobAd } from '../_models/jobAd/job-ad';

@Injectable({
  providedIn: 'root'
})
export class JobAdService {

  private jobAdURL = 'https://localhost:8080/jobad';

  constructor(private http: HttpClient, private router: Router) { }


  public getJobAd(id: number) {
    return this.http.get(this.jobAdURL + '/' + String(id));
  }

  public getAllJobAds() {
    return this.http.get(this.jobAdURL + '/all');
  }

  public addJobAd(jobAd: JobAd, creator_id: number){
    let param = new HttpParams()
      .append('creator_id', creator_id);

    return this.http.post(this.jobAdURL + '/add',  jobAd, {params: param});
  }
  
  public addApplicant(jobad_id: number, applicant_id: number) {
    let param = new HttpParams()
    .append('jobad_id', jobad_id)
    .append('applicant_id', applicant_id);

    return this.http.put(this.jobAdURL + '/addapplicant', param);
  }

  public getJobAdApplicants(jobad_id: number) {
    return this.http.get(this.jobAdURL + '/' + String(jobad_id) + '/applicants');

  }



}
