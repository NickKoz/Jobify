import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class ConnectionService {

  private connectionURL: string = 'https://localhost:8080/connection';

  constructor(private http: HttpClient, private router: Router) {}


  public getConnection(requester: number, receiver: number) {
    return this.http.get(this.connectionURL + '/employees/' + String(requester) + ',' + String(receiver));
  }


  public acceptConnection(receiver: number, requester: number) {
    
    let data = new HttpParams()
      .append('receiver', receiver)
      .append('requester', requester)

    return this.http.put(this.connectionURL + '/accept', data);
  }


  public deleteConnection(receiver: number, requester: number) {

    let data = new HttpParams()
      .append('receiver', receiver)
      .append('requester', requester)

    return this.http.put(this.connectionURL + '/delete', data);

  }

  public addPendingConnection(requester: number, receiver: number) {

    let data = new HttpParams()
      .append('requester', requester)
      .append('receiver', receiver)

    return this.http.post(this.connectionURL + '/add', data);
  }



}
