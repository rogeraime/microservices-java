import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { LaundryService } from '../../services/laundry/laundry.service';
import { DeleteLaundryComponent } from './laundry-delete.component';

describe('DeleteLaundryComponent', () => {
  let component: DeleteLaundryComponent;
  let fixture: ComponentFixture<DeleteLaundryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DeleteLaundryComponent ],
      providers: [ { provide: LaundryService } ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DeleteLaundryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
