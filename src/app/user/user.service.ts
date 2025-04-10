import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";

export interface User {
    name: string;
}

@Injectable({
    providedIn: 'root'
})
export class UserService {

    private userUrl = '/api/user';

    constructor(
        private http: HttpClient
    ) {
    }

    get user() {
        return this.http.get<User>(this.userUrl);
    }
}