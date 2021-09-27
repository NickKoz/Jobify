import { Employee } from "../employee/employee";

export class Comment {

    id: number;
    text: string;
    creator: Employee;

    constructor(id: number, text: string, creator: Employee) {
        this.id = id;
        this.text = text;
        this.creator = creator;
    }

}
