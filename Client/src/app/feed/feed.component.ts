import { Component, HostListener, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Employee } from '../_models/employee/employee';
import { EmployeeService } from '../_services/employee.service';
import * as globals from '../globals'
import { PostService } from '../_services/post.service';
import { Post } from '../_models/post/post';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { DatePipe } from '@angular/common';
import { Comment } from '../_models/comment/comment';

@Component({
  selector: 'app-feed',
  templateUrl: './feed.component.html',
  styleUrls: ['./feed.component.css']
})
export class FeedComponent implements OnInit {

  employee: Employee;
  postList: Post[];
  postFile: File;
  postForm: FormGroup;
  commentForm: FormGroup;

  constructor(private router: Router, private employeeService: EmployeeService,
    private postService: PostService, private formBuilder: FormBuilder, private datePipe: DatePipe) { 

      this.postForm = this.formBuilder.group({
        text: ['', [Validators.required]]
        
      });

      this.commentForm = this.formBuilder.group({
        text: ['', [Validators.required]]
      });

  }

  public submitPost() {
    console.log(this.postForm.get('text')!.value);
    
    let date = this.datePipe.transform(new Date(), 'yyyy-MM-dd') as string;

    let post = new Post(0, this.postForm.get('text')!.value, date, this.employee);

    this.postService.addPost(post, this.employee.id, this.postFile).subscribe();
  }

  public submitComment(post_id: number) {
    console.log(this.commentForm.get('text')!.value, post_id);

    let comment = new Comment(0, this.commentForm.get('text')!.value, this.employee);

    this.postService.addCommentToPost(post_id, comment).subscribe();
  }


  public processFile(event: Event){

    let target = event.target as HTMLInputElement;
    this.postFile = (target.files as FileList)[0];

  } 

  public handleLike(post_id: number) {

    this.postService.addLikeToPost(post_id, this.employee.id).subscribe();

    window.location.reload();
  }

  


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

        console.log(this.postList);

        for(let post of this.postList) {
          this.employeeService.getEmployeePicture(post.creator.id).subscribe(
            (resp: any) => {
              if(resp === null) {
                post.creator.photo = globals.blankPicture;
                return;
              }
              let type = resp.type;
              post.creator.photo = 'data:image/' + String(type) + ';base64,' + String(resp.bytes);
            }
          );

          this.postService.getPostComments(post.id).subscribe(
            (comments: any) => {
              if(comments === null) {
                return;
              }

              post.comments = comments;
            }
          );

          this.postService.getPostLikes(post.id).subscribe(
            (likes: any) => {
              if(likes._embedded == null) {
                return;
              }

              post.likes = likes._embedded.employeeList;

              if(post.likes.some( emp => emp['id'] === this.employee['id'] )) {
                post.liked = true;
              }
              else {
                post.liked = false;
              }

            }
          );

        }

      }
    );

  }


}
