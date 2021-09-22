import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { Employee } from '../_models/employee/employee';
import { EmployeeService } from '../_services/employee.service';
import * as globals from '../globals'
import { Job } from '../_models/job/job';
import { Certificate } from '../_models/certificate/certificate';

@Component({
  selector: 'app-employee-profile',
  templateUrl: './employee-profile.component.html',
  styleUrls: ['./employee-profile.component.css']
})
export class EmployeeProfileComponent implements OnInit {

  employeeID: number;
  employee: Employee;
  private sub: Subscription;

  constructor(private activatedRoute: ActivatedRoute, 
    private employeeService: EmployeeService) { }

  ngOnInit(): void {
    this.sub = this.activatedRoute.params.subscribe(
      (params) => {
        this.employeeID = parseInt(params['id']);

        // Getting employee's data.
        this.employeeService.getEmployee(this.employeeID).subscribe(
          (emp: any) =>{
            this.employee = new Employee(emp.id, emp.name, emp.surname, emp.email,
              emp.password, emp.jobTitle, emp.phone, emp.photo);
          }
        );
        
        // Getting their jobs.
        this.employeeService.getEmployeeJobs(this.employeeID).subscribe(
          (resp: any) => {
            if(resp._embedded == null){
              return;
            }

            let jobList = resp._embedded.jobList;
            
            for(let job of jobList) {
              this.employee.jobs.push(new Job(job.id, job.position, job.company,
                job.location, job.type, job.startDate, job.endDate, job.hidden));
            }
            console.log(this.employee.jobs);

          }
        );
        
        // Getting their certificates.
        this.employeeService.getEmployeeCertificates(this.employeeID).subscribe(
          (resp: any) => {
            if(resp._embedded == null){
              return;
            }

            let certificateList = resp._embedded.certificateList;
            
            for(let cert of certificateList) {
              this.employee.certificates.push(new Certificate(cert.id, cert.school, cert.degree,
                cert.field, cert.startDate, cert.endDate, cert.grade, cert.hidden));
            }
            console.log(this.employee.certificates);

          }
        );
        
        // Getting their skills.
        this.employeeService.getEmployeeSkills(this.employeeID).subscribe(
          (resp: any) => {
            if(resp == null){
              return;
            }

            let skillsList = resp;
            
            for(let skill of skillsList) {
              this.employee.skills.push(skill);
            }
            console.log(this.employee.skills);
          }

        );
      
        // Getting their picture.
        this.employeeService.getEmployeePicture(this.employeeID).subscribe(
          (res: any) => {
            if(res === null) {
              this.employee.photo = globals.blankPicture;
              return;
            }
            let type = res.type;
            this.employee.photo = 'data:image/' + String(type) + ';base64,' + res.bytes;
          }
        );
      });
      
  }

}
