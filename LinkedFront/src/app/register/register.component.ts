import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';


@Component({
  selector: 'app-register', // <app-register></app-register>
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  registerForm: FormGroup

  constructor(private formBuilder: FormBuilder) {
    this.registerForm = this.formBuilder.group({
      name: formBuilder.control(null, [ Validators.required]),
      surname: formBuilder.control(null, [ Validators.required]),
      email: formBuilder.control(null, [ Validators.required, Validators.email]),
      password: formBuilder.control(null, [Validators.required, Validators.minLength(8), Validators.maxLength(16)]),
      confirmPassword: formBuilder.control(null, [Validators.required, Validators.minLength(8), Validators.maxLength(16)]),
      phone: formBuilder.control(null, [Validators.pattern('^[0-9]{10}$')]),
      photo: formBuilder.control(null),
    })
  }

  public onSubmit(): void {

    console.warn('You are now registered!', this.registerForm.value);
    this.registerForm.reset();

  }

  public control(name: string){
    return this.registerForm.get(name);
  }

  ngOnInit(): void {
  }
  
}
