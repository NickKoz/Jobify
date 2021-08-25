import { HttpClient } from '@angular/common/http';
import { HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Employee } from '../employee';
import { EmployeeService } from '../employee.service';


@Component({
  selector: 'app-register', // <app-register></app-register>
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  registerForm: FormGroup;
  employee: Employee = new Employee("", "", "", "", "", "", "");
  profilePicture: File;

  constructor(private formBuilder: FormBuilder, private http:HttpClient, private employeeService: EmployeeService) {
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
    this.employee.confirmPassword = this.registerForm.get('confirmPassword')!.value;
    this.employee.phone = this.registerForm.get('phone')!.value;
    this.employee.photo = this.registerForm.get('photo')!.value;

    // console.log(JSON.stringify(this.employee));

    // Send employee's data to server with post request.
    let resp = this.employeeService.registerEmployee(this.employee, this.profilePicture);
    
    // Subscribe response.
    resp.subscribe((data) => {console.log("Data: " + JSON.stringify(data));});

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
