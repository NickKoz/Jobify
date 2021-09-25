import * as globals from '../../globals'

export class Job {

    id: number;
    position: string;
    company: string;
    location: string;
    type: TypeOfEmployment;
    startDate: string;
    endDate: string;
    hidden: boolean;

    constructor(id: number, position: string, company: string, location: string, type: TypeOfEmployment,
        startDate: string, endDate: string, hidden: boolean) {
        this.id = id;
        this.position = position;
        this.company = company;
        this.location = location;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.hidden = hidden;
    }

    public beautify() {
      // if(this.type === TypeOfEmployment.FULL_TIME){
      //   this.type = 'Full-time'
      // }
      // else if(this.type === TypeOfEmployment.PART_TIME){
      //   this.type = 'Part-time'
      // }
      // else if(this.type === TypeOfEmployment.INTERNSHIP){
      //   this.type = 'Internship'
      // }
      // else if(this.type === TypeOfEmployment.VOLUNTEER){
      //   this.type = 'Volunteer'
      // }
      
      if(this.startDate != null) {
        let month = this.startDate.split('-')[1];
        let year = this.startDate.split('-')[2];
        this.startDate = ' ' + globals.findMonth(month) + ' ' + year;
      }
      
      if(this.endDate != null) {
        let month = this.endDate.split('-')[1];
        let year = this.endDate.split('-')[2];
        this.endDate = ' - ' + globals.findMonth(month) + ' ' + year;
      }
    }

}

export enum TypeOfEmployment {
  FULL_TIME = 'FULL_TIME',
  PART_TIME = 'PART_TIME',
  INTERNSHIP = 'INTERNSHIP',
  VOLUNTEER = 'VOLUNTEER',
}
