import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { AdminLaundryService } from '../../services/Laundry/admin-laundry.service';
import { MockLaundry } from '../../models/mockLaundry';
import { AdminLaundryListComponent } from './admin-laundry-list.component';
import { FormsModule } from '@angular/forms';


describe('AdminLaundryListComponent', () => {
  let component: AdminLaundryListComponent;
  let fixture: ComponentFixture < AdminLaundryListComponent > ;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
        imports: [ FormsModule ],
        providers: [{provide: AdminLaundryService, useClass: MockLaundry}],
        declarations: [ AdminLaundryListComponent ]
      })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminLaundryListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
