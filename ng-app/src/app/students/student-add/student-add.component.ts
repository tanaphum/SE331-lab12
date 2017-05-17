import {Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {Student} from '../student';
import {Router} from "@angular/router";
import {StudentsDataService} from "../../service/students-data.service";


@Component({
  selector: 'app-student-add',
  templateUrl: './student-add.component.html',
  styleUrls: ['./student-add.component.css']
})
export class StudentAddComponent implements OnInit {

  user:any = {};
  roles:Array<string> = ['Admin','User'];
 public refreshValue(value:any):void {
   this.user.role = value.text;
   }


  student: any = {};

  constructor(private studentDataService: StudentsDataService, private router: Router) {
  };

  ngOnInit() {
    this.student = new Student();
  }

  upQuantity(student: Student) {
    student.penAmount++;
  }

  downQuantity(student: Student) {
    if (student.penAmount > 0)
      student.penAmount--;
  }
  @ViewChild('fileInput') inputEl: ElementRef;
  addStudent(student: Student) {
    console.log('dsfdsf');
    let result: Student;
    let inputEl: HTMLInputElement = this.inputEl.nativeElement;

    this.studentDataService.addStudentWihtAuthen(student,inputEl.files.item(0), this.user)
      .subscribe(resultStudent => {
        result = resultStudent
        if (result != null){
          this.router.navigate(['/list']);
        }else{
          alert("Error in adding the student");
        }
      });
  }

  onFileChange(event, student: any) {
    var filename = event.target.files[0].name;
    console.log(filename);
    student.image = filename;
    student.file = event.target.files[0];
  }

  isValid():boolean{
    if (!this.user.role)
      return false;
    return true;
  }


}
