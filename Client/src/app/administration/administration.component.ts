import { Component, OnInit } from '@angular/core';
import { Employee } from '../_models/employee/employee';
import { AdminService } from '../_services/admin.service';
import { EmployeeService } from '../_services/employee.service';

@Component({
  selector: 'app-administration',
  templateUrl: './administration.component.html',
  styleUrls: ['./administration.component.css']
})
export class AdministrationComponent implements OnInit {

  employees: Employee[];

  constructor(private employeeService: EmployeeService, private adminService: AdminService) { }

  public logout() {
    this.adminService.logoutAdmin();
  }

  ngOnInit(): void {
    let resp = this.employeeService.getEmployees();
    resp.subscribe(
      (resp: any) => {
      this.employees = resp._embedded.employeeList;
      }
    );
  }

}
