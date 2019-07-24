import { Component, OnInit } from '@angular/core';
import { HttpClientService } from '../service/http-client.service';
import {NgForm} from '@angular/forms';

@Component({
  selector: 'app-message',
  templateUrl: './message.component.html',
  styleUrls: ['./message.component.css']
})

export class MessageComponent implements OnInit {

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
