import {NgIf} from "@angular/common";
import {Component, OnInit} from "@angular/core";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {Router, RouterLink} from "@angular/router";
import {FontAwesomeModule} from "@fortawesome/angular-fontawesome";
import {faGithub, faGoogle} from "@fortawesome/free-brands-svg-icons";

import {AuthenticationProvider} from "../model/model";
import {LoginService} from "./login.service";
import {AuthenticationService} from "../security/authentication.service";

@Component({
    selector: 'app-login',
    imports: [FontAwesomeModule, ReactiveFormsModule, NgIf, RouterLink],
    templateUrl: './login.component.html',
    styleUrl: './login.component.scss'
})
export class LoginComponent implements OnInit {
    readonly faGoogle = faGoogle;
    readonly faGithub = faGithub;

    readonly prGoogle = AuthenticationProvider.google
    readonly prGithub = AuthenticationProvider.github

    loginForm!: FormGroup;

    loading = false;
    submitted = false;
    error = false;

    constructor(
        private loginService: LoginService,
        private authenticationService: AuthenticationService,
        private router: Router,
        private formBuilder: FormBuilder,
    ) {
    }

    ngOnInit() {
        this.loginForm = this.formBuilder.group({
            email: ['', [Validators.required, Validators.email]],
            password: ['', [Validators.required]],
        });
    }

    loginLocally() {
        this.error = false;
        this.submitted = false;

        if (!this.loginForm.valid) {
            return;
        }

        this.loading = true;

        console.info("Logging in locally")
        this.loginService
            .login(this.loginForm.value)
            .subscribe({
                next: data => {
                    this.authenticationService.authenticate(data.token)
                    this.router.navigate(['/dashboard'], {state: {from: this.router.routerState.snapshot.url}})
                },
                error: error => {
                    console.info(`Login failed with status ${error.status}`);
                    this.error = true;
                    this.loading = false;
                }
            })
    }

    loginWithProvider(provider: AuthenticationProvider) {
        window.location.href = `/oauth2/authorize/${provider}?redirect_uri=/browser/oauth2?provider=${provider}`
    }
}