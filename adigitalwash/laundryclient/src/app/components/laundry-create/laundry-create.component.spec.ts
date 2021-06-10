import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateLaundryComponent } from './laundry-create.component';

describe('CreateLaundryComponent', () => {
  let component: CreateLaundryComponent;
  let fixture: ComponentFixture<CreateLaundryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CreateLaundryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CreateLaundryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
