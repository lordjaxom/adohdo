import {Component, Inject, OnInit} from "@angular/core";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {FontAwesomeModule} from "@fortawesome/angular-fontawesome";
import {faGoogle, faGithub} from "@fortawesome/free-brands-svg-icons";

import {AuthenticationProvider} from "../model/model";
import {APP_BASE_HREF} from "@angular/common";

@Component({
    selector: 'app-login',
    imports: [FontAwesomeModule, ReactiveFormsModule],
    templateUrl: './login.component.html',
    styleUrl: './login.component.scss'
})
export class LoginComponent implements OnInit {
    faGoogle = faGoogle;
    faGithub = faGithub;

    loginForm!: FormGroup;
    loading = false;
    submitted = false;

    private readonly baseHref = "http://localhost:8080/browser"; // TODO

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

    loginLocally() {

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