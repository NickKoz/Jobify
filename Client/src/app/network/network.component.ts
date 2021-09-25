import { Component, OnInit } from '@angular/core';
import { Employee } from '../_models/employee/employee';
import { EmployeeService } from '../_services/employee.service';
import * as globals from '../globals';
import { Job } from '../_models/job/job';

@Component({
  selector: 'app-network',
  templateUrl: './network.component.html',
  styleUrls: ['./network.component.css']
})
export class NetworkComponent implements OnInit {

  employee: Employee;
  noJobs: boolean = false;

  constructor(private employeeService: EmployeeService) { }

  ngOnInit(): void {
      let tempEmp = localStorage.getItem('employee') as string;
      this.employee = JSON.parse(tempEmp);

      this.employeeService.getEmployeeConnections(this.employee.id).subscribe(
        (resp: any) => {
          if(resp._embedded == null) {
            return;
          }

          this.employee.connections = resp._embedded.employeeList;

          for(let emp of this.employee.connections) {

            // Getting their picture.
            this.employeeService.getEmployeePicture(emp.id).subscribe(
              (res: any) => {
                if(res === null) {
                  emp.photo = globals.blankPicture;
                  return;
                }
                let type = res.type;
                emp.photo = 'data:image/' + String(type) + ';base64,' + res.bytes;
              }
            );

            this.employeeService.getEmployeeJobs(emp.id).subscribe(
              (resp: any) => {
                  emp.jobs = [];
                  if(resp._embedded == null){
                    this.noJobs = true;
                    return;
                  }
                  let jobList = resp._embedded.jobList;
                  let job = jobList[0];

                  emp.jobs.push(new Job(job.id, job.position, job.company,
                    job.location, job.type, job.startDate, job.endDate, job.hidden));
                }
            );



          }
          
          console.log(this.employee.connections);

        }

      );


  }

}
