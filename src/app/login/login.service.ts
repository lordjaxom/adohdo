import {Injectable} from "@angular/core";
import {HttpBackend, HttpClient} from "@angular/common/http";

export interface LoginRequest {
    email: string;
    password: string;
}

export interface LoginResponse {
    token: string;
}

@Injectable({
    providedIn: 'root'
})
export class LoginService {

    private baseUrl = "/api/login"
    private http: HttpClient; // non-intercepting

    constructor(
        private httpBackend: HttpBackend
    ) {
        this.http = new HttpClient(httpBackend);
    }

    login(value: LoginRequest) {
        return this.http.post<LoginResponse>(this.baseUrl, value);
    }
}