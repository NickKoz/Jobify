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
        
      }

      public beautify() {
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
