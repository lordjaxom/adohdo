import {HttpErrorResponse, HttpHandlerFn, HttpInterceptorFn, HttpRequest} from "@angular/common/http";
import {inject} from "@angular/core";
import {catchError, throwError} from "rxjs";

import {AuthenticationService} from "./authentication.service";

export const authenticationInterceptor: HttpInterceptorFn = (req: HttpRequest<unknown>, next: HttpHandlerFn) => {
    const authenticationService = inject(AuthenticationService);

    const authToken = authenticationService.getToken();

    if (authToken) {
        req = req.clone({
            setHeaders: {
                ["Authorization"]: `Bearer ${authToken}`,
            },
        });
    }

    return next(req)
        .pipe(
            catchError((error: HttpErrorResponse) => {
                if (error.status === 403) {
                    authenticationService.logout();
                }

                const errorMessage = JSON.stringify(error.error, null, '\t'); // TODO
                return throwError(() => error);
            })
        )
}