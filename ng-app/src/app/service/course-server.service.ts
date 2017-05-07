import { Injectable } from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {Course} from '../students/course';
import {Http,Headers, RequestOptions} from '@angular/http';
import {AuthenticationService} from './authentication.service';

@Injectable()
export class CourseServerService {

  constructor(private http: Http, private authenticationService: AuthenticationService) {
  }

  private headers = new Headers({
    'Content-type': 'application/json',
    'Authorization': 'Bearer ' + this.authenticationService.getToken()
  });
  getCourse():Observable<Course[]>{

    return this.http.get('http://localhost:8080/course',({headers:this.headers}))
      .map(res => res.json());
  }

  addCourse(course:Course){
    let headers = new Headers({'Content-Type': 'application/json'});
    let options = new RequestOptions({headers: this.headers});
    let body = JSON.stringify(course);
    return this.http.post('http://localhost:8080/course', body, options)
      .map(res => {
        return res.json()
      })
      .catch((error: any) => {
        return Observable.throw(new Error(error.status))
      })

  }
}
