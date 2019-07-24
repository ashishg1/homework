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
	saveMessage(message){
	          var h1 = new Headers();
             h1.append('content-type', 'Application/Json');

             var requestOptions = new RequestOptions();
             requestOptions.headers = h1;
             
             // Here you need to enter Url of your REST API for Post action
             var Url="http://localhost:8080/api/v1/clone";
           

            return this.http.post(Url, message, requestOptions)
                  .map((response: Response) => response.json())
	}
}