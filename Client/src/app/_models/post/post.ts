import { Employee } from "../employee/employee";

export class Post {

    id: number;
    description: string;
    date: string;
    creator: Employee;
    comments: Comment[];
    employeeLikes: Employee[];

    constructor(id: number, description: string, date: string, creator: Employee) {
        this.id = id;
        this.description = description;
        this.date = date;
        this.creator = creator;
    }

}
