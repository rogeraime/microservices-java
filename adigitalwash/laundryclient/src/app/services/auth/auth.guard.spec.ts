import { TestBed, async, inject } from '@angular/core/testing';
import { UserService } from '../user/user.service';
import { AuthGuard } from './auth.guard';
import { Router } from '@angular/router';


describe('AuthGuard', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [AuthGuard, {provide: UserService}, {provide: Router}]
    });
  });

  it('should ...', inject([AuthGuard], (guard: AuthGuard) => {
    expect(guard).toBeTruthy();
  }));
});
