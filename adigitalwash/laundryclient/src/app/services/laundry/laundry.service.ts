import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs';

import { Laundry } from '../../models/laundry';
import { UserService } from '../user/user.service';
const url = '/laundryservice';

@Injectable()
export class LaundryService {

    constructor(private http: Http,
        private userService: UserService) { }

    laundries: Array<Laundry> = [];
    gotLaundryThisWeek: boolean = true;

    setLaundries(laundries: Array<Laundry>) {
        this.laundries = laundries.reverse();
    }

    getLaundry(id: number): Laundry {
        return this.laundries.filter(x => x.id === id)[0];
    }

    add(laundry: Laundry) {
        this.laundries.unshift(laundry);
        this.gotLaundryThisWeek = true;
    }

    update(laundry: Laundry) {
        let index = this.laundries.findIndex(x => x.id === laundry.id);
        if (index > -1) {
            this.laundries.splice(index, 1);
        }
        this.laundries.unshift(laundry);
    }

    getUserLaundries(): Observable<Laundry[]> {
        return this.http.get(url + '/laundries', this.userService.getHeaders())
            .map((response: Response) => response.json());
    }

    deleteLaundry(laundry: Laundry) {
        if (this.isEditable(laundry)) {
            let id = laundry.id;
            let index = this.laundries.findIndex(x => x.id === id);
            if (index > -1) {
                this.laundries.splice(index, 1);
                this.gotLaundryThisWeek = false;
            }
            return this.http.delete(url + '/delete-laundry/' + id, this.userService.getHeaders());
        }
    }

    // ACHTUNG: CategoryId ist fest zugeordnet (1, 2, 3, 4, 5 momentan)
    addLaundry(categoryCounts: Array<number>, categoryPrices: Array<number>, image: String) {
        const newLaundry = { clothes: [], image: image };
        for (let i = 0; i < categoryCounts.length; i++) {
            newLaundry.clothes.push({ clothCount: categoryCounts[i], costs: categoryPrices[i] * categoryCounts[i], categoryId: i + 1 });
        }
        let sendMail: string = localStorage.getItem("sendMail");
        return this.http.post(url + '/create-laundry/' + sendMail, newLaundry, this.userService.getHeaders())
            .map((response: Response) => response.json());
    }

    updateLaundry(laundry: Laundry, categoryCounts: Array<number>, categoryPrices: Array<number>, image: string) {
        if (this.isEditable(laundry)) {
            for (let i = 0; i < laundry.clothes.length; i++) {
                laundry.clothes[i].clothCount = categoryCounts[i];
                laundry.clothes[i].costs = categoryCounts[i] * categoryPrices[i];
            }
            laundry.image = image;
            laundry.totalCost = 0;
            let sendMail: string = localStorage.getItem("sendMail");
            return this.http.put(url + '/update-laundry/' + sendMail, laundry, this.userService.getHeaders())
                .map((response: Response) => response.json());
        }
    }

    isEditable(laundry: Laundry): boolean {
        var accDate = new Date(laundry.acceptedDate + "T00:00:00");
        return (accDate > new Date());
    }

    isDelivered(laundry: Laundry): boolean {
        var delDate = new Date(laundry.deliveryDate + "T00:00:00");
        return (delDate < new Date());
    }

    updateCompleted(id: number) {
        return this.http.put(url + '/laundries/' + id + '/completed', {}, this.userService.getHeaders());
    }

    getWeekdays() {
        return this.http.get(url + '/weekdays', this.userService.getHeaders()).map((response: Response) => response.json());
    }
}
