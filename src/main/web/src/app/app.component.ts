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
   message:string;
   response:string;

    constructor(private httpClientService:HttpClientService) {
     }

    ngOnInit() {
    }

    saveMessage() {
      console.log("Submitting to service: " + this.message);
      this.httpClientService.saveMessage(this.message).subscribe(
        data => { console.log(data);this.response = data;
        },
        error => { console.log(error);
        })
        }
}
