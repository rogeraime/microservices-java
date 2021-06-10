import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { UserService } from '../user/user.service';
import { CookieService } from 'ngx-cookie-service';

@Injectable()
/**
 * Middleware used to protect the routes from unauthorized access.
 */
export class AuthGuard implements CanActivate {

    constructor(
        private userService: UserService,
        private router: Router
    ) {

    }

    /**
     * Use in route configuration to protect routes containing
     * user data.
     * @param  {ActivatedRouteSnapshot} next  
     * @param  {RouterStateSnapshot}    state
     * @return {Observable}                   
     */
    canActivate(
        next: ActivatedRouteSnapshot,
        state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
        let url: string = state.url;
        if (!this.userService.logoutIfTokenExpired() && !this.checkUserPrivilege(next.data.expectedRole)) {
            // start a new navigation to redirect to login page
            this.router.navigate(['/login'], { queryParams: { returnUrl: url } });
            // abort current navigation

            return false;
        }
        else {
            // all ok, proceed navigation to routed component
            return true;
        }
    }


    /**
    * Check if user has a certain role
    * redirect to login form while storing
    * the attempted route.
    * @param  {string}  url url user tried to access
    * @return {boolean}     true if User had role, false if not
    */
    checkUserPrivilege(role: string): boolean {
        let authorities = this.userService.userGroupFromToken();
        if (authorities == null) {
            return false;
        }
        for (let r of authorities) {
            if (r.search(role) != -1) {
                return true;
            }
        }
        return false;
    }
}

@Injectable()
/**
 * Middleware used to protect the routes from unauthorized access.
 */
export class LoginGuard implements CanActivate {

    constructor(
        private userService: UserService,
        private router: Router,
        private cookieService: CookieService) { }

    /**
     * Use in route configuration to protect routes from loggen in Users
     * @param  {ActivatedRouteSnapshot} next  
     * @param  {RouterStateSnapshot}    state
     * @return {Observable}                   
     */
    canActivate(
        next: ActivatedRouteSnapshot,
        state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
        if (!this.userService.isLoggedIn()) {
            if (this.cookieService.check("refreshToken")) {
                this.userService.refresh().subscribe(() => {
                    this.toLaundryOrAdmin();
                    return false;
                }, () => { return true; });
            }
            return true;
        }
        else {
            this.toLaundryOrAdmin();
            return false;
        }
    }

    toLaundryOrAdmin() {
        if (this.userService.isAdmin()) {
            this.router.navigate(['/admin']);
        }
        else {
            this.router.navigate(['/laundry']);
        }
    }
}