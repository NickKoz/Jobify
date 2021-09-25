

export const ADMIN_EMAIL: string = "admin@jobify.com";

export const EMAIL_NOT_FOUND: number = 404;

export const WRONG_PASSWD: number = 401;

export const CONFLICT: number = 409;

export const blankPicture: any = '../assets/Photos/blank-profile-picture.png';

export enum Months {
  JANUARY = '01',
  FEBRUARY = '02',
  MARCH = '03',
  APRIL = '04',
  MAY = '05',
  JUNE = '06',
  JULY = '07',
  AUGUST = '08',
  SEPTEMBER = '09',
  OCTOBER = '10',
  NOVEMBER = '11',
  DECEMBER = '12'
}

export function findMonth(month: string): string {
  if(month === Months.JANUARY){
    return 'January';
  }
  else if(month === Months.FEBRUARY){
    return 'February';
  }
  else if(month === Months.MARCH){
    return 'March';
  }
  else if(month === Months.APRIL){
    return 'April';
  }
  else if(month === Months.MAY){
    return 'May';
  }
  else if(month === Months.JUNE){
    return 'June';
  }
  else if(month === Months.JULY){
    return 'July';
  }
  else if(month === Months.AUGUST){
    return 'August';
  }
  else if(month === Months.SEPTEMBER){
    return 'September';
  }
  else if(month === Months.OCTOBER){
    return 'October';
  }
  else if(month === Months.NOVEMBER){
    return 'November';
  }
  else if(month === Months.DECEMBER){
    return 'December';
  }
  return '';
}


