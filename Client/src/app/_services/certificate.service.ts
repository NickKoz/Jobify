import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Certificate } from '../_models/certificate/certificate';

@Injectable({
  providedIn: 'root'
})
export class CertificateService {

  private certificateURL: string = 'https://localhost:8080/certificate';

  constructor(private http: HttpClient, private router: Router) { }

  public addCertificateToEmployee(certificate: Certificate, employee_id: number) {
    let param = new HttpParams()
      .append('employee_id', employee_id);
    
      return this.http.post(this.certificateURL + '/add', certificate, {params: param});
  }

}
