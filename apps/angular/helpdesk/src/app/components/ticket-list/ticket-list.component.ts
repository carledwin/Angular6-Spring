import { ResponseApi } from './../../model/response-api.model';
import { DialogService } from './../../dialog.service';
import { Component, OnInit } from '@angular/core';
import { SharedService } from '../../services/shared.service';
import { Ticket } from '../../model/ticket.model';
import { TicketService } from '../../services/ticket.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-ticket-list',
  templateUrl: './ticket-list.component.html',
  styleUrls: ['./ticket-list.component.css']
})
export class TicketListComponent implements OnInit {

  assignedToMe: boolean = false;
  page: number=0;
  count: number=5;
  pages: Array<number>;
  shared: SharedService;
  message: {};
  classCss: {};
  listTicket = [];
  ticketFilter = new Ticket('', null, '', '', '', '', null, null, '', null);
  

  constructor(
    private dialogService: DialogService,
    private ticketService: TicketService,
    private router: Router
  ) { 
    this.shared = SharedService.getInstance();
  }

  ngOnInit() {
    this.findAll(this.page, this.count);
  }

  findByFilter(){
    this.page = 0;
    this.count = 5;
    this.ticketService.findByParameters(this.page, this.count, this.assignedToMe.toString(), this.ticketFilter)
                          .subscribe((responseApi: ResponseApi) => {
                              this.ticketFilter.number = this.ticketFilter.number == null ? 0 : this.ticketFilter.number;
                              this.listTicket = responseApi['data']['content'];
                              this.pages = new Array(responseApi['data']['totalPages']);
                          }, err => {
                            this.showMessage({
                              type: 'error',
                              text: err['error']['errors'][0]
                            });
                          });
  }


  findAll(page: number, count: number){
    this.ticketService.findAll(page, count).subscribe((responseApi: ResponseApi) => {
                                                      this.listTicket = responseApi['data']['content'];
                                                      this.pages = new Array(responseApi['data']['totalPages']);
                                                    }, err => {
                                                      this.showMessage({
                                                        type: 'error',
                                                        text: err['error']['errors'][0]
                                                      });
                                                    });
  }

  delete(id: string){
    this.dialogService.confirm('Dou tou want to delete the ticket?')
                      .then((canDelete: boolean) => {
                        
                        if(canDelete){
                          this.message ={};
                          this.ticketService.deleteById(id).subscribe((responseApi: ResponseApi) => {
                            this.showMessage({
                              type: 'success',
                              text: 'Record deleted'
                            });

                            this.findAll(this.page, this.count);
                          }, err => {
                            this.showMessage({
                              type: 'error',
                              text: err['error']['errors'][0]
                            });
                          });  
                        }
                      });
  }

  cleanFilter(){
    this.assignedToMe = false;
    this.page = 0;
    this.count = 5;
    this.ticketFilter = new Ticket('', null, '', '', '', '', null, null, '', null);
    this.findAll(this.page, this.count);
  }

  edit(id: string){
    this.router.navigate(['/ticket-new', id]);
  }

  detail(id: string){
    this.router.navigate(['/ticket-detail', id]);
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

  setNextPage(event: any){
    
    event.preventDefaults();

    if(this.page + 1 < this.pages.length){
      this.page = this.page + 1;
      this.findAll(this.page, this.count);
    }
  }

  setPreviewPage(event: any){
    
    event.preventDefaults();

    if(this.page > 0){
      this.page = this.page - 1;
      this.findAll(this.page, this.count);
    }
  }

  setPage(i, event: any){
   event.preventDefaults();
   this.page = i;
   this.findAll(this.page, this.count);
  }

}
