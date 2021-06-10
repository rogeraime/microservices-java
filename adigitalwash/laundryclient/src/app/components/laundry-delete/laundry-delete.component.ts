import { Component, OnInit, Input, ViewEncapsulation, Inject } from '@angular/core';
import { Laundry } from '../../models/laundry';
import { LaundryService } from '../../services/laundry/laundry.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { UserService } from '../../services/user/user.service';
import { AlertComponent } from '../alert/alert.component';

@Component({
    selector: 'app-delete-laundry',
    templateUrl: './laundry-delete.component.html',
    styleUrls: ['./laundry-delete.component.css'],
    encapsulation: ViewEncapsulation.None
})
export class DeleteLaundryComponent implements OnInit {
    @Input() private laundry: Laundry;

    constructor(
        private laundryService: LaundryService,
        public activeModal: NgbActiveModal,
        private userService: UserService
    ) {}

    ngOnInit() {}

    /**
     * Permanently remove laundry from data storage.
     */
    deleteLaundry() {
        this.laundryService.deleteLaundry(this.laundry).subscribe(data => {}, error => {
            AlertComponent.show.next("savingError");
        });
    }

}
