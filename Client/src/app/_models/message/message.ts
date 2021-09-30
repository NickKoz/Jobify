import { Employee } from "../employee/employee";

export class Message {

    id: number;
    timeSent: string;
    text: string;
    sender: Employee;
    receiver: Employee;

    constructor(id: number, timeSent: string ,text: string, sender: Employee, receiver: Employee){
        this.id = id;
        this.timeSent = timeSent;
        this.text = text;
        this.sender = sender;
        this.receiver = receiver;
    }

}
