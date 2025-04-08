import {Component, OnInit} from "@angular/core";
import {ActivatedRoute, Router} from "@angular/router";

import {AuthenticationService} from "./authentication.service";
import {AuthenticationProvider} from "../model/model";

@Component({
    selector: 'app-oauth2-redirect-handler',
    template: ''
})
export class OAuth2RedirectComponent implements OnInit {

    provider!: AuthenticationProvider;
    token!: string;
    error!: string;

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private authenticationService: AuthenticationService,
    ) {
    }

    ngOnInit() {
        this.route.paramMap.subscribe(params => {
            this.provider = params.get('provider') as AuthenticationProvider;
        })

        this.route.queryParams.subscribe(params => {
            this.token = params['token'];
            this.error = params['error'];

            if (this.token) {
                this.authenticationService.setAuthentication(this.token);
                this.router.navigate(
                    ['/dashboard'],
                    {state: {from: this.router.routerState.snapshot.url}}
                )
            } else {
                console.error(this.error, 'Error!');
                this.router.navigate(
                    ['/login'],
                    {state: {from: this.router.routerState.snapshot.url, error: this.error}});
            }
        })
    }
}