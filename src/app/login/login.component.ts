import {Component, Inject, OnInit} from "@angular/core";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {FontAwesomeModule} from "@fortawesome/angular-fontawesome";
import {faGoogle, faGithub} from "@fortawesome/free-brands-svg-icons";
import {ToastrService} from "ngx-toastr";

import {AuthenticationProvider} from "../model/model";

@Component({
    selector: 'app-login',
    imports: [FontAwesomeModule, ReactiveFormsModule],
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
    message = '';

    constructor(
        private formBuilder: FormBuilder,
        private toastrService: ToastrService
    ) {
    }

    ngOnInit(): void {
        this.loginForm = this.formBuilder.group({
            email: ['', Validators.required],
            password: ['', Validators.required],
        })
    }

    loginLocally() {
        this.error = false;
        this.submitted = false;
        this.toastrService.info("No Login yet", "Alarma")
    }

    loginWithProvider(provider: AuthenticationProvider) {
        window.location.href = `/oauth2/authorize/${provider}?redirect_uri=/browser/oauth2?provider=${provider}`
    }
}