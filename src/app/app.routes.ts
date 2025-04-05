import {Routes} from '@angular/router';

import {DashboardComponent} from "./dashboard/dashboard.component";
import {TasksComponent} from "./tasks/tasks.component";
import {LoginComponent} from "./login/login.component";
import {AuthenticationGuard} from "./security/authentication.guard";
import {OAuth2RedirectHandlerComponent} from "./security/oauth2-redirect-handler.component";

export const routes: Routes = [
    {path: '', redirectTo: '/login', pathMatch: 'full'},
    {path: 'login', component: LoginComponent},
    {path: 'oauth2/:provider/redirect', component: OAuth2RedirectHandlerComponent},
    {path: 'dashboard', component: DashboardComponent, canActivate: [AuthenticationGuard]},
    {path: 'tasks', component: TasksComponent, canActivate: [AuthenticationGuard]},
];
