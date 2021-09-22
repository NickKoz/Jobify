import { Component, OnInit } from '@angular/core';
import { Employee } from '../_models/employee/employee';
import { EmployeeService } from '../_services/employee.service';

@Component({
  selector: 'app-network',
  templateUrl: './network.component.html',
  styleUrls: ['./network.component.css']
})
export class NetworkComponent implements OnInit {

  employee: Employee;

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
          
          console.log(this.employee.connections);

        }

      );


  }

}
