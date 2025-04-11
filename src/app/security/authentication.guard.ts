import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from "@angular/router";

import {AuthenticationService} from "./authentication.service";

@Injectable({
    providedIn: 'root'
})
export class AuthenticationGuard implements CanActivate {

    constructor(
        private router: Router,
        private authenticationService: AuthenticationService
    ) {
    }

    canActivate(_route: ActivatedRouteSnapshot, _state: RouterStateSnapshot): boolean {
        console.info("AuthGuard: checking authentication");
        if (this.authenticationService.authenticated) {
            return true;
        }

        console.info("AuthGuard: redirecting to login")
        this.router.navigate(['/login']);
        return false;
    }
}