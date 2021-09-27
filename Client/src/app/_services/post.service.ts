import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Post } from '../_models/post/post';

@Injectable({
  providedIn: 'root'
})
export class PostService {

  private postURL: string = 'https://localhost:8080/post';

  constructor(private http: HttpClient, private router: Router) { }


  public addPost(post: Post, employee_id: number) {
    let param = new HttpParams()
      .append('id', employee_id);

      return this.http.post(this.postURL + '/add',  post, {params: param});
  }

  public getAllPosts() {
    return this.http.get(this.postURL + '/all');
  }

}
