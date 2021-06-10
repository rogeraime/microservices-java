import { RouterModule, Routes, CanActivate } from '@angular/router';

import { LaundryDetailComponent } from './components/laundry-detail/laundry-detail.component';
import { AdminLaundryListComponent } from './components/admin-laundry-list/admin-laundry-list.component';
import { LaundryListComponent } from './components/laundry-list/laundry-list.component';
import { LoginComponent } from './components/login/login.component';
import { AuthGuard, LoginGuard } from './services/auth/auth.guard';
import { SidenavComponent } from './components/sidenav/sidenav.component';

export const appRoutes: Routes = [{
        path: 'admin',
        component: AdminLaundryListComponent,
        canActivate: [AuthGuard],
        data: {
        expectedRole: 'ROLE_ADMIN' }
    },
    {
        path: 'laundry',
        component: LaundryListComponent,
        canActivate: [AuthGuard],
        data: {
        expectedRole: 'ROLE_USER'}
    },
    {
        path: 'login',
        component: LoginComponent,
        canActivate: [LoginGuard],
    },
    //Defaultpath
    { path: "**", redirectTo: 'login', pathMatch: 'full' }
];
