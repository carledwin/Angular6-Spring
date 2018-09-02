"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var person_1 = require("./../lesson01-classes/person");
var PersonDao = /** @class */ (function () {
    function PersonDao() {
    }
    PersonDao.prototype.insert = function (person) {
        console.log('inserting...', person.toString());
        return true;
        //throw new Error("Method not implemented.");
    };
    PersonDao.prototype.update = function (person) {
        return true;
        //throw new Error("Method not implemented.");
    };
    PersonDao.prototype.delete = function (id) {
        return true;
        //throw new Error("Method not implemented.");
    };
    PersonDao.prototype.find = function (id) {
        return null;
        //throw new Error("Method not implemented.");
    };
    PersonDao.prototype.findAll = function () {
        return [new person_1.Person('carl')];
        //throw new Error("Method not implemented.");
    };
    return PersonDao;
}());
exports.PersonDao = PersonDao;
//# sourceMappingURL=person-dao.js.map