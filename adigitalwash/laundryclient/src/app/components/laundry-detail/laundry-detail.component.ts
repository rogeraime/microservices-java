import { Component, OnInit, Input, ViewEncapsulation, Inject } from '@angular/core';
import { LaundryService } from '../../services/laundry/laundry.service';
import { CategoryService } from '../../services/category/category.service';
import { Laundry } from '../../models/laundry';
import { UserService } from '../../services/user/user.service'
import { NgbModal, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { LaundryDeleteModalComponent } from '../laundry-delete-modal/laundry-delete-modal.component';

@Component({
  selector: 'app-laundry-detail',
  templateUrl: './laundry-detail.component.html',
  styleUrls: ['./laundry-detail.component.css'],
  encapsulation: ViewEncapsulation.None
})

/**
 * Child component of laundry-list.
 * Displays laundry details like payment status, submission, accepted and delivery date,
 * an overview of the clothes ordered and the fetched status.
 */
export class LaundryDetailComponent implements OnInit {

  @Input() public laundry: Laundry;
  @Input() public deletable: boolean;
  private allCategories: Array<any>;
  private checked: boolean = false;
  private indeterminate: boolean = false;
  private align: String = 'start';
  private disabled: boolean = false;
  private selectedLaundry: Laundry;

  constructor(
    private laundryService: LaundryService,
    private categoryService: CategoryService,
    public activeModal: NgbActiveModal,
    public modalService: NgbModal,
    private userService: UserService
  ) { }

  /**
   * Query all categories from service.
   */
  ngOnInit() {
    this.categoryService.getAllCategories().subscribe(
      data => {
        this.allCategories = data;
      });
  }


  /**
   * Compute and return the total amount of clothes assigned to the laundry.
   */
  calcClothAmount() {
    let result = 0;
    this.laundry.clothes.forEach(x => result += x.clothCount);
    return result;
  }

  /**
   * Hide this laundry from the laundry list.
   */
  //TODO: replace by api request
  hideThisLaundry() {
    const hide = confirm('Willst du diesen W\u00e4schebeutel aus der Ansicht entfernen?');
    if (hide) {
      this.laundry.fetchedByOwner = true;

    }
  }


  deleteLaundry(laundry: Laundry) {
    if (!this.userService.logoutIfTokenExpired()) {
      const modalRefDelete = this.modalService.open(LaundryDeleteModalComponent);
      modalRefDelete.componentInstance.laundry = this.laundry;
    }
  }
}
