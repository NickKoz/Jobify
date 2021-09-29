import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Comment } from '../_models/comment/comment';
import { Post } from '../_models/post/post';

@Injectable({
  providedIn: 'root'
})
export class PostService {

  private postURL: string = 'https://localhost:8080/post';

  constructor(private http: HttpClient, private router: Router) { }


  public addPost(post: Post, employee_id: number, file: File) {
    let param = new HttpParams()
      .append('id', employee_id);

    const postJson = JSON.stringify(post);
    const postData = new Blob([postJson], {
      type: 'application/json'
    });

    const formData = new FormData();

    formData.append('post', postData);

    if(file) {
      formData.append('file', file, file.name);
    }

    return this.http.post(this.postURL + '/add',  formData, {params: param});
  }


  public addCommentToPost(post_id: number, comment: Comment) {

    let param = new HttpParams()
      .append('post_id', post_id);

    return this.http.post(this.postURL + '/comment', comment, {params: param});
  }


  public addLikeToPost(post_id: number, employee_id: number) {
    let param = new HttpParams()
      .append('post_id', post_id)
      .append('employee_id', employee_id);

    return this.http.post(this.postURL + '/like', param);
  }


  public getAllPosts() {
    return this.http.get(this.postURL + '/all');
  }


  public getPostComments(post_id: number) {
    return this.http.get(this.postURL + '/' + String(post_id) + '/comments');
  }


  public getPostLikes(post_id: number) {
    return this.http.get(this.postURL + '/' + String(post_id) + '/likes');
  }

}
