import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";

@Injectable({
    providedIn: 'root'
})
export class LoginService {

    private baseUrl = "/api/login"

    constructor(
        private http: HttpClient
    ) {
    }

    login(value: any) {
        this.http.post<String>(this.baseUrl, value).subscribe(next => console.info(next))
    }
}