import { Component, ViewEncapsulation } from '@angular/core';
import { SidenavComponent } from './components/sidenav/sidenav.component';
import { UserService } from './services/user/user.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class AppComponent {
  constructor(
      private userService: UserService
  ){  }
  title: string = 'Digiwash';
  logo: string = '../../assets/logo.jpg';
  username: string = this.userService.userFullNameFromToken();
  roles: string = this.userService.userGroupFromToken();

  ngOnInit() {
    if(localStorage.getItem("sendMail") == undefined || localStorage.getItem("sendMail") == null) {
      localStorage.setItem("sendMail", "true");
    }
  }
}
