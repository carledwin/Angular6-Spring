import { DaoInterface } from './dao.interface';

export class Dao<T> implements DaoInterface<T>{
    
    tableName: string;    
    
    insert(object: T): boolean {
        console.log('inserting...');
        return true;
        //throw new Error("Method not implemented.");
    }
    
    update(object: T): boolean {
        return true;
        //throw new Error("Method not implemented.");
    }
    
    delete(id: number): boolean {
        return true;
        //throw new Error("Method not implemented.");
    }
    
    find(id: number): T {
        return null;
        //throw new Error("Method not implemented.");
    }
    
    findAll(): [T] {
        return [null];
        //throw new Error("Method not implemented.");
    }


}