import { ResponseApi } from './../../model/response-api.model';
import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NgForm } from '@angular/forms';
import { User } from '../../model/user.model';
import { SharedService } from '../../services/shared.service';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-user-new',
  templateUrl: './user-new.component.html',
  styleUrls: ['./user-new.component.css']
})
export class UserNewComponent implements OnInit {

  @ViewChild("form")
  form: NgForm

  user = new User('','','','');
  shared: SharedService;
  message: {};
  classCss: {};

  constructor(
    private userService: UserService,
    private route: ActivatedRoute) {
      this.shared = SharedService.getInstance();
    }

  ngOnInit() {
    // este id sera capturado do parametro do navigate
    let id: string = this.route.snapshot.params['id'];

    if(id != undefined){
      this.findById(id);
    }
  }

  findById(id: string){
    this.userService.findById(id).subscribe((responseApi: ResponseApi) => {
      this.user = responseApi.data;
      this.user.password = '';
      }
      , error =>{
        this.showMessage({
          type: 'error',
          text: error['error']['errors'][0]
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

  register(){
    this.message ={};
    this.userService.createOrUpdate(this.user).subscribe((responseApi: ResponseApi) => {
        this.user = new User('', '', '', '');
        let userResponse: User = responseApi.data;
        this.form.resetForm();
        this.showMessage({
          type: 'success',
          text: `Registered ${userResponse.email} succesfully`
        });
      }, error =>{
        this.showMessage({
          type: 'error',
          text: ['error']['errors'][0]
        });
      });
  }

  getFormGroupClass(isInvalid: boolean, isDirty):{}{
    return {
      'form-group': true,
      'has-error': isInvalid && isDirty,
      'has-success':!isInvalid && isDirty
    };
  }
}
