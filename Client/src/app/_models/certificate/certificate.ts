import * as globals from "../../globals"

export class Certificate {

    id: number;
    school: string;
    degree: string;
    field: string;
    startDate: string;
    endDate: string;
    grade: number;
    hidden: boolean;

    constructor(id: number, school: string, degree: string, field: string,
        startDate: string, endDate: string, grade: number, hidden: boolean) {
        this.id = id;
        this.school = school;
        this.degree = degree;
        this.field = field;
        this.startDate = startDate;
        this.endDate = endDate;
        this.grade = grade;
        this.hidden = hidden;
        
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
