import { Component, OnInit } from '@angular/core';
import { Employee } from '../_models/employee/employee';
import { EmployeeService } from '../_services/employee.service';
import { MessageService } from '../_services/message.service';
import * as globals from '../globals';
import { Message } from '../_models/message/message';
import { Router } from '@angular/router';

@Component({
  selector: 'app-messaging',
  templateUrl: './messaging.component.html',
  styleUrls: ['./messaging.component.css']
})
export class MessagingComponent implements OnInit {

  employee: Employee;

  constructor(private messageService: MessageService, private employeeService: EmployeeService,
    private router: Router) { }

  ngOnInit(): void {

    let tempEmp = localStorage.getItem('employee') as string;
    this.employee = JSON.parse(tempEmp);

    this.employeeService.getEmployeeMessages(this.employee.id).subscribe(
      (resp: any) => {
        if(resp._embedded === null) {
          return;
        }

        let messageList: Message[] = resp._embedded.messageList;
        this.employee.messages = messageList;

        this.employee.outgoingMessages = [];
        this.employee.incomingMessages = [];

        for(let mess of messageList){

          if(this.employee.id === mess.sender.id) {
            this.employee.outgoingMessages.push(mess);
          }
          else {
            this.employee.incomingMessages.push(mess);
          }
        }


        this.employee.chats = [];

        for(let mess of this.employee.incomingMessages) {
          let id = mess.sender.id;

          let alreadyExists = false;
          for(let emp of this.employee.chats){
            if(emp.id === id) {
              alreadyExists = true;
            }
          }
          if(!alreadyExists) {
            this.employee.chats.push(mess.sender);
          }
        }

        for(let mess of this.employee.outgoingMessages) {
          let id = mess.receiver.id;

          let alreadyExists = false;
          for(let emp of this.employee.chats){
            if(emp.id === id) {
              alreadyExists = true;
            }
          }
          if(!alreadyExists) {
            this.employee.chats.push(mess.receiver);
          }
        }

        for(let emp of this.employee.chats){

          this.employeeService.getEmployeePicture(emp.id).subscribe(
            (res: any) => {
              if(res === null) {
                emp.photo = globals.blankPicture;
                return;
              }
              let type = res.type;
              emp.photo = 'data:image/' + String(type) + ';base64,' + String(res.bytes);
            }
          );

        }

        localStorage.setItem('employee', JSON.stringify(this.employee));

        let lastEmpID;
        if(messageList[messageList.length - 1].sender.id === this.employee.id){
          lastEmpID = messageList[messageList.length - 1].receiver.id;
        }
        else {
          lastEmpID = messageList[messageList.length - 1].sender.id;
        }

        this.router.navigate(['/messaging/' + String(lastEmpID)]);

      }
    );



  }

}
