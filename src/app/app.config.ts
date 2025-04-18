import {ApplicationConfig, provideZoneChangeDetection} from '@angular/core';
import {provideRouter} from '@angular/router';
import {provideHttpClient, withFetch, withInterceptors} from "@angular/common/http";

import {routes} from './app.routes';
import {authenticationInterceptor} from "./security/authentication.interceptor";
import {provideToastr} from "ngx-toastr";
import {provideAnimationsAsync} from "@angular/platform-browser/animations/async";

export const appConfig: ApplicationConfig = {
    providers: [
        provideZoneChangeDetection({eventCoalescing: true}),
        provideRouter(routes),
        provideHttpClient(
            withFetch(),
            withInterceptors([authenticationInterceptor])
        ),
        provideAnimationsAsync(),
        provideToastr()
    ]
};
