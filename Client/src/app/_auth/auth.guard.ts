import { Injectable } from '@angular/core';
import {  ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from '@angular/router';
import { EmployeeService } from '../_services/employee.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {

    const emp = localStorage.getItem('employee');
    const adm = localStorage.getItem('admin');

    if(state.url === '/feed' && emp == null) {
      this.router.navigate(['404']);
      return false;
    }
    else if(state.url === '/network' && emp == null) {
      this.router.navigate(['404']);
      return false;
    }
    else if(state.url === '/settings' && emp == null) {
      this.router.navigate(['404']);
      return false;
    }
    else if(state.url === '/notifications' && emp == null) {
      this.router.navigate(['404']);
      return false;
    }
    else if(state.url === '/admin' && adm == null) {
      this.router.navigate(['404']);
      return false;
    }
    else if(state.url === '/register' && emp != null) {
      this.router.navigate(['feed']);
      return false;
    }
    else if(state.url === '/'){
      if(emp != null){
        this.router.navigate(['feed']);
        return false;
      }
      else if(adm != null){
        this.router.navigate(['admin']);
        return false;
      }
    }

    return true;
  }
  
}
