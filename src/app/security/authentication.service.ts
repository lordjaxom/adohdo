import {Injectable} from "@angular/core";
import {Router} from "@angular/router";

const ACCESS_TOKEN = "accessToken";

@Injectable({
    providedIn: 'root'
})
export class AuthenticationService {
    private authenticated_ = false;

    constructor(
        private router: Router
    ) {
        console.info("Constructing AuthenticationService");
        const token = this.token;
        if (token !== null) {
            console.info("Found stored token")
            this.authenticate(token);
        }
    }

    get authenticated() {
        return this.authenticated_
    }

    get token() {
        return localStorage.getItem(ACCESS_TOKEN);
    }

    authenticate(accessToken: string) {
        localStorage.setItem(ACCESS_TOKEN, accessToken);
        this.authenticated_ = true;
        console.info("authentication set successfully")
    }

    logout() {
        console.info("Logging out");
        localStorage.removeItem(ACCESS_TOKEN);

        this.authenticated_ = false;
        this.router.navigate(['/login']);
    }
}