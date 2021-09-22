import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { EmployeeService } from '../_services/employee.service';
import * as globals from '../globals'

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  employee: any;
  tabSelected: boolean[] = [false];

  constructor(private router: Router, private employeeService: EmployeeService) { }

  ngOnInit(): void {
    let emp = localStorage.getItem('employee') as string;
    this.employee = JSON.parse(emp);

    if(this.employee == null){
      return;
    }

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

    if(this.router.url === '/feed') {
      this.tabSelected[0] = true;
    }
    else if(this.router.url === '/network') {
      this.tabSelected[1] = true;
    }
    else if(this.router.url === '/jobs') {
      this.tabSelected[2] = true;
    }
    else if(this.router.url === '/messaging') {
      this.tabSelected[3] = true;
    }
    else if(this.router.url === '/notifications') {
      this.tabSelected[4] = true;
    }

  }

  public selectTab(tab: number) {
    for (let i = 0; i < this.tabSelected.length; i++) {
      this.tabSelected[i] = false;
    }
    this.tabSelected[tab] = true;
  }


  public onLogout() {
    this.employeeService.logoutEmployee();
  }

  public showDropdownMenu() {
    document.getElementById("dropdown-menu")?.classList.toggle('show');
  }

}
