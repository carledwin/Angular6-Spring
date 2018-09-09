import { SharedService } from './../../services/shared.service';
import { HttpInterceptor, HttpHandler, HttpEvent, HttpRequest } from "@angular/common/http";

import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';

@Injectable()
export class AuthInterceptor implements HttpInterceptor{
   
    shared: SharedService;

    constructor(){
        this.shared = SharedService.getInstance();
    }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        
        let authRequest : any;

        if(this.shared.isLoggedIn()){
            authRequest = req.clone({
                setHeaders: {
                    'Authorization': this.shared.token
                }
            });

            console.log('clonou.....');
            return next.handle(authRequest);
        }else{
            console.log('passou sem clonar');
            return next.handle(req);
        }
    }

}