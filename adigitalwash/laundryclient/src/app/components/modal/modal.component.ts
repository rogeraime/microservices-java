import { Component, Input } from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
/**
 *
 * Wrapper for the NgbActiveModal with default modal-header includes a headline field and a close button.
 * You should define a modal-body & modal-footer for a fully usable Modal
 * usage: <app-modal><modal-body></modal-body><modal-footer></modal-footer></app-modal>
 * @export
 * @class ModalComponent
 */
@Component({
  selector: 'app-modal',
  templateUrl: './modal.component.html',
  styleUrls: ['./modal.component.css']
})

export class ModalComponent {

  /**
   *
   * The headline displayed in the modal header
   * You can overwrite it by using the headline field
   * @memberof ModalComponent
   */
  @Input() headline = `Information`;

  constructor(public activeModal: NgbActiveModal) {}

}
