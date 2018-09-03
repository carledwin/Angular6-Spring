import { Component } from '@angular/core';
import { User } from './user';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Tasks ';
  upperText: string = 'Display uppercase text';
  lowerText: string = 'Display lowercase text';
  percentValue: number = 50.07;
  date: Date = new Date();
  money: number = 4343.32;
  profile: number = 22;
  user: User = {
    name: 'carl',
    age: 35
  }
}
