import { Person } from './../lesson01-classes/person';
import {Dao } from './dao';
import { DaoInterface } from './dao.interface';

let dao: DaoInterface<Person> = new Dao<Person>();

let person: Person = new Person('carl');

dao.insert(person);