import { Component, OnInit } from '@angular/core';
import { Laundry } from '../../models/laundry';

@Component({
  selector: 'app-laundry-detail-modal',
  templateUrl: './laundry-detail-modal.component.html',
  styleUrls: ['./laundry-detail-modal.component.css']
})
export class LaundryDetailModalComponent implements OnInit {

  public laundry: Laundry;
  public deletable: boolean;

  constructor() { }

  ngOnInit() {
  }

}
