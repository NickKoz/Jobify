import { Component, OnInit } from '@angular/core';
import { Employee } from '../_models/employee/employee';
import { EmployeeService } from '../_services/employee.service';
import { MessageService } from '../_services/message.service';

@Component({
  selector: 'app-messaging',
  templateUrl: './messaging.component.html',
  styleUrls: ['./messaging.component.css']
})
export class MessagingComponent implements OnInit {

  employee: Employee;

  constructor(private messageService: MessageService, private employeeService: EmployeeService) { }

  ngOnInit(): void {

    let tempEmp = localStorage.getItem('employee') as string;
    this.employee = JSON.parse(tempEmp);

    this.employeeService.getEmployeeMessages(1).subscribe(
      (resp: any) => {
        if(resp._embedded === null) {
          return;
        }

        let messageList = resp._embedded.messageList;
        console.log(messageList)
      }
    );



  }

}
