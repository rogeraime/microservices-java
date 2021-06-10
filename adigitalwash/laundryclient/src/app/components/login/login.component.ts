import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { UserService } from '../../services/user/user.service';
import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AlertComponent } from '../alert/alert.component';
import { CookieService } from "ngx-cookie-service";

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.css'],
    encapsulation: ViewEncapsulation.None
})
export class LoginComponent implements OnInit {
    private returnUrl: string;
    public loginFormControl: FormGroup;
    constructor(
        private userService: UserService,
        private router: Router,
        private route: ActivatedRoute,
        private formBuilder: FormBuilder,
        private cookieService: CookieService
    ) {
        this.loginFormControl = this.formBuilder.group({
            username: ['', Validators.required],
            pwd: ['', Validators.required],
            rememberMe: [true, Validators.required]
        })
    }

    ngOnInit() {
        this.returnUrl = this.route.snapshot.queryParams['returnUrl'];
    }

    public submit(): void {
        this.userService.login(
            this.loginFormControl.get('username').value,
            this.loginFormControl.get('pwd').value,
            this.loginFormControl.get('rememberMe').value
        ).subscribe(
            this.redirectToLandingpage,
            () => {
                AlertComponent.show.next("login");
            });
    }

    private redirectToLandingpage = () => {
        if (this.returnUrl == null) {
            //location.href = (this.userService.isAdmin() ? '/admin' : '/laundry');
            location.reload();
        }
        else {
            location.href = this.returnUrl;
            this.returnUrl = null;
        }
    }


}
