import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Employee } from '../_models/employee/employee';
import { EmployeeService } from '../_services/employee.service';
import * as globals from '../globals';


@Component({
  selector: 'app-register', // <app-register></app-register>
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  registerForm: FormGroup;
  employee: Employee = new Employee(0, "", "", "", "", "", "");
  profilePicture: File;
  emailAlreadyExists: boolean = false;

  constructor(private formBuilder: FormBuilder, private http:HttpClient, private employeeService: EmployeeService, 
    private router: Router) {
    this.registerForm = this.formBuilder.group({
      name: ['', [ Validators.required]],
      surname: ['', [ Validators.required]],
      email: ['', [ Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(16)]],
      confirmPassword: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(16)]],
      phone: ['', [Validators.pattern('^[0-9]{10}$')]],
      photo: ['']
    });
    
  }

  public onSubmit(): void {

    this.employee.name = this.registerForm.get('name')!.value;
    this.employee.surname = this.registerForm.get('surname')!.value;
    this.employee.email = this.registerForm.get('email')!.value;
    this.employee.password = this.registerForm.get('password')!.value;
    this.employee.phone = this.registerForm.get('phone')!.value;
    this.employee.photo = this.registerForm.get('photo')!.value;

    // Send employee's data to server with post request.
    let resp = this.employeeService.registerEmployee(this.employee, this.profilePicture);
    
    // Subscribe response.
    resp.subscribe(
      (emp) => {
        this.router.navigate(['feed']);
      },
      (err) => {
        if(err.status == globals.CONFLICT){
          this.emailAlreadyExists = true;
        }
      }
    );

    // Reset form data.
    this.registerForm.reset();

  }

  public control(name: string){
    return this.registerForm.get(name);
  }


  public processPhoto(event: Event){

    let target = event.target as HTMLInputElement;
    this.profilePicture = (target.files as FileList)[0];

  } 


  ngOnInit(): void {
  }
  
}
