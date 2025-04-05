import {Component, OnInit} from "@angular/core";
import {NgIf} from "@angular/common";

import {User, UserService} from "../user/user.service";
import {RouterLink, RouterLinkActive} from "@angular/router";
import {AuthenticationService} from "../security/authentication.service";

@Component({
    selector: "app-navigation",
    imports: [NgIf, RouterLink, RouterLinkActive],
    templateUrl: "./navigation.component.html"
})
export class NavigationComponent implements OnInit {
    user: User | undefined;

    constructor(
        private userService: UserService,
        private authenticationService: AuthenticationService,
    ) {
    }

    ngOnInit(): void {
        // this.userService.getUser().subscribe(user => {
        //     this.user = user;
        // })
    }

    get authenticated() {
        return this.authenticationService.authenticated
    }
}