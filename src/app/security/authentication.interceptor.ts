import {HttpInterceptorFn} from "@angular/common/http";
import {inject} from "@angular/core";
import {catchError, of, throwError} from "rxjs";

import {AuthenticationService} from "./authentication.service";
import {ToastrService} from "ngx-toastr";

export const authenticationInterceptor: HttpInterceptorFn = (req, next) => {

    const authenticationService = inject(AuthenticationService);
    const toastrService = inject(ToastrService);

    if (authenticationService.authenticated) {
        req = req.clone({
            headers: req.headers.set('Authorization', `Bearer ${authenticationService.accessToken}`),
        });
    }

    return next(req)
        .pipe(
            catchError(error => {
                console.info(`Interceptor failed with code ${error.status}`)
                if (error.status === 401) {
                    authenticationService.logout();
                    return of();
                }

                const errorMessage = JSON.stringify(error.error, null, '\t');
                toastrService
                    .error(errorMessage, "Error!").onHidden
                    .subscribe(() => {
                        authenticationService.logout();
                    });
                return throwError(() => error);
            })
        )
}