import { Component, OnInit } from '@angular/core';
import { Employee } from '../_models/employee/employee';
import { ConnectionService } from '../_services/connection.service';
import { EmployeeService } from '../_services/employee.service';

@Component({
  selector: 'app-notifications',
  templateUrl: './notifications.component.html',
  styleUrls: ['./notifications.component.css']
})
export class NotificationsComponent implements OnInit {

  pendingConnections: Employee[] = [];
  employee: Employee;

  constructor(private employeeService: EmployeeService, 
    private connectionService: ConnectionService) { }

  ngOnInit(): void {
    let tempEmp = localStorage.getItem('employee') as string;
    this.employee = JSON.parse(tempEmp);

  
    // Getting pending connection requests from other employees.
    this.employeeService.getEmployeePendingConnections(this.employee.id).subscribe(
      (resp: any) => {
        if(resp._embedded == null) {
          return;
        }

        this.pendingConnections = resp._embedded.employeeList;

        console.log(this.pendingConnections);

      }
    );

  }

  public handleRequest(accept: boolean, requester: number) {

    if(accept) {
      this.connectionService.acceptConnection(this.employee.id, requester).subscribe();
    }
    else {
      this.connectionService.deleteConnection(this.employee.id, requester).subscribe();
    }
    window.location.reload();
  }



}
