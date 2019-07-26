import { Injectable } from '@angular/core';
import { Http, Headers, Response, RequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';

@Injectable({
  providedIn: 'root'
})
export class HttpClientService {
      headers = new Headers();
      requestOptions = new RequestOptions();
  constructor(private http:Http) {
 }
	post(userName,message){
	          var h1 = new Headers();
             h1.append('content-type', 'Application/Json');

             var requestOptions = new RequestOptions();
             requestOptions.headers = h1;

             var Url="http://localhost:8080/api/v1/users/"+ userName + "/post";
           

            return this.http.post(Url,
             message
             , requestOptions).map((response: Response) => {
                return response.json();
             })
	}

	getAllPosts(userName){
               var Url="http://localhost:8080/api/v1/users/"+ userName + "/posts";

              return this.http.get(Url).map((response: Response) => {
                  return response.json();
               });
  	}


  replyToPost(postId, message){
          var h1 = new Headers();
          h1.append('content-type', 'Application/Json');

          var requestOptions = new RequestOptions();
          requestOptions.headers = h1;

          var Url="http://localhost:8080/api/v1/posts/"+ postId + "/reply";


         return this.http.post(Url,
          message
          , requestOptions).map((response: Response) => {
             return response.json();
          })
    	}
}
