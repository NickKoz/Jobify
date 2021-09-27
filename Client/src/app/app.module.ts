import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { RegisterComponent } from './register/register.component';
import { HomeComponent } from './home/home.component';
import { RouterModule } from '@angular/router';
import { HeaderComponent } from './header/header.component';
import { FooterComponent } from './footer/footer.component';
import { NotFoundComponent } from './not-found/not-found.component';
import { FeedComponent } from './feed/feed.component';
import { AdministrationComponent } from './administration/administration.component';
import { EmployeeDetailsComponent } from './administration/employee-details/employee-details.component';
import { AuthGuard } from './_auth/auth.guard';
import { EmployeeProfileComponent } from './employee-profile/employee-profile.component';
import { NetworkComponent } from './network/network.component';
import { SettingsComponent } from './settings/settings.component';
import { NotificationsComponent } from './notifications/notifications.component'
import { DatePipe } from '@angular/common';
import {MatButtonModule} from '@angular/material/button';
import { JobsComponent } from './jobs/jobs.component';
import { JobsDetailsComponent } from './jobs/jobs-details/jobs-details.component';

@NgModule({
  declarations: [
    AppComponent,
    RegisterComponent,
    HomeComponent,
    HeaderComponent,
    FooterComponent,
    NotFoundComponent,
    FeedComponent,
    AdministrationComponent,
    EmployeeDetailsComponent,
    EmployeeProfileComponent,
    NetworkComponent,
    SettingsComponent,
    NotificationsComponent,
    JobsComponent,
    JobsDetailsComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    MatButtonModule,
    RouterModule.forRoot([
      {
        path: '', 
        component: HomeComponent,
        canActivate: [AuthGuard]
      },
      {
        path: 'register', 
        component: RegisterComponent,
        canActivate: [AuthGuard]
      },
      {
        path: '404', 
        component: NotFoundComponent
      },
      {
        path: 'feed',
        component: FeedComponent,
        canActivate: [AuthGuard]
      },
      {
        path: 'network',
        component: NetworkComponent,
        canActivate: [AuthGuard]
      },
      {
        path: 'jobs',
        children: [
          {
            path: '',
            component: JobsComponent
          },
          {
            path: ':id',
            component: JobsDetailsComponent
          }
        ],
        canActivate: [AuthGuard]
      },
      {
        path: 'notifications',
        component: NotificationsComponent,
        canActivate: [AuthGuard]
      },
      {
        path: 'admin',
        children: [
          {
            path: '',
            component: AdministrationComponent
          },
          {
            path: ':id',
            component: EmployeeDetailsComponent
          }
        ],
        canActivate: [AuthGuard]
      },
      {
        path: 'profile/:id',
        component: EmployeeProfileComponent,
        canActivate: [AuthGuard]
      },
      {
        path: 'settings',
        component: SettingsComponent,
        canActivate: [AuthGuard]
      },
      {
        path: '**', 
        redirectTo: '404'
      },
    ])
  ],
  providers: [DatePipe],
  bootstrap: [AppComponent]
})
export class AppModule { }
