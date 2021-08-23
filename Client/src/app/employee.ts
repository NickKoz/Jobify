

export class Employee {
    
    name: string;
    surname: string;
    email: string;
    password: string;
    confirmPassword: string;
    phone: string;
    photo: string;


    constructor(name: string, surname: string, email: string, password: string, phone: string, photo: string, confirmPassword: string) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.phone = phone;
        this.photo = photo;
    }

}
