import { Component, OnInit } from '@angular/core';
import { Employee } from '../_models/employee/employee';
import { JobAd } from '../_models/jobAd/job-ad';
import { EmployeeService } from '../_services/employee.service';
import { JobAdService } from '../_services/job-ad.service';
import * as globals from '../globals'
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-jobs',
  templateUrl: './jobs.component.html',
  styleUrls: ['./jobs.component.css']
})
export class JobsComponent implements OnInit {

  jobAdList: JobAd[];
  employee: Employee;
  jobAdForm: FormGroup;

  constructor(private jobAdService: JobAdService, private employeeService: EmployeeService,
    private formBuilder: FormBuilder) {

      this.jobAdForm = this.formBuilder.group({
        position: ['', [ Validators.required]],
        company: ['', [ Validators.required]],
        location: ['', [ Validators.required]],
        type: ['', [Validators.required]],
        startDate: ['', [Validators.required]]
      });

  }

  public submitJobAd() {
    let jobAd = new JobAd(0, this.jobAdForm.get('position')!.value, this.jobAdForm.get('company')!.value,
      this.jobAdForm.get('location')!.value, this.jobAdForm.get('type')!.value - 1, this.jobAdForm.get('startDate')!.value,
      this.employee);
    
    this.jobAdService.addJobAd(jobAd, this.employee.id).subscribe();
    window.location.reload();
  }

  ngOnInit(): void {

    let emp = localStorage.getItem('employee') as string;
    this.employee = JSON.parse(emp);

    this.employeeService.getEmployee(this.employee.id).subscribe(
      (emp: any) =>{
        this.employee = new Employee(emp.id, emp.name, emp.surname, emp.email,
          emp.password, emp.phone, emp.photo);
          
        this.employeeService.getEmployeeJobAds(this.employee.id).subscribe(
          (resp: any) => {
            if(resp._embedded == null) {
              return;
            }
            
            this.jobAdList = resp._embedded.jobAdList;
          });
      }
    );

  }

}
