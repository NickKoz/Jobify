import * as globals from '../../globals';
import { Employee } from '../employee/employee';
import { TypeOfEmployement } from '../job/job';

export class JobAd {

    id: number;
    position: string;
    company: string;
    location: string;
    type: TypeOfEmployement;
    typeString: string;
    startDate: string;
    creator: Employee;
    applicants: Employee[];

    constructor(id: number, position: string, company: string, location: string, type: TypeOfEmployement,
        startDate: string, creator: Employee) {
        this.id = id;
        this.position = position;
        this.company = company;
        this.location = location;
        this.type = type;
        this.startDate = startDate;
        this.creator = creator;
    }

    public beautify() {

        if(this.type === TypeOfEmployement.FULL_TIME){
          this.typeString = 'Full-time';
        }
        else if(this.type === TypeOfEmployement.PART_TIME){
          this.typeString = 'Part-time';
        }
        else if(this.type === TypeOfEmployement.INTERNSHIP){
          this.typeString = 'Internship';
        }
        else if(this.type === TypeOfEmployement.VOLUNTEER){
          this.typeString = 'Volunteer';
        }
        
        if(this.startDate != null) {
          let day = this.startDate.split('-')[2];
          let month = this.startDate.split('-')[1];
          let year = this.startDate.split('-')[0];
          this.startDate =  day + ' ' + globals.findMonth(month) + ' ' + year;
        }
        
      }

}
