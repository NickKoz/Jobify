import { Certificate } from '../certificate/certificate';
import { Job } from '../job/job';
import { Post } from '../post/post';


export class Employee {
    
    id: number;
    name: string;
    surname: string;
    email: string;
    password: string;
    jobTitle: string;
    phone: string;
    photo: string;
    jobs: Job[] = [];
    certificates: Certificate[] = [];
    skills: String[] = [];
    connections: Employee[] = [];
    posts: Post[] = [];


    constructor(id: number, name: string, surname: string, email: string, password: string, 
        jobTitle: string, phone: string, photo: string) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.jobTitle = jobTitle;
        this.phone = phone;
        this.photo = photo;
    }

}
