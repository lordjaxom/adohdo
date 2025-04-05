import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

export interface User {
    name: string;
}

@Injectable({
    providedIn: 'root'
})
export class UserService {

    private userUrl = 'api/user';

    constructor(private http: HttpClient) {
    }

    getUser(): Observable<User> {
        return this.http.get<User>(this.userUrl);
    }
}