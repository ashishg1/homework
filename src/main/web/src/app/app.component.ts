import {Component, ElementRef, ViewChild} from '@angular/core';
import {HttpClientService} from './service/http-client.service';
import {NgForm} from '@angular/forms';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'homework';
  lastUserName:string;
  userName: string;
  message: string;
  errorMessage: string;
  replyUserName: string;
  responses: Array<any>;
  city:string;

  constructor(private httpClientService: HttpClientService) {
  }

  ngOnInit() {
    this.responses = [];
    this.errorMessage = "";
  }

  post() {
    if (this.userName) {
      if (!this.lastUserName) {
        this.lastUserName = this.userName;
        this.loadAllPosts(true);
        return;
      }
      if (!this.message || this.lastUserName !== this.userName) {
        this.loadAllPosts(false);
      } else {
        this.httpClientService.post(this.userName, this.message, this.city).subscribe(
          data => {
            data.color = "#F0F8FF";
            this.responses.unshift(data);
          },
          error => {
            this.errorMessage = "Uh Oh! Seems like server had a failure";
            console.log(error);
          });
      }
      if (this.lastUserName === this.userName) {
        this.clear();
      }
      this.lastUserName = this.userName;
    } else {
      this.errorMessage = "Please enter a Poster's User Name";
    }
  }

  reply(messageId, rootMessageId) {
    var message = this.message;
    var localResponses = this.responses;
    if (this.message) {
      if (!rootMessageId) {
        this.httpClientService.replyToPost(messageId, message, this.replyUserName, this.city).subscribe(
          data => {
            var index = this.findIndexForPostReply(messageId, localResponses);
            data.color = "#F0F8FF";
            this.responses.splice(index + 1, 0, data);
          },
          error => {
            this.errorMessage = "Uh Oh! Seems like server had a failure";
            console.log(error);
          });
      } else {
        this.httpClientService.replyToReply(rootMessageId, messageId, message, this.replyUserName, this.city).subscribe(
          data => {
            var index = this.findIndexForReply(messageId, localResponses);
            data.color = "#F0F8FF";
            this.responses.splice(index + 1, 0, data);
          },
          error => {
            this.errorMessage = "Uh Oh! Seems like server had a failure";
            console.log(error);
          });
      }
      this.clear();
    } else {
      this.errorMessage = "Please enter a Reply Text";
    }
  }

  loadAllPosts(postAfterLoad) {
    this.httpClientService.getAllPosts(this.userName).subscribe(
      data => {
        this.responses = data;
        if (postAfterLoad) {
          this.post();
        }
      },
      error => {
        console.log(error);
      });
  }

  findIndexForPostReply(messageId, responses) {
    var index = -1;
    var nextIndex = responses.findIndex(function (item, i) {
      if (index > -1) {
        if (item.rootMessageId !== messageId) {
          return true;
        } else {
          index = i;
        }
      }
      if (item.messageId === messageId) {
        index = i;
      }
      return false;
    });
    return nextIndex > -1 ? nextIndex - 1 : index;
  }

  findIndexForReply(messageId, responses) {
    var index = -1;
    var depth = -1;
    var nextIndex = responses.findIndex(function (item, i) {
      if (index > -1) {
        if (!item.messageDepth || item.messageDepth < depth) {
          return true;
        } else {
          index = i;
        }
      }
      if (item.messageId === messageId) {
        index = i;
        depth = item.messageDepth + 1;
      }
      return false;
    });
    return nextIndex > -1 ? nextIndex - 1 : index;
  }

  clear() {
    this.message = "";
    this.errorMessage = "";
  }

}
