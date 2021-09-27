import { Component, HostListener, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Employee } from '../_models/employee/employee';
import { EmployeeService } from '../_services/employee.service';
import * as globals from '../globals'
import { PostService } from '../_services/post.service';
import { Post } from '../_models/post/post';

@Component({
  selector: 'app-feed',
  templateUrl: './feed.component.html',
  styleUrls: ['./feed.component.css']
})
export class FeedComponent implements OnInit {

  employee: any;
  postList: Post[];

  constructor(private router: Router, private employeeService: EmployeeService,
    private postService: PostService) { }

  
  // public onPost() {
  //   this.postService.addPost()
  // }

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

    this.postService.getAllPosts().subscribe(
      (resp: any) => {
        if(resp._embedded == null) {
          return;
        }

        this.postList = resp._embedded.postList;

      }
    );

  }


}
