import { Component, OnInit } from '@angular/core';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { map } from 'rxjs/operators';
import { Employee } from 'src/app/employee/employee';
import { EmployeeService } from 'src/app/employee/employee.service';



@Component({
  selector: 'app-employee-details',
  templateUrl: './employee-details.component.html',
  styleUrls: ['./employee-details.component.css']
})
export class EmployeeDetailsComponent implements OnInit {

  employeeID: number;
  employee: Employee;
  employeePicture: any;
  blankPicture: any = '../../assets/Photos/blank-profile-picture.png'
  private sub: Subscription;

  constructor(private activatedRoute: ActivatedRoute, private employeeService: EmployeeService) { }

  ngOnInit(): void {
    this.sub = this.activatedRoute.params.subscribe(
      (params) => {
        this.employeeID = parseInt(params['id']);
        this.employeeService.getEmployee(this.employeeID).pipe(
          map((emp) => {this.employee = this.employeeService.extractEmployee(emp);})
          ).subscribe();

        this.employeeService.getEmployeePicture(this.employeeID).pipe(
          map((res: any) => {
            let type = res.type;
            this.employeePicture = 'data:image/' + String(type) + ';base64,' + res.bytes;
          })
        ).subscribe();
    });
  
  }

}
