import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  loginForm: FormGroup

  constructor(private formBuilder: FormBuilder) {
    this.loginForm = this.formBuilder.group({
      email: formBuilder.control(null, [Validators.required]),
      password: formBuilder.control(null, [Validators.required])
    })
   }

  public onSubmit(): void {

    console.warn('You are now registered!', this.loginForm.value);
    this.loginForm.reset();

  }

  public control(name: string){
    return this.loginForm.get(name);
  }


  ngOnInit(): void {
  }

}
