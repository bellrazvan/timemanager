import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { guestRedirectGuard } from './guest-redirect.guard';

describe('guestRedirectGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => guestRedirectGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
