import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LaundryListComponent } from './laundry-list.component';

describe('LaundryListComponent', () => {
  let component: LaundryListComponent;
  let fixture: ComponentFixture<LaundryListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LaundryListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LaundryListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
