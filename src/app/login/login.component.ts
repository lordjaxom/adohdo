import {Component, Inject, OnInit} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

import {AuthenticationProvider} from "../model/model";
import {APP_BASE_HREF} from "@angular/common";

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
})
export class LoginComponent implements OnInit {
    loginForm!: FormGroup;

    private readonly baseHref = "http://localhost:8080/browser";

    constructor(
        private formBuilder: FormBuilder,
        //@Inject(APP_BASE_HREF) private baseHref: string,
    ) {
    }

    ngOnInit(): void {
        this.loginForm = this.formBuilder.group({
            email: ['', Validators.required],
            password: ['', Validators.required],
        })
    }

    onSubmit() {

    }

    loginWithProvider(provider: AuthenticationProvider) {
        switch (provider) {
            case AuthenticationProvider.google:
                window.location.href = `/oauth2/authorize/google?redirect_uri=${this.baseHref}/oauth2/google/redirect`;
                break;
            case AuthenticationProvider.github:
                window.location.href = `/oauth2/authorize/github?redirect_uri=${this.baseHref}/oauth2/github/redirect`;
                break;
        }
    }

    protected readonly AuthenticationProvider = AuthenticationProvider;
}