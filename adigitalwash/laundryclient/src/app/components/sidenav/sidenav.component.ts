import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user/user.service';
import { ClothCategoryModalComponent } from '../cloth-category-modal/cloth-category-modal.component'
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';

@Component({
  selector: 'app-sidenav',
  templateUrl: './sidenav.component.html',
  styleUrls: ['./sidenav.component.css']
})
export class SidenavComponent implements OnInit {
  title: string = 'Digiwash';
  logo: string = '../../assets/logo.jpg';
  username: string = this.userService.userFullNameFromToken();
  sendMail: boolean = true;
  on = "An";
  off = "Aus";

  navItems = [
    { name: "Eigene Wäscheliste", route: '/laundry' },
    { name: "Adminpanel", route: '/admin' },
    { name: "Logout", route: '/' }
  ];

  constructor(
    public userService: UserService,
    public modalService: NgbModal,
    private router: Router
  ) { }

  ngOnInit() {
    if(localStorage.getItem("sendMail") == "true") {
      this.sendMail = true;
    } else {
      this.sendMail = false;
    }
  }

  redirectToAdmin() {
    this.router.navigate(['/admin']);
  }

  redirectToLaundry() {
    this.router.navigate(['/laundry']);
  }

  toggleNotification() {
    this.sendMail = !this.sendMail;
    localStorage.setItem("sendMail", "" + this.sendMail);
  }

  openClothCategory() {
    if (!this.userService.logoutIfTokenExpired())
      this.modalService.open(ClothCategoryModalComponent);
  }
}
