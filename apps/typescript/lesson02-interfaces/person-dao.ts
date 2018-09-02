import { Person } from './../lesson01-classes/person';
import { DaoInterface } from './dao.interface';

export class PersonDao implements DaoInterface{
    
    tableName: string;    
    
    insert(person: Person): boolean {
        console.log('inserting...', person.toString());
        return true;
        //throw new Error("Method not implemented.");
    }
    
    update(person: Person): boolean {
        return true;
        //throw new Error("Method not implemented.");
    }
    
    delete(id: number): boolean {
        return true;
        //throw new Error("Method not implemented.");
    }
    
    find(id: number) {
        return null;
        //throw new Error("Method not implemented.");
    }
    
    findAll(): [any] {
        return [new Person('carl')];
        //throw new Error("Method not implemented.");
    }


} 