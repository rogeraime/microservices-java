import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ClothCategoryModalComponent } from './cloth-category-modal.component';

describe('ClothCategoryModalComponent', () => {
  let component: ClothCategoryModalComponent;
  let fixture: ComponentFixture<ClothCategoryModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ClothCategoryModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ClothCategoryModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
