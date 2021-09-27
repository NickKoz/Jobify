import { TestBed } from '@angular/core/testing';

import { JobAdService } from './job-ad.service';

describe('JobAdService', () => {
  let service: JobAdService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(JobAdService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
