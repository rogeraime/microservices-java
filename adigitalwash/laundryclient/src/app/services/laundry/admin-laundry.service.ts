import { Injectable } from '@angular/core';
import { Http, Response, RequestOptions, Headers } from '@angular/http';
import { Observable } from 'rxjs';
import { Laundry } from '../../models/laundry';
import { Category } from '../../models/category';
import { UserService } from '../user/user.service';

//const url = 'http://localhost:8084';
const url = '/adminservice';

@Injectable()
/**
 * Stores the laundry and category objects required
 * in the admin components. Performs basic crud operations 
 * with the adminservice.
 */
export class AdminLaundryService {

    constructor(
        private http: Http,
        private userService: UserService
    ) {}

    laundries: Array < Laundry > = [];
    categories: Array < Category > = [];
    
    setLaundries(laundries: Array < Laundry > ): void {
        this.laundries = laundries;
    }

    /**
     * Return laundry with given id
     * @param {number} id laundry id
     */
    getLaundry(id: number) {
        return this.laundries.filter(x => x.id === id)[0];
    }

    /**
     * Query laundries by delivery date.
     * @param  {String}        date          Delivery date in the form yyyy-mm-dd
     * @return {Observable <Laundry[]>}               List of laundries with given delivery date.
     */
    queryLaundriesByDate(date: String): Observable <Laundry[]> {
        return this.http.get(url + '/get-laundries/' + date, this.userService.getHeaders()).map((response: Response) => response.json());
    }

    /**
     * Update the payment status of the laundry with the given id.
     * @param  {number}     id laundry id
     * @return {Observable}    The updated laundry.
     */
    updatePaymentStatus(id: number): Observable < Laundry > {
        return this.http.get(url + '/laundries/' + id + '/paid', this.userService.getHeaders()).map((response: Response) => response.json());
    }

    /**
     * Update the fetch status fo the laundry with the given id.
     * @param  {number}     id laundry id
     * @return {Observable}    The updated laundry.
     
    updateFetchedByCleanersStatus(id: number): Observable < Laundry > {
        return this.http.put(url + '/laundries/' + id + '/fetched', {}, ).map((response: Response) => response.json());
    }*/

    /**
     * Delete laundry from the server and remove it from laundry list.
     * @param {number} id laundry id
    
    deleteLaundry(id: number) {
        let index = this.laundries.findIndex(x => x.id === id);
        if (index > -1) {
            this.laundries.splice(index, 1);
        }

        return this.http.delete(url + '/laundries/' + id, this.userService.getHeaders())
            .map((response: Response) => response.json());
    } */

    
    
    fillZeros(num: number): string {
        if (num < 10)
            return '0' + num;
        return num + '';
    }

    getShirtsPdf(filterDelivery: string) {
        location.href = '/export/shirts' + filterDelivery;
    }

    getNonShirtsPdf(filterDelivery: string) {
        location.href = '/export/nonshirts' + filterDelivery;
    }
}
