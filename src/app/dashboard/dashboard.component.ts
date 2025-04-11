import {Component, OnInit} from "@angular/core";
import {Observable} from "rxjs";
import {ToastrService} from "ngx-toastr";

import {AuthenticationService} from "../security/authentication.service";
import {HttpHeaders} from "@angular/common/http";
import {SseClient} from "ngx-sse-client";

@Component({
    selector: "app-dashboard",
    templateUrl: "./dashboard.component.html"
})
export class DashboardComponent implements OnInit {

    private observable?: Observable<unknown>;

    constructor(
        private authenticationService: AuthenticationService,
        private toastrService: ToastrService,
        private sseClient: SseClient
    ) {
    }

    ngOnInit() {
        const headers = new HttpHeaders().set("Authorization", `Bearer ${this.authenticationService.accessToken}`)
        this.sseClient
            .stream("/api/events", {}, {headers})
            .subscribe(event => {
                const message = event as MessageEvent
                this.toastrService.info(message.data)
            })
    }
}