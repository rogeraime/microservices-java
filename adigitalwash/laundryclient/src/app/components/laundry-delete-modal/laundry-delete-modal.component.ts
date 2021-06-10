import { Component, OnInit, Input } from '@angular/core';
import { Laundry } from '../../models/laundry';

@Component({
  selector: 'app-laundry-delete-modal',
  templateUrl: './laundry-delete-modal.component.html',
  styleUrls: ['./laundry-delete-modal.component.css']
})
export class LaundryDeleteModalComponent implements OnInit {

  public laundry: Laundry;

  constructor() { }

  ngOnInit() {
  }

}
