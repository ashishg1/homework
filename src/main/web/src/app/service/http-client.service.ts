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
    this.baseUrl = "/api/v1/";
  }

  post(userName, message, city) {
    var Url = "users/" + userName + "/post";
    var json = JSON.stringify({message:message,city:city,userName:userName});
    return this.makeHttpPost(Url, json);
  }

  getAllPosts(userName) {
    var Url = "users/" + userName + "/posts";
    return this.http.get(this.baseUrl + Url).map((response: Response) => {
      return response.json();
    });
  }

  replyToPost(postId, message, replyUserName, city) {
    var Url = "posts/" + postId + "/reply"
    var json = JSON.stringify({message:message,userName:replyUserName,city:city});
    return this.makeHttpPost(Url, json);
  }

  replyToReply(postId, replyId, message, replyUserName, city) {
    var Url = "posts/" + postId + "/" + replyId + "/reply";
    var json = JSON.stringify({message:message,userName:replyUserName,city:city});
    return this.makeHttpPost(Url, json);
  }

  private makeHttpPost(Url, json) {
    var requestOptions = HttpClientService.getRequestOptions();
    return this.http.post(this.baseUrl + Url,
      json
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
