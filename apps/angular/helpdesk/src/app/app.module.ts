import { AuthGuard } from './components/security/auth.guard';
import {FormsModule} from '@angular/forms';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { SharedService } from './services/shared.service';
import { UserService } from './services/user.service';
import { routes } from './api.routes';
import { AppComponent } from './app.component';
import { AuthInterceptor } from './components/security/auth.interceptor';
import { HeaderComponent } from './components/header/header.component';
import { MenuComponent } from './components/menu/menu.component';
import { FooterComponent } from './components/footer/footer.component';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/security/login/login.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    MenuComponent,
    FooterComponent,
    HomeComponent,
    LoginComponent
  ],
  imports: [
    BrowserModule,
    routes,
    HttpClientModule,
    FormsModule
  ],
  providers: [
    UserService,
    SharedService,
    AuthGuard,
   {
     provide: HTTP_INTERCEPTORS,
     useClass: AuthInterceptor,
     multi: true
   }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
