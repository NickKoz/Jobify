import * as globals from '../../globals'

export class Job {

    id: number;
    position: string;
    company: string;
    location: string;
    type: string;
    startDate: string;
    endDate: string;
    hidden: boolean;

    constructor(id: number, position: string, company: string, location: string, type: string,
        startDate: string, endDate: string, hidden: boolean) {
        this.id = id;
        this.position = position;
        this.company = company;
        this.location = location;
        this.hidden = hidden;

        if(type === TypeOfEmployment.FULL_TIME){
          this.type = 'Full-time'
        }
        else if(type === TypeOfEmployment.PART_TIME){
          this.type = 'Part-time'
        }
        else if(type === TypeOfEmployment.INTERNSHIP){
          this.type = 'Internship'
        }
        else if(type === TypeOfEmployment.VOLUNTEER){
          this.type = 'Volunteer'
        }
        
        if(startDate != null) {
          let month = startDate.split('-')[1];
          let year = startDate.split('-')[2];
          this.startDate = ' ' + globals.findMonth(month) + ' ' + year;
        }
        
        if(endDate != null) {
          let month = endDate.split('-')[1];
          let year = endDate.split('-')[2];
          this.endDate = ' - ' + globals.findMonth(month) + ' ' + year;
        }


    }
}

enum TypeOfEmployment {
  FULL_TIME = 'FULL_TIME',
  PART_TIME = 'PART_TIME',
  INTERNSHIP = 'INTERNSHIP',
  VOLUNTEER = 'VOLUNTEER',
}
