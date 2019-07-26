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
    }

    saveMessage() {
      this.httpClientService.saveMessage(this.userName, this.message).subscribe(
        data => {
          console.log(data);
          this.responses = data;
        },
          error => { console.log(error);
        })
      }
}
