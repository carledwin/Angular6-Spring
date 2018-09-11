import { ResponseApi } from './../../model/response-api.model';
import { SharedService } from './../../services/shared.service';
import { TicketService } from './../../services/ticket.service';
import { Summary } from './../../model/summary.model';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-summary',
  templateUrl: './summary.component.html',
  styleUrls: ['./summary.component.css']
})
export class SummaryComponent implements OnInit {

  summary: Summary = new Summary();
  shared: SharedService;
  message: {};
  classCss: {};

  constructor(
    private ticketServce: TicketService
  ) { 
    this.shared = SharedService.getInstance();
  }

  ngOnInit() {
    this.ticketServce.summery().subscribe((responseApi: ResponseApi) => {
      this.summary = responseApi.data;
    }, err => {
      this.showMessage({
        type: 'error',
        text: err['error']['errors'][0]
      });
    });
  }

  private showMessage(message: {type: string, text: string}): void{
    
    this.message = message;
    this.buildClasses(message.type);
    
    setTimeout(() => {
      this.message = undefined;
    }, 3000);
  }

  private buildClasses(type: string): void{
    this.classCss = {
      'alert': true
    }

    this.classCss['alert-'+type] = true;
  }

}
