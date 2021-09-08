import { Component, HostListener, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Employee } from '../_models/employee/employee';
import { EmployeeService } from '../_services/employee.service';

@Component({
  selector: 'app-feed',
  templateUrl: './feed.component.html',
  styleUrls: ['./feed.component.css']
})
export class FeedComponent implements OnInit {

  employee: any;
  tabSelected: boolean[] = [false];

  constructor(private router: Router, private employeeService: EmployeeService) { }

  ngOnInit(): void {
    let emp = localStorage.getItem('employee') as string;
    this.employee = JSON.parse(emp);

    this.employeeService.getEmployeePicture(this.employee.id).subscribe(
      (res: any) => {
        if(res === null) {
          return;
        }
        let type = res.type;
        this.employee.photo = 'data:image/' + String(type) + ';base64,' + String(res.bytes);
      }
    );

    if(this.router.url === '/feed') {
      this.tabSelected[0] = true;
    }

  }

  // @HostListener('window:popstate', ['$event'])
  // onPopState(event: any) {
  //   event.preventDefault();
  //   this.router.navigate(['/feed']);
  // }

  public selectTab(tab: number) {
    for (let i = 0; i < this.tabSelected.length; i++) {
      this.tabSelected[i] = false;
    }
    this.tabSelected[tab] = true;
  }



}
