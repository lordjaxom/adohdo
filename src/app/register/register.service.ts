import {Injectable} from "@angular/core";
import {HttpBackend, HttpClient} from "@angular/common/http";

export interface RegisterRequest {
    email: string;
    password: string;
}

export interface RegisterResponse {
}

@Injectable({
    providedIn: 'root'
})
export class RegisterService {

    private baseUrl = "/api/register"
    private http: HttpClient; // non-intercepting

    constructor(
        private httpBackend: HttpBackend
    ) {
        this.http = new HttpClient(httpBackend);
    }

    register(value: RegisterRequest) {
        return this.http.put<void>(this.baseUrl, value);
    }
}