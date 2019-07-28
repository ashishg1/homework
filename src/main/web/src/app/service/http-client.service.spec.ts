import { TestBed } from '@angular/core/testing';
import { HttpClientService } from './http-client.service';
import { Observable } from 'rxjs/Observable';
import { of } from 'rxjs';
import { HttpModule } from '@angular/http';

describe('HttpClientService', () => {
  let httpClientService:HttpClientService;
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
            HttpModule
        ],
      providers: [HttpClientService]
    });
    httpClientService = TestBed.get(HttpClientService);
  });

  it('should be created', () => {
    expect(httpClientService).toBeTruthy();
  });
});
