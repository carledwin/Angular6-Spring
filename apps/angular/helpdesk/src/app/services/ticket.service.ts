import { Ticket } from './../model/ticket.model';
import { HELP_DESC_API } from './helpdesk.api';
import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import { tick } from '@angular/core/testing';

@Injectable({
  providedIn: 'root'
})
export class TicketService {

  constructor(private httpClient: HttpClient) { }

  createOrUpdate(ticket: Ticket){

    if(ticket.id != null && ticket.id != ''){
      return this.httpClient.put(`${HELP_DESC_API}/api/ticket`, ticket);
    }else{

      ticket.id = null;
      ticket.status = 'New';
      return this.httpClient.post(`${HELP_DESC_API}/api/ticket`, ticket);
    }
  }

  findAll(page: number, count: number){
    return this.httpClient.get(`${HELP_DESC_API}/api/ticket/${page}/${count}`);
  }

  findById(id: string){
    return this.httpClient.get(`${HELP_DESC_API}/api/ticket/${id}`);
  }

  deleteById(id: string){
    return this.httpClient.delete(`${HELP_DESC_API}/api/ticket/${id}`);
  }

  findByParameters(page: number, count: number, assignedToMe: string, ticket: Ticket){
    ticket.number = ticket.number == null ? 0 : ticket.number;
    ticket.title = ticket.title == '' ? 'uninformed' : ticket.title;
    ticket.status = ticket.status == '' ? 'uninformed' : ticket.status;
    ticket.priority = ticket.priority == '' ? 'priority' : ticket.priority;
    
    return this.httpClient.get(`${HELP_DESC_API}/api/ticket/${page}/${count}/${ticket.number}/${ticket.title}/${ticket.status}/${ticket.priority}/${assignedToMe}`);
  }

  changeStatus(status: string, ticket: Ticket){
    return this.httpClient.put(`${HELP_DESC_API}/api/ticket/${ticket.id}/${status}`, ticket);
  }

  summery(){
    return this.httpClient.get(`${HELP_DESC_API}/api/ticket/summary`);
  }
}
