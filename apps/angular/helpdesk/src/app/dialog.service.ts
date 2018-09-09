import { Injectable } from "@angular/core";

@Injectable()
export class DialogService{
    confirm(message?: string){
        return new Promise(resolve =>{
            //se vier preenchido exibe a mensagem do contrario exibe a mensagem padrao
            return resolve(window.confirm(message || 'Confirm ?'));
        });
    }
}