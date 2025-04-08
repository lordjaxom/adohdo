import {Component, Inject, OnInit} from "@angular/core";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {FontAwesomeModule} from "@fortawesome/angular-fontawesome";
import {faGoogle, faGithub} from "@fortawesome/free-brands-svg-icons";

import {AuthenticationProvider} from "../model/model";

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

    readonly AuthenticationProvider = AuthenticationProvider

    constructor(
        private formBuilder: FormBuilder,
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
        window.location.href = `/oauth2/authorize/${provider}?redirect_uri=/browser/oauth2?provider=${provider}`
    }
}