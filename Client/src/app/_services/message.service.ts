import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class MessageService {

  private messageURL: string = 'https://localhost:8080/message';

  constructor(private http: HttpClient) { }

  public addMessage(sender_id: number, receiver_id: number, text: string) {

    let param = new HttpParams()
      .append('sender_id', sender_id)
      .append('receiver_id', receiver_id)
      .append('text', text);

    return this.http.post(this.messageURL + '/add', param);
  }

}
