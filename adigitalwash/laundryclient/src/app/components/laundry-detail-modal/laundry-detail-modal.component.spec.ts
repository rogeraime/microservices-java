import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LaundryDetailModalComponent } from './laundry-detail-modal.component';

describe('LaundryDetailModalComponent', () => {
  let component: LaundryDetailModalComponent;
  let fixture: ComponentFixture<LaundryDetailModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LaundryDetailModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LaundryDetailModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
