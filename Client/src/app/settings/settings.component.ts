import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { EmployeeService } from '../_services/employee.service';
import * as globals from '../globals'

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit {

  settingsForm: FormGroup;
  employeeID: number;
  newEmail: string;
  newPassword: string;
  emailAlreadyExists: boolean = false;
  successfulSave: boolean = false;
  

  constructor(private formBuilder: FormBuilder, private http: HttpClient,
    private router: Router, private employeeService: EmployeeService) { 

      this.settingsForm = this.formBuilder.group({
        newΕmail: ['', [Validators.email]],
        newPassword: ['', [Validators.minLength(8), Validators.maxLength(16)]]
      });

  }

  public onSubmit(): void {
    
    if(this.settingsForm.get('newΕmail') === null) {
      this.newEmail = null as any;
    }
    else {
      this.newEmail = this.settingsForm.get('newΕmail')!.value;
    }
    
    if(this.settingsForm.get('newPassword') === null) {
      this.newPassword = null as any;
    }
    else {
      this.newPassword = this.settingsForm.get('newPassword')!.value;
    }

    let emp = localStorage.getItem('employee') as string;
    this.employeeID = JSON.parse(emp).id;

    this.employeeService.updateEmployee(this.employeeID, this.newEmail, this.newPassword).subscribe(
      (resp) => {
        this.emailAlreadyExists = false;
        this.successfulSave = true;
      },
      (err) => {
        if(err.status === globals.CONFLICT) {
          this.emailAlreadyExists = true;
        }
        this.successfulSave = false;
      }
    );


    this.settingsForm.reset();
  
  }

  ngOnInit(): void {
  }


}
