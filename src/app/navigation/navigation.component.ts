import {Component, OnInit} from "@angular/core";
import {RouterLink, RouterLinkActive} from "@angular/router";

import {User, UserService} from "../user/user.service";
import {AuthenticationService} from "../security/authentication.service";

@Component({
    selector: "app-navigation",
    imports: [RouterLink, RouterLinkActive],
    templateUrl: "./navigation.component.html"
})
export class NavigationComponent implements OnInit {

    user_!: User;

    constructor(
        private userService: UserService,
        private authenticationService: AuthenticationService,
    ) {
    }

    ngOnInit() {
        this.userService.user.subscribe(user => this.user_ = user);
    }

    get user() {
        return this.user_;
    }

    logout() {
        this.authenticationService.logout();
    }
}