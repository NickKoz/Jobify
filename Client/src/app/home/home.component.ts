import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AdminService } from '../_services/admin.service';
import { EmployeeService } from '../_services/employee.service';
import * as globals from '../globals';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  loginForm: FormGroup;
  tempEmployee: any;

  emailNotFound: boolean = false;
  wrongPasswd: boolean = false;

  constructor(private formBuilder: FormBuilder, private employeeService: EmployeeService, 
    private router: Router, private adminService: AdminService) {
    this.loginForm = this.formBuilder.group({
      email: formBuilder.control(null, [Validators.required]),
      password: formBuilder.control(null, [Validators.required])
    })
   }

  public onSubmit(): void {

    let email = this.loginForm.get('email')?.value;
    let password = this.loginForm.get('password')?.value;

    if(email === globals.ADMIN_EMAIL){
      let resp = this.adminService.loginAdmin(email, password);
      resp.subscribe(
        (adm) => {
          this.router.navigate(['admin']);
        },
        (err) => {
          if(err.status == globals.EMAIL_NOT_FOUND) {
            this.emailNotFound = true;
          }
          else if(err.status == globals.WRONG_PASSWD) {
            this.wrongPasswd = true;
          }
        }

      );
    }
    else{
      let resp = this.employeeService.loginEmployee(email, password);
      resp.subscribe( 
        (emp) => {
          this.router.navigate(['feed']);
        }, 
        (err) => {
          if(err.status == globals.EMAIL_NOT_FOUND) {
            this.emailNotFound = true;
          }
          else if(err.status == globals.WRONG_PASSWD) {
            this.wrongPasswd = true;
          }
        }
      );
    }

    this.loginForm.reset();    

  }

  // Used for UI purposes - user is notified if their email/password is invalid.
  public tryEmail(): boolean {

    this.emailNotFound = false;
    this.wrongPasswd = false;
    return true;
  }



  ngOnInit(): void {
  }

}
