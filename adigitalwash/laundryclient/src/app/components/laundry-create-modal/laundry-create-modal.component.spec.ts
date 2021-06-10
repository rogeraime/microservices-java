import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LaundryCreateModalComponent } from './laundry-create-modal.component';

describe('LaundryCreateModalComponent', () => {
  let component: LaundryCreateModalComponent;
  let fixture: ComponentFixture<LaundryCreateModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LaundryCreateModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LaundryCreateModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
