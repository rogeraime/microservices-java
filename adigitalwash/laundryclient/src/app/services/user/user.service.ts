import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Http, Response, Headers, RequestOptionsArgs } from '@angular/http';
import { JwtHelper } from 'angular2-jwt';
import { CookieService } from 'ngx-cookie-service';
import { Router } from '@angular/router';

@Injectable()
export class UserService {
    private jwtHelper: JwtHelper = new JwtHelper();
    constructor(
        private http: Http,
        private cookieService: CookieService,
        private router: Router
    ) { }

    redirectUrl: string = "/";

    /**
     * Send login query to authservice.
     * @param  {string}              username username
     * @param  {string}              password password
     * @return {Observable<boolean>}          Observable containing the server response
     */
    login(username: string, password: string, rememberMe: boolean): Observable<boolean> {
        let url = '/oauth/token?grant_type=password&username=' + username + '&password=' + password;
        return this.loginOrRefresh(url, rememberMe);
    }

    refresh(): Observable<boolean> {
        let url = '/oauth/token?grant_type=refresh_token&refresh_token=' + this.cookieService.get("refreshToken");
        return this.loginOrRefresh(url, true);
    }

    loginOrRefresh(url: string, rememberMe?: boolean): Observable<boolean> {
        //TODO Client Signing On Gateway
        return this.http.post(url, '', { withCredentials: true })
            .map((response: Response) => {
                let JSONResponse = response.json();
                let tokenString = JSONResponse && JSONResponse.access_token;
                if (tokenString) {
                    let expiresIn = JSONResponse.expires_in;
                    this.cookieService.set("accessToken", tokenString, new Date(Date.now() + expiresIn * 1000));
                    if (rememberMe) this.cookieService.set("refreshToken", JSONResponse.refresh_token, 30);
                    
                    return true;
                } else {
                    return false;
                }
            });
    }

    userFullNameFromToken() {
        var tokenString = this.cookieService.get('accessToken');
        if (tokenString) {
            let token = this.jwtHelper.decodeToken(tokenString);
            return token.user_full_name;
        } else {
            return null;
        }
    }

    userNameFromToken() {
        var tokenString = this.cookieService.get('accessToken');
        if (tokenString) {
            let token = this.jwtHelper.decodeToken(tokenString);
            return token.user_name;
        } else {
            return null;
        }
    }

    userEmailFromToken() {
        var tokenString = this.cookieService.get('accessToken');
        if (tokenString) {
            let token = this.jwtHelper.decodeToken(tokenString);
            return token.user_eMail;
        } else {
            return null;
        }
    }

    userGroupFromToken() {
        var tokenString = this.cookieService.get('accessToken');
        if (tokenString) {
            let token = this.jwtHelper.decodeToken(tokenString);
            return token.authorities;
        } else {
            return null;
        }
    }

    expireTimeFromToken() {
        var tokenString = this.cookieService.get('accessToken');
        if (tokenString) {
            let token = this.jwtHelper.decodeToken(tokenString);
            return token.expires_in;
        } else {
            return null;
        }
    }

    /**
     * loggs a user out by clearing the local storage
     */
    public logout(keepRefreshToken?: boolean): void {
        let sendMail = localStorage.getItem("sendMail");
        localStorage.clear();
        if (!keepRefreshToken) this.cookieService.deleteAll();
        localStorage.setItem("sendMail", sendMail);
        this.router.navigate(['/']);
    }

    /**
     * Check if user is logged in. This has to be verified by calling the auth service
     * or by checking the validity of the user token.
     * @return {boolean} whether a user is Logged in
     */
    public isLoggedIn(): boolean {
        var tokenString = this.cookieService.get("accessToken");
        if (tokenString) {
            return true;
        }
        return false;
    }

    /** 
     * Logs a user out if the authentification token is expired
     * @return {boolean} true if token is expired
    */
    public logoutIfTokenExpired(): boolean {
        let tokenString = this.cookieService.get("accessToken");
        if (tokenString){
            return false;
        }else{
            this.logout(true);
            return true;
        }
        
    }

    public isUser() {
        let roles: String = String(this.userGroupFromToken());
        if (roles.search("ROLE_USER") != -1) {
            return true;
        } else {
            return false;
        }
    }

    public isAdmin() {
        let roles: String = String(this.userGroupFromToken());
        if (roles.search("ROLE_ADMIN") != -1) {
            return true;
        } else {
            return false;
        }
    }

    public getHeaders(): RequestOptionsArgs {
        let headers = new Headers({ 'Content-Type': 'application/json', 'Authorization': 'bearer ' + this.cookieService.get("accessToken") });
        return { headers: headers };
    }
}