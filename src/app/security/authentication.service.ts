import {Injectable} from "@angular/core";
import {Router} from "@angular/router";

const ACCESS_TOKEN = "accessToken";

@Injectable({
    providedIn: 'root'
})
export class AuthenticationService {

    authenticated = this.accessToken !== null;

    constructor(
        private router: Router
    ) {
    }

    get accessToken() {
        return localStorage.getItem(ACCESS_TOKEN);
    }

    authenticate(accessToken: string) {
        localStorage.setItem(ACCESS_TOKEN, accessToken);
        this.authenticated = true;
    }

    logout() {
        localStorage.removeItem(ACCESS_TOKEN);

        this.authenticated = false;
        this.router.navigate(['/login']);
    }
}