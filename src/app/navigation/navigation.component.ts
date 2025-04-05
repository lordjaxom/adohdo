import {Component, OnInit} from "@angular/core";
import {NgIf} from "@angular/common";

import {User, UserService} from "../user/user.service";
import {RouterLink, RouterLinkActive} from "@angular/router";

@Component({
    selector: "app-navigation",
    imports: [NgIf, RouterLink, RouterLinkActive],
    templateUrl: "./navigation.component.html"
})
export class NavigationComponent implements OnInit {
    user: User | undefined;

    constructor(
        private userService: UserService
    ) {
    }

    ngOnInit(): void {
        this.userService.getUser().subscribe(user => {
            this.user = user;
        })
    }
}