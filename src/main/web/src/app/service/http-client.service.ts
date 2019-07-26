import {Injectable} from '@angular/core';
import {Http, Headers, Response, RequestOptions} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/map';

@Injectable({
  providedIn: 'root'
})
export class HttpClientService {

  baseUrl: string;
  headers = new Headers();
  requestOptions = new RequestOptions();

  constructor(private http: Http) {
    this.baseUrl = "http://localhost:8080/api/v1/";
  }

  post(userName, message) {
    var Url = "users/" + userName + "/post";
    return this.makeHttpPost(Url, message);
  }

  getAllPosts(userName) {
    var Url = "users/" + userName + "/posts";
    return this.http.get(this.baseUrl + Url).map((response: Response) => {
      return response.json();
    });
  }


  replyToPost(postId, message, replyUserName) {
    var Url = "posts/" + postId + "/reply" + (replyUserName ? "?replyUserName=" + replyUserName : "");

    return this.makeHttpPost(Url, message);
  }

  replyToReply(postId, replyId, message, replyUserName) {
    var Url = "posts/" + postId + "/" + replyId + "/reply" + (replyUserName ? "?replyUserName=" + replyUserName : "");

    return this.makeHttpPost(Url, message);
  }

  private makeHttpPost(Url, message) {
    var requestOptions = HttpClientService.getRequestOptions();
    return this.http.post(this.baseUrl + Url,
      message
      , requestOptions).map((response: Response) => {
      return response.json();
    })
  }

  static getRequestOptions() {
    var h1 = new Headers();
    h1.append('content-type', 'Application/Json');

    var requestOptions = new RequestOptions();
    requestOptions.headers = h1;
    return requestOptions;
  }
}
