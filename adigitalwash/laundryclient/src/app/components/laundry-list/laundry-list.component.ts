import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { LaundryService } from '../../services/laundry/laundry.service';
import { Laundry } from '../../models/laundry';
import { ObservableMedia } from '@angular/flex-layout';
import { Observable } from "rxjs/observable";
import "rxjs/add/operator/map";
import "rxjs/add/operator/takeWhile";
import "rxjs/add/operator/startWith";
import { UserService } from '../../services/user/user.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { LaundryCreateModalComponent } from '../laundry-create-modal/laundry-create-modal.component';
import { LaundryDeleteModalComponent } from '../laundry-delete-modal/laundry-delete-modal.component';
import { LaundryDetailModalComponent } from "../laundry-detail-modal/laundry-detail-modal.component";
import { LaundryEditModalComponent } from "../laundry-edit-modal/laundry-edit-modal.component";
import { AlertComponent } from '../alert/alert.component';

@Component({
  selector: 'app-laundry-list',
  templateUrl: './laundry-list.component.html',
  styleUrls: ['./laundry-list.component.css', '../../app.component.css'],
  encapsulation: ViewEncapsulation.None
})

/**
 * Displays all laundries of the current user.
 */
export class LaundryListComponent implements OnInit {

  private roles: string = this.userService.userGroupFromToken();

  // selected laundry to remove, required by confirmation module
  private deleteLaundry: Laundry;
  // selected laundry to show details of, required by laundry-detail component
  private detailLaundry: Laundry;
  private editLaundry: Laundry;
  private showDetail: boolean = false;
  public title: string = 'Deine W\u00e4sche-\u00dcbersicht:';
  public cols: Observable<number>;
  private length: number;
  private defaultLaundryImage: string = '../../assets/defaultlaundry.jpg';
  


  constructor(
    public laundryService: LaundryService,
    private observableMedia: ObservableMedia,
    private userService: UserService,
    public modalService: NgbModal) { }

  ngOnInit() {
    const grid = new Map([
      ["xs", 1],
      ["sm", 2],
      ["md", 3],
      ["lg", 4],
      ["xl", 4]
    ]);
    let start: number;
    grid.forEach((cols, mqAlias) => {
      if (this.observableMedia.isActive(mqAlias)) {
        start = cols;
        length = cols;
      }
    });
    this.cols = this.observableMedia.asObservable()
      .map(change => {
        return grid.get(change.mqAlias);
      })
      .startWith(start);

    this.getLaundriesByCurrentUser();
  }

  /**
   * Query all laundries of the current user from the server.
   */
  getLaundriesByCurrentUser() {
    this.laundryService.getUserLaundries().subscribe(
      data => {
        this.laundryService.setLaundries(data);
        if(this.laundryService.laundries.length > 0){
          this.laundryService.gotLaundryThisWeek = this.laundryService.isEditable(this.laundryService.laundries[0]);
        }
        else{
          this.laundryService.gotLaundryThisWeek = false;
        }
          
      });
  }

  /**
   * Hide the laundry from the list.
   * @param {number} id laundry id
   */
  //TODO: replace by api call
  hideThisLaundry(id: number) {
    const hide = confirm('Willst du diesen Wäschebeutel aus der Ansicht entfernen?');
    if (hide) {
      this.laundryService.laundries.filter(x => x.id === id)[0].fetchedByOwner = true;
    }
  }

  markAsCompleted(id: number) {
    if (!this.userService.logoutIfTokenExpired()) {
      this.laundryService.updateCompleted(id).subscribe(data => {
        let index = this.laundryService.laundries.findIndex(x => x.id === id);
        let laundry = this.laundryService.laundries[index];
        laundry.completed = !laundry.completed;
        this.laundryService.laundries[index] = laundry;
      }, error => {
        AlertComponent.show.next("savingError");
      }
      );
    }
  }

  open(className: string) {
    if(!this.userService.logoutIfTokenExpired())
    switch (className) {
      case LaundryCreateModalComponent.name:
        if (!this.laundryService.gotLaundryThisWeek)
          this.modalService.open(LaundryCreateModalComponent);
        break;

      case LaundryDeleteModalComponent.name:
        const modalRefDelete = this.modalService.open(LaundryDeleteModalComponent);
        modalRefDelete.componentInstance.laundry = this.deleteLaundry;
        break;

      case LaundryDetailModalComponent.name:
        const modalRefDetail = this.modalService.open(LaundryDetailModalComponent);
        modalRefDetail.componentInstance.laundry = this.detailLaundry;
        modalRefDetail.componentInstance.deletable = true;
        break;

      case LaundryEditModalComponent.name:
        const modalRefEdit = this.modalService.open(LaundryEditModalComponent);
        modalRefEdit.componentInstance.laundry = this.editLaundry;
        break;

      default:
        break;
    }
  }
}
