import { Component } from '@angular/core';
import { HttpClientService } from './service/http-client.service';
import {NgForm} from '@angular/forms';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'homework';
  userName:string;
   message:string;
   responses:Object[];

    constructor(private httpClientService:HttpClientService) {
     }

    ngOnInit() {
      this.responses = [];
    }

    post() {

      if (!this.message){
      this.httpClientService.getAllPosts(this.userName).subscribe(
              data => {
                console.log(data);
                this.responses = data;
              },
                error => { console.log(error);
              });
      } else {
      this.httpClientService.post(this.userName, this.message).subscribe(
        data => {
          console.log(data);
          this.responses.unshift(data);
        },
          error => { console.log(error);
        });
       }
      }

      replyToPost(messageId) {
        this.httpClientService.replyToPost(messageId, "woohoo").subscribe(
                data => {
                  console.log(data);
                },
                  error => { console.log(error);
                });
         this.httpClientService.getAllPosts(this.userName).subscribe(
               data => {
                 console.log(data);
                 this.responses = data;
               },
                 error => { console.log(error);
               });
      }
}
