import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { Employee } from 'src/app/_models/employee/employee';
import { Message } from 'src/app/_models/message/message';
import { EmployeeService } from 'src/app/_services/employee.service';
import { MessageService } from 'src/app/_services/message.service';
import * as globals from '../../globals';

@Component({
  selector: 'app-message-details',
  templateUrl: './message-details.component.html',
  styleUrls: ['./message-details.component.css']
})
export class MessageDetailsComponent implements OnInit {

  loggedInEmployee: Employee;
  chat: Employee;
  private sub: Subscription;
  messageForm: FormGroup;

  constructor(private router: Router, private activatedRoute: ActivatedRoute, 
    private employeeService: EmployeeService, private formBuilder: FormBuilder,
    private messageService: MessageService) {
    
    this.messageForm = this.formBuilder.group({
      text: ['', [ Validators.required]]
    });

  }

  public submitMessage() {
    this.messageService.addMessage(this.loggedInEmployee.id, this.chat.id, this.messageForm.get('text')!.value)
    .subscribe();

    setTimeout(()=> console.log(""), 3000);
    window.location.reload();
  }
  

  ngOnInit(): void {

    let tempEmp = localStorage.getItem('employee') as string;
    this.loggedInEmployee = JSON.parse(tempEmp);

    this.sub = this.activatedRoute.params.subscribe(
      (params) => {
        let chatID = parseInt(params['id']);

        this.employeeService.getEmployee(chatID).subscribe(
          (emp: any) => {
            this.chat = new Employee(emp.id, emp.name, emp.surname, emp.email,
              emp.password, emp.phone, emp.imageUrl);

            this.employeeService.getEmployeePicture(this.chat.id).subscribe(
              (res: any) => {
                if(res === null) {
                  this.chat.photo = globals.blankPicture;
                  return;
                }
                let type = res.type;
                this.chat.photo = 'data:image/' + String(type) + ';base64,' + String(res.bytes);
              }
            );

            this.employeeService.getEmployeePicture(this.loggedInEmployee.id).subscribe(
              (res: any) => {
                if(res === null) {
                  this.loggedInEmployee.photo = globals.blankPicture;
                  return;
                }
                let type = res.type;
                this.loggedInEmployee.photo = 'data:image/' + String(type) + ';base64,' + String(res.bytes);
              }
            );

            for(let mess of this.loggedInEmployee.incomingMessages){
              if(this.chat.id === mess.sender.id) {
                this.chat.outgoingMessages.push(mess);
              }
            }

            for(let mess of this.loggedInEmployee.outgoingMessages){
              if(this.chat.id === mess.receiver.id) {
                this.chat.incomingMessages.push(mess);
              }
            }

            for(let mess of this.chat.incomingMessages) {
              this.chat.messages.push(mess);
            }
            for(let mess of this.chat.outgoingMessages) {
              this.chat.messages.push(mess);
            }

            this.chat.messages = this.chat.messages.sort(
              (mess1: Message, mess2: Message) => {
                if(mess1.timeSent.localeCompare(mess2.timeSent) < 0){
                  return -1;
                }
                else if(mess1.timeSent.localeCompare(mess2.timeSent) > 0){
                  return 1;
                }
                return 0;
              }
            );
            
          }
        );


      }
    );



  }

}
