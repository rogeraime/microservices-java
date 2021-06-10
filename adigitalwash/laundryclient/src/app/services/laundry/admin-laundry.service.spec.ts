import { TestBed, inject } from '@angular/core/testing';
import { Http } from '@angular/http';

import { AdminLaundryService } from './admin-laundry.service';

describe('AdminLaundryService', () => {
    beforeEach(() => {
        TestBed.configureTestingModule({
            providers: [AdminLaundryService, {provide: Http}]
        });
    });

    it('should be created', inject([AdminLaundryService], (service: AdminLaundryService) => {
        expect(service).toBeTruthy();
    }));
});
