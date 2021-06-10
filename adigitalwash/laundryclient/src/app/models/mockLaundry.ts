import {Observable} from 'rxjs/Rx';
import { Laundry } from './laundry';

const LAUNDRY_OBJECT: Laundry = new Laundry();

export class MockLaundry {
  
    
  public me(): Observable<Laundry> {
    return Observable.of(LAUNDRY_OBJECT);
  }
}