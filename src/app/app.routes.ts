import {Routes} from '@angular/router';

import {DashboardComponent} from "./dashboard/dashboard.component";
import {TasksComponent} from "./tasks/tasks.component";
import {LoginComponent} from "./login/login.component";
import {AuthenticationGuard} from "./security/authentication.guard";
import {OAuth2RedirectComponent} from "./security/oauth2-redirect.component";
import {RegisterComponent} from "./register/register.component";

export const routes: Routes = [
    {path: '', redirectTo: '/dashboard', pathMatch: 'full'},
    {path: 'login', component: LoginComponent},
    {path: 'register', component: RegisterComponent},
    {path: 'oauth2', component: OAuth2RedirectComponent},
    {path: 'dashboard', component: DashboardComponent, canActivate: [AuthenticationGuard]},
    {path: 'tasks', component: TasksComponent, canActivate: [AuthenticationGuard]},
];
