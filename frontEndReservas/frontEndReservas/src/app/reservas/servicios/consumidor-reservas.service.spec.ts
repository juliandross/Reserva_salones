import { TestBed } from '@angular/core/testing';

import { ConsumidorReservasService } from './consumidor-reservas.service';

describe('ConsumidorReservasService', () => {
  let service: ConsumidorReservasService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ConsumidorReservasService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
