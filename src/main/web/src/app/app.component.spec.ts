import { TestBed, async } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { AppComponent } from './app.component';
import { HttpClientService } from './service/http-client.service';
import { Observable } from 'rxjs/Observable';
import { of } from 'rxjs';
import { HttpModule } from '@angular/http';
import { FormsModule }        from '@angular/forms';
import {NgForm} from '@angular/forms';

describe('AppComponent', () => {
  let fixture;
  let app;
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpModule,
        FormsModule
      ],
      providers: [HttpClientService],
      declarations: [
        AppComponent
      ],
    }).compileComponents();
    fixture = TestBed.createComponent(AppComponent);
    app = fixture.debugElement.componentInstance;
    app.httpClientService = TestBed.get(HttpClientService);


  }));

  it('should create the app', () => {
    expect(app).toBeTruthy();
  });

  it('post without username should set the error message', () => {
      app.post();
      expect(app.errorMessage).toEqual("Please enter a Poster's User Name");
  });

  it('post with username should load all posts', () => {
      app.userName = "test";
      spyOn(app.httpClientService, 'getAllPosts').and.callFake(()=>{
         return of(JSON.parse('[{"messageId":"1564272251536-69207586-6455-4894-806c-e87d3c6f1dea","message":"test post","city":"","cityDetails":"","userName":"test"}]'));
      });
      app.post();
      expect(app.responses[0].message).toEqual("test post");
  });

  it('post with username should set last username', () => {
      app.userName = "test";
      spyOn(app.httpClientService, 'getAllPosts').and.callFake(()=>{
         return of(JSON.parse('[{"messageId":"1564272251536-69207586-6455-4894-806c-e87d3c6f1dea","message":"test post","city":"","cityDetails":"","userName":"test"}]'));
      });
      app.post();
      expect(app.lastUserName).toEqual("test");
  });

  it('post with username and text should make call to post', () => {
      app.userName = "test";
      app.message = "test post";
      app.responses = [];
      spyOn(app.httpClientService, 'post').and.callFake(()=>{
         return of(JSON.parse('{"messageId":"1564272251536-69207586-6455-4894-806c-e87d3c6f1dea","message":"test post","city":"","cityDetails":"","userName":"test"}'));
      });
      app.post();
      expect(app.responses[0].message).toEqual("test post");
  });

  it('a second post call should add the post to the local cache of posts', () => {
      app.userName = "test";
      app.message = "test post";
      app.responses = [];
      spyOn(app.httpClientService, 'post').and.callFake(()=>{
         return of(JSON.parse('{"messageId":"1564272251536-69207586-6455-4894-806c-e87d3c6f1dea","message":"test post","city":"","cityDetails":"","userName":"test"}'));
      });
      app.post();
      app.message = "test post";
      app.post();
      expect(app.responses.length).toEqual(2);
  });

  it('a reply to a post should add reply after the post', () => {
      app.userName = "test";
      app.message = "test post";
      app.responses = [];
      spyOn(app.httpClientService, 'post').and.callFake(()=>{
         return of(JSON.parse('{"messageId":"1564272251536-69207586-6455-4894-806c-e87d3c6f1dea","message":"test post","city":"","cityDetails":"","userName":"test"}'));
      });
      app.post();
      app.message = "test reply";
      spyOn(app.httpClientService, 'replyToPost').and.callFake(()=>{
               return of(JSON.parse('{"messageId":"1564272251536-69207586-6455-4894-806c-e87d3c6f1dea","message":"test post","city":"","cityDetails":"","userName":"test"}'));
      });
      app.post();
      expect(app.responses.length).toEqual(2);
  });
});
