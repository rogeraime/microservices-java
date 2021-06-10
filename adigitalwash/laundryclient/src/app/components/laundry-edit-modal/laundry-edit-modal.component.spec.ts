import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LaundryEditModalComponent } from './laundry-edit-modal.component';

describe('LaundryEditModalComponent', () => {
  let component: LaundryEditModalComponent;
  let fixture: ComponentFixture<LaundryEditModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LaundryEditModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LaundryEditModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
