import { Comment } from "../comment/comment";
import { Employee } from "../employee/employee";

export class Post {

    id: number;
    description: string;
    date: string;
    file: any;
    creator: Employee;
    photo: any;
    comments: Comment[];
    likes: Employee[];
    liked: boolean;

    constructor(id: number, description: string, date: string, creator: Employee) {
        this.id = id;
        this.description = description;
        this.date = date;
        this.creator = creator;
    }

}
