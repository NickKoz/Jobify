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
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    RouterModule.forRoot([
      {
        path: '', 
        component: HomeComponent
      },
      {
        path: 'register', 
        component: RegisterComponent
      },
      {
        path: '404', 
        component: NotFoundComponent
      },
      {
        path: 'feed',
        component: FeedComponent
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
        ]
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
