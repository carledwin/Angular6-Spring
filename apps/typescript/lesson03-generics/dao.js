"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var Dao = /** @class */ (function () {
    function Dao() {
    }
    Dao.prototype.insert = function (object) {
        console.log('inserting...');
        return true;
        //throw new Error("Method not implemented.");
    };
    Dao.prototype.update = function (object) {
        return true;
        //throw new Error("Method not implemented.");
    };
    Dao.prototype.delete = function (id) {
        return true;
        //throw new Error("Method not implemented.");
    };
    Dao.prototype.find = function (id) {
        return null;
        //throw new Error("Method not implemented.");
    };
    Dao.prototype.findAll = function () {
        return [null];
        //throw new Error("Method not implemented.");
    };
    return Dao;
}());
exports.Dao = Dao;
//# sourceMappingURL=dao.js.map