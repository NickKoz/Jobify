import * as globals from '../../globals'

export class Job {

    id: number;
    position: string;
    company: string;
    location: string;
    type: TypeOfEmployement;
    typeString: string;
    startDate: string;
    endDate: string;
    hidden: boolean;

    constructor(id: number, position: string, company: string, location: string, type: TypeOfEmployement,
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
        let month = this.startDate.split('-')[1];
        let year = this.startDate.split('-')[0];
        this.startDate = ' ' + globals.findMonth(month) + ' ' + year;
      }
      
      if(this.endDate != null) {
        let month = this.endDate.split('-')[1];
        let year = this.endDate.split('-')[0];
        this.endDate = ' - ' + globals.findMonth(month) + ' ' + year;
      }
    }

}

export enum TypeOfEmployement {
  FULL_TIME = 0,
  PART_TIME = 1,
  INTERNSHIP = 2,
  VOLUNTEER = 3
}
