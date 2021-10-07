import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { Employee } from 'src/app/_models/employee/employee';
import { JobAd } from 'src/app/_models/jobAd/job-ad';
import { EmployeeService } from 'src/app/_services/employee.service';
import { JobAdService } from 'src/app/_services/job-ad.service';

@Component({
  selector: 'app-jobs-details',
  templateUrl: './jobs-details.component.html',
  styleUrls: ['./jobs-details.component.css']
})
export class JobsDetailsComponent implements OnInit {

  private sub: Subscription;
  jobAd: JobAd;
  visible: boolean;
  loggedInEmployee: Employee;
  applicants: Employee[];
  alreadyApplied: boolean = false;

  constructor(private activatedRoute: ActivatedRoute, private jobAdService: JobAdService,
    private employeeService: EmployeeService) { }

  public onApply() {
    this.jobAdService.addApplicant(this.jobAd.id, this.loggedInEmployee.id).subscribe();
    window.location.reload();
  }

  ngOnInit(): void {

    this.sub = this.activatedRoute.params.subscribe(
      (params) => {
        let jobAdID = parseInt(params['id']);

        this.jobAdService.getJobAd(jobAdID).subscribe(
          (resp: any) => {
            this.jobAd = new JobAd(resp.id, resp.position, resp.company,
              resp.location, resp.type, resp.startDate, resp.creator);
            this.jobAd.beautify();
              
            let tempEmp = localStorage.getItem('employee') as string;
            this.loggedInEmployee = JSON.parse(tempEmp);
            
            if(this.loggedInEmployee.id === this.jobAd.creator.id) {
              this.visible = false;
            }
            else {
              this.visible = true;
            }

            this.employeeService.addJobAdViewToEmploye(this.loggedInEmployee.id).subscribe();
          }
        );
        
        this.jobAdService.getJobAdApplicants(jobAdID).subscribe(
          (resp: any) => {
            if(resp._embedded.employeeList == null) {
              return;
            }

            this.applicants = resp._embedded.employeeList;

            for(let applic of this.applicants) {
              if(applic.id === this.loggedInEmployee.id) {
                this.alreadyApplied = true;
              }
            }

          }
        );

    });
    

  }

}
