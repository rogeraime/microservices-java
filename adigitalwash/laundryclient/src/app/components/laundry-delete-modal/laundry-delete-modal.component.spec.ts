import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LaundryDeleteModalComponent } from './laundry-delete-modal.component';

describe('LaundryDeleteModalComponent', () => {
  let component: LaundryDeleteModalComponent;
  let fixture: ComponentFixture<LaundryDeleteModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LaundryDeleteModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LaundryDeleteModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
