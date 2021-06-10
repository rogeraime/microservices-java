import { Component, OnInit } from '@angular/core';
import { LaundryService } from '../../services/laundry/laundry.service';
import { AdminLaundryService } from '../../services/laundry/admin-laundry.service';
import { CategoryService } from '../../services/category/category.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { UserService } from '../../services/user/user.service';
import { of } from 'rxjs/observable/of';
import { Category } from '../../models/category';
import { AlertComponent } from '../alert/alert.component';

@Component({
  selector: 'app-cloth-category',
  templateUrl: './cloth-category.component.html',
  styleUrls: ['./cloth-category.component.css']
})
export class ClothCategoryComponent implements OnInit {

  public allCategories: Array<any>;
  private weekDaysAsNumbers: Array<any>;
  public isInEditMode: boolean = false;
  private newCategory: Category = new Category;
  private felderIsLeer: boolean = false; 

  constructor(
    private laundryService: LaundryService,
    private adminService: AdminLaundryService,
    private categoryService: CategoryService,
    public activeModal: NgbActiveModal,
    public userService: UserService
  ) { }

  active = 'aktiv';
  inactive = 'inaktiv';

  ngOnInit() {
    this.categoryService.getAllCategories().subscribe(
      data => {
        this.allCategories = data;
      },
    );
  }

  switchMode(): void {
    if (!this.userService.logoutIfTokenExpired()) {
      this.isInEditMode = !this.isInEditMode;
    }
  }

  addCategory() {
    if (this.newCategory.name != undefined && this.newCategory.priceCache != undefined && this.newCategory.name != null && this.newCategory.name != "" && this.newCategory.priceCache != null) {
      this.newCategory.price = 0.0;
      this.newCategory.priceCache = parseFloat(("" + this.newCategory.priceCache).replace(',', '.'));
      this.newCategory.id = this.allCategories[this.allCategories.length - 1].id + 1;
      this.newCategory.active = false;
      this.newCategory.activeCache = true;
      this.allCategories.push(this.newCategory);
      this.newCategory = new Category;
      this.felderIsLeer = false;
    } else {
        this.felderIsLeer = true;
        AlertComponent.show.next("category");
    }
  }

  updateCategories() {
      
      this.addCategory();
      
      if(this.felderIsLeer == false){
          
          for (let category of this.allCategories) {
              
              if (category.name != null && category.name != "" && category.priceCache != null) {
              category.priceCache = parseFloat(("" + category.priceCache).replace(',', '.'));
              } else {
                 AlertComponent.show.next("category");
              }
            }
            this.categoryService.updateCategories(this.allCategories).subscribe(
            data => this.allCategories = data,
            () => AlertComponent.show.next("savingError"));
            this.activeModal.close('Close click');
     
      }else{
            return;
      }
  }
      
  getWeekDaysAsFullTextGerman(): String {
      this.laundryService.getWeekdays().subscribe(
              data => this.weekDaysAsNumbers = data);
      
      let weekDaysFullText: Array<String> = new Array();
      
      this.weekDaysAsNumbers.forEach(dayNumber => {weekDaysFullText.push(this.dayOfWeekGerman(dayNumber))});
      
      return weekDaysFullText.join("/");
  }
  
  private dayOfWeekGerman(dayNumber): String {
      return ["Montag","Dienstag","Mittwoch","Donnerstag","Freitag","Samstag","Sonntag"][dayNumber-1];
    }
}
