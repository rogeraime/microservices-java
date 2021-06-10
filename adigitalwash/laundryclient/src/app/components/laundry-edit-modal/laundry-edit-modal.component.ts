import { Component, OnInit } from '@angular/core';
import { Laundry } from '../../models/laundry';

@Component({
  selector: 'app-laundry-edit-modal',
  templateUrl: './laundry-edit-modal.component.html',
  styleUrls: ['./laundry-edit-modal.component.css']
})
export class LaundryEditModalComponent implements OnInit {

  public laundry: Laundry;

  constructor() { }

  ngOnInit() {
  }

}
