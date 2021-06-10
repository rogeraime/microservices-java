import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { RouterModule, Routes, CanActivate } from '@angular/router';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { AppComponent } from './app.component';
import { CreateLaundryComponent } from './components/laundry-create/laundry-create.component';
import { LaundryListComponent } from './components/laundry-list/laundry-list.component';
import { LaundryService } from './services/laundry/laundry.service';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { ModalModule } from 'ngx-bootstrap';
import { TooltipModule } from 'ngx-bootstrap';
import { DeleteLaundryComponent } from './components/laundry-delete/laundry-delete.component';
import { LaundryDetailComponent } from './components/laundry-detail/laundry-detail.component';
import { AdminLaundryListComponent } from './components/admin-laundry-list/admin-laundry-list.component';
import { CustomDateParserFormatter } from './components/util/datepicker-formatter';
import { AdminLaundryService } from './services/laundry/admin-laundry.service';
import { LoginComponent } from './components/login/login.component';
import { ClothCategoryComponent } from './components/cloth-category/cloth-category.component';
import { AuthGuard, LoginGuard } from './services/auth/auth.guard';
import { UserService } from './services/user/user.service';
import { OrderModule } from 'ngx-order-pipe';
import { CategoryService } from './services/category/category.service';

import { CookieService } from 'ngx-cookie-service';

// Routes in different file
import { appRoutes } from './routes';

// Angular Material 
import { MatSidenavModule } from '@angular/material';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatListModule } from '@angular/material/list';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatButtonModule } from '@angular/material/button';
import { SidenavComponent } from './components/sidenav/sidenav.component';
import { MatInputModule } from '@angular/material/input';
import { FlexLayoutModule } from "@angular/flex-layout";
import { MatDividerModule } from '@angular/material/divider';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatTabsModule } from '@angular/material/tabs';
import { MatDatepickerModule, MatNativeDateModule } from '@angular/material';

import { NgbModule, NgbActiveModal, NgbDateParserFormatter } from '@ng-bootstrap/ng-bootstrap';
import { ModalComponent } from './components/modal/modal.component';
import { LaundryCreateModalComponent } from './components/laundry-create-modal/laundry-create-modal.component';
import { LaundryDeleteModalComponent } from './components/laundry-delete-modal/laundry-delete-modal.component';
import { LaundryDetailModalComponent } from './components/laundry-detail-modal/laundry-detail-modal.component';
import { ClothCategoryModalComponent } from './components/cloth-category-modal/cloth-category-modal.component';
import { LaundryEditModalComponent } from './components/laundry-edit-modal/laundry-edit-modal.component';
import { AlertComponent } from './components/alert/alert.component';
import { SafePipe } from './pipe';

@NgModule({
    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        FormsModule,
        ReactiveFormsModule,
        OrderModule,
        ModalModule.forRoot(),
        TooltipModule.forRoot(),
        RouterModule.forRoot(appRoutes, { enableTracing: true }),
        HttpModule,
        MatSidenavModule,
        MatIconModule,
        MatCardModule,
        MatListModule,
        MatGridListModule,
        MatButtonModule,
        MatInputModule,
        FlexLayoutModule,
        MatDividerModule,
        MatCheckboxModule,
        MatTabsModule,
        MatDatepickerModule,
        MatNativeDateModule,
        NgbModule.forRoot()
    ],
    declarations: [
        AppComponent,
        CreateLaundryComponent,
        ClothCategoryComponent,
        LaundryListComponent,
        DeleteLaundryComponent,
        LaundryDetailComponent,
        AdminLaundryListComponent,
        LoginComponent,
        SidenavComponent,
        ClothCategoryComponent,
        ModalComponent,
        LaundryCreateModalComponent,
        LaundryDeleteModalComponent,
        LaundryDetailModalComponent,
        ClothCategoryModalComponent,
        LaundryEditModalComponent,
        AlertComponent,
        SafePipe
    ],
    providers: [
        LaundryService,
        AdminLaundryService,
        CategoryService,
        UserService,
        AuthGuard,
        LoginGuard,
        NgbActiveModal,
        CookieService, { provide: NgbDateParserFormatter, useClass: CustomDateParserFormatter }
    ],
    bootstrap: [AppComponent],
    entryComponents: [
        LaundryCreateModalComponent,
        LaundryDeleteModalComponent,
        LaundryDetailModalComponent,
        LaundryEditModalComponent,
        ClothCategoryModalComponent
    ]
})
export class AppModule { }
