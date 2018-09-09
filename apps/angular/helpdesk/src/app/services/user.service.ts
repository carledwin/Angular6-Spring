import { HELP_DESC_API } from './helpdesk.api';
import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import { User } from '../model/user.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private httpClient: HttpClient) { }

  login(user: User){
    return this.httpClient.post(`${HELP_DESC_API}/api/auth`, user);
  }

  createOrUpdate(user: User){

    if(user.id != null && user.id != ''){
      return this.httpClient.put(`${HELP_DESC_API}/api/user`, user);
    }else{
      user.id = null;
      return this.httpClient.post(`${HELP_DESC_API}/api/user`, user);
    }
  }

  findAll(page: number, count: number){
    return this.httpClient.get(`${HELP_DESC_API}/api/user/${page}/${count}`);
  }

  findById(id: string){
    return this.httpClient.get(`${HELP_DESC_API}/${id}`);
  }

  deleteById(id: string){
    return this.httpClient.delete(`${HELP_DESC_API}/${id}`);
  }
}
