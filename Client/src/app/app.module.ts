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
import { NetworkComponent } from './network/network.component'

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
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
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
        path: '**', 
        redirectTo: '404'
      },
    ])
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
