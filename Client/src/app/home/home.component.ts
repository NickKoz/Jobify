import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { EmployeeService } from '../employee/employee.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  loginForm: FormGroup
  tempEmployee: any

  constructor(private formBuilder: FormBuilder, private employeeService: EmployeeService, 
    private router: Router) {
    this.loginForm = this.formBuilder.group({
      email: formBuilder.control(null, [Validators.required]),
      password: formBuilder.control(null, [Validators.required])
    })
   }

  public onSubmit(): void {

    let email = this.loginForm.get('email')?.value;
    let password = this.loginForm.get('password')?.value;

    let resp = this.employeeService.loginEmployee(email, password);
    resp.subscribe( 
      (emp) => {console.log(JSON.stringify(emp));
        this.router.navigate(['feed']);
      }
      , (err) => console.log(err)
    );


    this.loginForm.reset();

  }

  public control(name: string){
    return this.loginForm.get(name);
  }


  ngOnInit(): void {
  }

}
