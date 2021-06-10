import { Component, OnInit, Input, ViewEncapsulation, Inject } from '@angular/core';
import { LaundryService } from '../../services/laundry/laundry.service';
import { CategoryService } from '../../services/category/category.service';
import { Laundry } from '../../models/laundry';
import { UserService } from '../../services/user/user.service'
import { NgbModal, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { LaundryDeleteModalComponent } from '../laundry-delete-modal/laundry-delete-modal.component';
import { Category } from '../../models/category';
import { AlertComponent } from '../alert/alert.component';

@Component({
  selector: 'app-create-laundry',
  templateUrl: './laundry-create.component.html',
  styleUrls: ['./laundry-create.component.css', '../../app.component.css'],
  host: {
    '(document:drop)': 'dropPicture($event)',
    '(document:dragover)': 'dragoverPicture($event)'

  }
})
export class CreateLaundryComponent implements OnInit {

  @Input() private laundry: Laundry;
  public title: string = 'F\u00fclle deinen W\u00e4sche-Beutel!';
  public allCategories: Array<any> = new Array<any>();
  private allPrices: Array<number>;
  private categoryCount: Array<number>;
  private image: any;
  fileName: string="";

  constructor(
    private laundryService: LaundryService,
    private categoryService: CategoryService,
    public activeModal: NgbActiveModal,
    public modalService: NgbModal,
    private userService: UserService
  ) { }

  /**
   * Query categories and current prices from the server.
   */
  ngOnInit() {
    this.categoryService.getAllCategories().subscribe(
      data => {
        this.allCategories = data;
        this.categoryCount = new Array(data.length).fill(0);
        if (this.laundry != null) {
          this.laundry.clothes.forEach(
            x => {
              this.categoryCount[x.categoryId-1] = x.clothCount;
            });
        }
      },
    );

    this.categoryService.getAllPrices().subscribe(
      data => {
        this.allPrices = data;
      },
    );
    if (this.laundry != null) {
      this.image = this.laundry.image;
    } else {
      this.image = "";
    }
  }

  addImage(ev: any) {
    var file: File = ev.target.files[0];
    this.fileName = file.name;
    this.resizeImage(file, 400, 400).then(
      (data) => {
        this.getBase64(data).then((img) => {
          this.image = img;
        })
      })

  }
  getBase64(file) {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => resolve(reader.result);
      reader.onerror = error => reject(error);
    });
  }
  resizeImage(file: File, maxWidth: number, maxHeight: number): Promise<Blob> {
    return new Promise((resolve, reject) => {
      let image = new Image();
      image.src = URL.createObjectURL(file);
      image.onload = () => {
        let width = image.width;
        let height = image.height;

        if (width <= maxWidth && height <= maxHeight) {
          resolve(file);
        }

        let newWidth;
        let newHeight;

        if (width > height) {
          newHeight = height * (maxWidth / width);
          newWidth = maxWidth;
        } else {
          newWidth = width * (maxHeight / height);
          newHeight = maxHeight;
        }

        let canvas = document.createElement('canvas');
        canvas.width = newWidth;
        canvas.height = newHeight;

        let context = canvas.getContext('2d');

        context.drawImage(image, 0, 0, newWidth, newHeight);

        canvas.toBlob(resolve, file.type);
      };
      image.onerror = reject;
    });
  }

  /**
   * Increment the amount of clothes of the given id.
   * @param {number} id category id
   */
  incrementCategoryCount(id: number) {
    this.categoryCount[id]++;
  }

  /**
   * Decrement the amount of clothes of the given id.
   * @param {number} id category id
   */
  decrementCategoryCount(id: number) {
    if (this.categoryCount[id] === 0) return;
    this.categoryCount[id]--;
  }

  /**
   * If laundry contains at least one piece of cloth, send
   * request to laundryservice to add laundry to data storage.
   */
  addOrUpdateLaundry() {
    if(this.laundry == null) {
      this.addLaundry();
    } else {
      this.updateLaundry();
    }
  }
  
   addLaundry() {
    if (this.categoryCount.every(x => x === 0)) {
      AlertComponent.show.next("laundryEmpty");
    } else {
      let result = this.laundryService.addLaundry(this.categoryCount, this.allPrices, this.image)
        .subscribe(data => {
          this.laundryService.add(data);
        }, () => {
          AlertComponent.show.next("savingError");
        });
      this.categoryCount = new Array(this.allCategories.length).fill(0);
      this.activeModal.close('Close click');
    }
  }

  updateLaundry() {
    if (this.categoryCount.every(x => x === 0)) {
      AlertComponent.show.next("laundryEmpty");
    } else {
      let result = this.laundryService.updateLaundry(this.laundry, this.categoryCount, this.allPrices, this.image)
        .subscribe(data => {
          this.laundryService.update(data);
        }, () => {
          AlertComponent.show.next("savingError");
        });
      this.categoryCount = new Array(this.allCategories.length).fill(0);
      this.activeModal.close('Close click');
    }
  }

  deleteLaundry(laundry: Laundry) {
    if (!this.userService.logoutIfTokenExpired()) {
      const modalRefDelete = this.modalService.open(LaundryDeleteModalComponent);
      modalRefDelete.componentInstance.laundry = this.laundry;
    }
  }

}
