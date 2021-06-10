import { TestBed, inject } from '@angular/core/testing';
import { Http } from '@angular/http';
import { LaundryService } from './laundry.service';

describe('LaundryService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [LaundryService, {provide: Http}]
    });
  });

  it('should be created', inject([LaundryService], (service: LaundryService) => {
    expect(service).toBeTruthy();
  }));
});
