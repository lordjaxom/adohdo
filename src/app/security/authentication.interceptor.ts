import {HttpErrorResponse, HttpHandlerFn, HttpInterceptorFn, HttpRequest} from "@angular/common/http";
import {inject} from "@angular/core";
import {catchError, of, throwError} from "rxjs";

import {AuthenticationService} from "./authentication.service";

export const authenticationInterceptor: HttpInterceptorFn = (req: HttpRequest<unknown>, next: HttpHandlerFn) => {
    const authenticationService = inject(AuthenticationService);

    if (authenticationService.authenticated) {
        req = req.clone({
            headers: req.headers.set('Authorization', `Bearer ${authenticationService.token}`),
        });
    }

    return next(req)
        .pipe(
            catchError((error: HttpErrorResponse) => {
                console.info(`Interceptor failed with code ${error.status}`)
                if (error.status === 401) {
                    authenticationService.logout();
                    return of();
                } else {
                    const errorMessage = JSON.stringify(error.error, null, '\t'); // TODO
                    return throwError(() => error);
                }
            })
        )
}