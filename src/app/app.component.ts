import {Component} from '@angular/core';
import {RouterOutlet} from "@angular/router";

import {NavigationComponent} from "./navigation/navigation.component";
import {AuthenticationService} from "./security/authentication.service";
import {NgIf} from "@angular/common";

@Component({
    selector: 'app-root',
    imports: [NavigationComponent, RouterOutlet, NgIf],
    templateUrl: './app.component.html',
    styleUrl: './app.component.scss'
})
export class AppComponent {

    constructor(
        private authenticationService: AuthenticationService
    ) {
    }

    get authenticated() {
        return this.authenticationService.authenticated
    }
}
