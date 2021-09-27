import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { map } from 'rxjs/operators';
import { Employee } from 'src/app/_models/employee/employee';
import { EmployeeService } from 'src/app/_services/employee.service';
import * as globals from '../../globals';



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

  ngOnInit(): void {
    this.sub = this.activatedRoute.params.subscribe(
      (params) => {
        this.employeeID = parseInt(params['id']);
        this.employeeService.getEmployee(this.employeeID).subscribe(
          (emp: any) => 
            {
              this.employee = new Employee(emp.id, emp.name, emp.surname, emp.email,
                emp.password, emp.jobTitle, emp.phone, emp.imageUrl);
            }
        );

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
