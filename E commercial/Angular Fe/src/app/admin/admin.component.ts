import { Component } from '@angular/core';
import { UserService } from '../service/user.service';
import { TokenService } from '../service/token.service';
import { Router } from '@angular/router';
import { UserResponse } from '../responses/user/user.respon';
@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.scss']
})
export class AdminComponent {
  //adminComponent: string = 'orders';
  userResponse?:UserResponse | null;
  constructor(
    private userService: UserService,       
    private tokenService: TokenService,    
    private router: Router,
  ) {
    
   }
  ngOnInit() {
    this.userResponse = this.userService.getUserResponseFromLocalStorage();    
   }  
  logout() {
    this.userService.removeUserFromLocalStorage();
    this.tokenService.removeToken();
    this.userResponse = this.userService.getUserResponseFromLocalStorage();    
  }
  showAdminComponent(componentName: string): void {
    //this.adminComponent = componentName;orders,categories
    if(componentName=='orders') {
      this.router.navigate(['/admin/orders']);
    } else if(componentName=='categories') {
      this.router.navigate(['/admin/categories']);
    }else if(componentName=='products') {
      this.router.navigate(['/admin/products']);
    }
    
  }

}
