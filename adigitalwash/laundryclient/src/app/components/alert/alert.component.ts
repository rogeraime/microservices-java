import { Component, OnInit, Input } from '@angular/core';
import { Subject } from 'rxjs';
import { debounceTime } from 'rxjs/operators';

@Component({
  selector: 'app-alert',
  templateUrl: './alert.component.html',
  styleUrls: ['./alert.component.css']
})
export class AlertComponent implements OnInit {

  constructor() { }

  @Input() private message: String;

  /**
   *  sets alert css class (e. g. bootstrap classes like 'danger')
   */
  @Input() private alertType: String;

  @Input() public alertId: String;
  @Input() private alertDismiss: String;

  /**
   *  AlertComponent.show.next("ALERTID") calls alert with alertId
   */
  public static show = new Subject<String>();

  tempId: String;

  ngOnInit() {
    AlertComponent.show.subscribe((id) => this.tempId = id);
    if (this.alertDismiss != "false") {
      AlertComponent.show.pipe(
        debounceTime(5000)
      ).subscribe(() => this.tempId = null);
    }
  }

}
