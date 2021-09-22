import { Certificate } from '../certificate/certificate';
import { Job } from '../job/job';


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
