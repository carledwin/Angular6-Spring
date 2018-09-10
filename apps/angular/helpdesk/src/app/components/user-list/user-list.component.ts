import { ResponseApi } from './../../model/response-api.model';
import { SharedService } from './../../services/shared.service';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { DialogService } from '../../dialog.service';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {

  page: number= 0;
  count: number= 5;
  pages: Array<number>;
  shared: SharedService;
  message: {};
  classCss: {};
  listUser = [];

  constructor(
    private dialogService: DialogService,
    private userService: UserService,
    private router: Router
  ) { 
    this.shared = SharedService.getInstance();
  }

  ngOnInit() {
    this.findAll(this.page, this.count);
  }

  edit(id: string){
    this.router.navigate(['/user-new', id]);
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

  delete(id: string){
    this.dialogService.confirm('Dou tou want to delete the user?')
                      .then((canDelete: boolean) => {
                        
                        if(canDelete){
                          this.message ={};
                          this.userService.deleteById(id).subscribe((responseApi: ResponseApi) => {
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

  findAll(page: number, count: number){
    this.userService.findAll(page, count).subscribe((responseApi: ResponseApi) => {
                                                      this.listUser = responseApi['data']['content'];
                                                      this.pages = new Array(responseApi['data']['totalPages']);
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
