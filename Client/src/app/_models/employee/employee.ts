import { Certificate } from '../certificate/certificate';
import { Comment } from '../comment/comment';
import { Job } from '../job/job';
import { JobAd } from '../jobAd/job-ad';
import { Message } from '../message/message';
import { Post } from '../post/post';


export class Employee {
    
    id: number;
    name: string;
    surname: string;
    email: string;
    password: string;
    phone: string;
    photo: string;
    jobs: Job[] = [];
    certificates: Certificate[] = [];
    skills: String[] = [];
    connections: Employee[] = [];
    posts: Post[] = [];
    jobAds: JobAd[] = [];
    likes: Post[] = [];
    comments: Comment[] = [];
    incomingMessages: Message[] = [];
    outgoingMessages: Message[] = [];
    chats: Employee[] = [];
    messages: Message[] = [];


    constructor(id: number, name: string, surname: string, email: string, password: string, 
        phone: string, photo: string) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.photo = photo;
    }

}
