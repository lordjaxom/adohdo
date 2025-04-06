import {Injectable} from "@angular/core";
import {Router} from "@angular/router";
import {jwtDecode} from "jwt-decode";

import {User, UserService} from "../user/user.service";

const ACCESS_TOKEN = "accessToken";

@Injectable({
    providedIn: 'root'
})
export class AuthenticationService {
    parsedToken: any;
    authenticated: boolean = false;
    currentUser?: User | null;

    constructor(
        private router: Router,
        private userService: UserService
    )  {
        const token = this.getToken();
        if (token !== null && this.parsedToken == null) {
            this.parsedToken = jwtDecode(token);
        }
    }

    getToken() {
        return localStorage.getItem(ACCESS_TOKEN);
    }

    setAuthentication(accessToken: string) {
        localStorage.setItem(ACCESS_TOKEN, accessToken);
        this.authenticated = true;

        this.userService.getUser().subscribe(user => this.currentUser = user);
    }

    logout() {
        localStorage.removeItem(ACCESS_TOKEN);
        this.authenticated = false;
        this.currentUser = null;
        this.router.navigate(['/login']);
    }
}