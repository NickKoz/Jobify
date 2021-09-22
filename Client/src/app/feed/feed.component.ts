import { Component, HostListener, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Employee } from '../_models/employee/employee';
import { EmployeeService } from '../_services/employee.service';
import * as globals from '../globals'

@Component({
  selector: 'app-feed',
  templateUrl: './feed.component.html',
  styleUrls: ['./feed.component.css']
})
export class FeedComponent implements OnInit {

  employee: any;

  constructor(private router: Router, private employeeService: EmployeeService) { }

  ngOnInit(): void {
    let emp = localStorage.getItem('employee') as string;
    this.employee = JSON.parse(emp);

    this.employeeService.getEmployeePicture(this.employee.id).subscribe(
      (res: any) => {
        if(res === null) {
          this.employee.photo = globals.blankPicture;
          return;
        }
        let type = res.type;
        this.employee.photo = 'data:image/' + String(type) + ';base64,' + String(res.bytes);
      }
    );

  }


}
