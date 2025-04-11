import {Component, OnInit} from "@angular/core";
import {NgIf} from "@angular/common";
import {AbstractControl, FormBuilder, FormGroup, ReactiveFormsModule, ValidationErrors, ValidatorFn, Validators} from "@angular/forms";
import {RouterLink} from "@angular/router";

import {RegisterService} from "./register.service";

const confirmPassword: ValidatorFn = (
    control: AbstractControl
): ValidationErrors | null => {
    console.info(`confirmPasswordValidator: ${JSON.stringify(control.errors)}`);
    return control.value.password === control.value.password2
        ? null
        : { confirmPassword: true };
};

const strongPassword: RegExp = /^(?=[^A-Z]*[A-Z])(?=[^a-z]*[a-z])(?=\D*\d).{8,}$/;

@Component({
    selector: 'app-register',
    imports: [NgIf, ReactiveFormsModule, RouterLink],
    templateUrl: './register.component.html'
})
export class RegisterComponent implements OnInit {

    registerForm!: FormGroup;

    loading = false;
    submitted = false;
    error = false;
    success = false;
    errorMessage = '';

    constructor(
        private registerService: RegisterService,
        private formBuilder: FormBuilder,
    ) {
    }

    ngOnInit() {
        this.registerForm = this.formBuilder.group({
                email: ['', [Validators.required, Validators.email]],
                password: ['', [Validators.required, Validators.pattern(strongPassword)]],
                password2: [''],
                name: ['', Validators.required],
        });
        this.registerForm.addValidators(confirmPassword)
    }

    register() {
        this.error = false;
        this.success = false;
        this.submitted = false;

        if (!this.registerForm.valid) {
            return;
        }

        this.loading = true;
        this.registerService
            .register(this.registerForm.value)
            .subscribe({
                next: () => {
                    this.success = true;
                },
                error: error => {
                    this.error = true;
                    this.loading = false;
                    this.errorMessage = error.error;
                }
            })
    }
}