import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ClothCategoryComponent } from './cloth-category.component';

describe('ClothCategoryComponent', () => {
  let component: ClothCategoryComponent;
  let fixture: ComponentFixture<ClothCategoryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ClothCategoryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ClothCategoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
