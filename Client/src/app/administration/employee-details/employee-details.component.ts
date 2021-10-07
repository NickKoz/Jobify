import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { Employee } from 'src/app/_models/employee/employee';
import { EmployeeService } from 'src/app/_services/employee.service';
import * as globals from '../../globals';
import exportFromJSON from 'export-from-json';
import { Job } from 'src/app/_models/job/job';
import { Certificate } from 'src/app/_models/certificate/certificate';
import { Comment } from 'src/app/_models/comment/comment';



@Component({
  selector: 'app-employee-details',
  templateUrl: './employee-details.component.html',
  styleUrls: ['./employee-details.component.css']
})
export class EmployeeDetailsComponent implements OnInit {

  employeeID: number;
  employee: Employee;
  private sub: Subscription;

  constructor(private activatedRoute: ActivatedRoute, private employeeService: EmployeeService) { }

  public downloadXML() {
    const data = [this.employee];
    const fileName = 'employeeDetails';
    const exportType =  exportFromJSON.types.xml;
    exportFromJSON({ data, fileName, exportType });
  }

  public downloadJSON() {
    const data = [this.employee];
    const fileName = 'employeeDetails';
    const exportType =  exportFromJSON.types.json;
    exportFromJSON({ data, fileName, exportType });
  }

  ngOnInit(): void {
    this.sub = this.activatedRoute.params.subscribe(
      (params) => {
        this.employeeID = parseInt(params['id']);
        this.employeeService.getEmployee(this.employeeID).subscribe(
          (emp: any) => 
            {
              this.employee = new Employee(emp.id, emp.name, emp.surname, emp.email,
                emp.password, emp.phone, emp.imageUrl);
            }
        );
          
        // Getting their photo
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

        // Getting their jobs.
        this.employeeService.getEmployeeJobs(this.employeeID).subscribe(
          (resp: any) => {
            if(resp._embedded == null){
              return;
            }

            let jobList:Job[] = resp._embedded.jobList;
            
            for(let job of jobList) {
              this.employee.jobs.push(job);
                
              this.employee.jobs[this.employee.jobs.length - 1].beautify();
            }

          }
        );
        
        // Getting their certificates.
        this.employeeService.getEmployeeCertificates(this.employeeID).subscribe(
          (resp: any) => {
            if(resp._embedded == null){
              return;
            }

            let certificateList:Certificate[] = resp._embedded.certificateList;
            
            for(let cert of certificateList) {
              this.employee.certificates.push(cert);
              
              this.employee.certificates[this.employee.certificates.length - 1].beautify();
            }

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
          }
        );
        
        this.employeeService.getEmployeeConnections(this.employeeID).subscribe(
          (resp: any) => {
            if(resp._embedded == null){
              return;
            }
            this.employee.connections = resp._embedded.employeeList;
          }
        );

        this.employeeService.getEmployeePosts(this.employeeID).subscribe(
          (resp: any) => {
            if(resp._embedded == null){
              return;
            }
              this.employee.posts = resp._embedded.postList;
          }
        );

        this.employeeService.getEmployeeJobAds(this.employeeID).subscribe(
          (resp: any) => {
            if(resp._embedded == null){
              return;
            }
              this.employee.jobAds = resp._embedded.jobAdList;
          }
        );

        this.employeeService.getEmployeeLikes(this.employeeID).subscribe(
          (resp: any) => {
            if(resp._embedded == null){
              return;
            }
            this.employee.likes = resp._embedded.postList;
          }
        );

        this.employeeService.getEmployeeComments(this.employeeID).subscribe(
          (resp: any) => {
            if(resp == null){
              return;
            }
            this.employee.comments = resp;
          }
        );

    });
  
  }

}
