import { Component, OnInit, ViewEncapsulation, AfterViewInit } from '@angular/core';
import { AdminLaundryService } from '../../services/laundry/admin-laundry.service';
import { CategoryService } from '../../services/category/category.service';
import { Laundry } from '../../models/laundry';
import { Cloth } from '../../models/cloth';
import { UserService } from '../../services/user/user.service';
import { LaundryDetailModalComponent } from "../laundry-detail-modal/laundry-detail-modal.component";
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NgbCalendar, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { NgbDate } from '@ng-bootstrap/ng-bootstrap/datepicker/ngb-date';
import { AlertComponent } from '../alert/alert.component';
import { CookieService } from 'ngx-cookie-service';
import { LaundryService } from '../../services/laundry/laundry.service';
const url = "/export";

@Component({
    selector: 'app-admin-laundry-list',
    templateUrl: './admin-laundry-list.component.html',
    styleUrls: ['./admin-laundry-list.component.css'],
    encapsulation: ViewEncapsulation.None
})
export class AdminLaundryListComponent implements OnInit {

    constructor(
        private calendar: NgbCalendar,
        private laundryService: LaundryService,
        private adminLaundryService: AdminLaundryService,
        private categoryService: CategoryService,
        public userService: UserService,
        public modalService: NgbModal,
        private cookieService: CookieService
    ) { }

    // referenced by info and confirmation modals
    selectedLaundry: Laundry;

    unpaid = "unbezahlt";
    paid = "bezahlt";
    shirtsStr = "Hemden/Blusen";
    nonshirtsStr = "Nicht-Hemden";

    // Filter variables
    filter: string = "";
    filterAccepted: string;

    //used for switching between lists and determining the current listtype
    public tableSwitchName: string = this.nonshirtsStr;
    isNonShirtTable: boolean = false;
    
    // calendar´s date - selected and shown
    dateModel: NgbDateStruct;

    // statistics
    stats: Array<any> = [];
    statsTotalCosts: number = 0;
    statsTotalClothAmount: number = 0;

    aktLaundries: Array<Laundry> = [];
    shirts: Array<Cloth> = [];
    nonshirts: Array<Cloth> = [];

    weekdays: Array<number>;

    ngOnInit() {
        this.laundryService.getWeekdays().subscribe(data => {
            this.weekdays = data;
            this.setNextAcceptedDate();
            this.updateShownDateModel(this.getFilterAcceptedDate());
            this.categoryService.getAllCategories().subscribe(
                data => {
                    this.categoryService.categories = data;
                    this.getLaundriesByDate();
                }
            )
        });
    }

    setNextAcceptedDate() {
        let date = new Date();
        date.setDate(this.getNextAcceptedDate(date));
        this.filterAccepted = this.getFilterDateFormat(date);
    }

    getNextAcceptedDate(dateOfNextDay: Date) {
        let monthOfDateShownInCalendar = this.getFilterAcceptedDate().getMonth();
        let yearOfDateShownInCalendar = this.getFilterAcceptedDate().getFullYear();

        let additionalDaysToStepIntoNextMonth = 0;

        if (monthOfDateShownInCalendar < dateOfNextDay.getMonth() || yearOfDateShownInCalendar < dateOfNextDay.getFullYear() ) {
            additionalDaysToStepIntoNextMonth += this.getNumberOfDaysFromMonthInYear(monthOfDateShownInCalendar, yearOfDateShownInCalendar); 
        } 
        
        return dateOfNextDay.getDate() + additionalDaysToStepIntoNextMonth + ( this.getNextWeekdayFromWeekdays( dateOfNextDay.getDay() ) + 7 - dateOfNextDay.getDay() ) % 7;
    }

    getPrevAcceptedDate(dateOfPreviousDay: Date) {
        let monthOfDateShownInCalendar = this.getFilterAcceptedDate().getMonth();
        let yearOfDateShownInCalendar = this.getFilterAcceptedDate().getFullYear();
        
        let substractionDaysToStepIntoPreviousMonth = 0;
        
        if (monthOfDateShownInCalendar > dateOfPreviousDay.getMonth() || this.getFilterAcceptedDate().getFullYear() > dateOfPreviousDay.getFullYear() ) {
            substractionDaysToStepIntoPreviousMonth -= this.getNumberOfDaysFromMonthInYear(monthOfDateShownInCalendar - 1, yearOfDateShownInCalendar); // -1 to get count of days from previous month
        }

        return dateOfPreviousDay.getDate() + substractionDaysToStepIntoPreviousMonth + (this.getPrevWeekdayFromWeekdays(dateOfPreviousDay.getDay()) - 7 - dateOfPreviousDay.getDay()) % 7;
    }
    
    /**
     * Beware: January is 0 based here!
     * 
     * @param month to get number of days from
     * @param year where month is part of - remember: days of February varies in leap year
     */
     private getNumberOfDaysFromMonthInYear(month: number, year: number): number {
         return new Date(year, month + 1, 0 ).getDate(); // +1 to get January 0 based   
     }
    
    getFilterDateFormat(date): string {
        return date.getFullYear() + '-' + this.fillZeros(date.getMonth() + 1) + '-' + this.fillZeros(date.getDate());
    }

    getFilterAcceptedDate() {
        return new Date(this.filterAccepted + "T00:00:00");
    }

    getNextWeekdayFromWeekdays(day: number) {
        let differences: Array<number> = new Array<number>();
        let difference: number;
        this.weekdays.forEach(wday => {
            difference = wday - day;
            if (difference < 0) {
                difference = difference + 7;
            }
            differences.push(difference);
        });
        let minimum = 7;
        differences.forEach(difference => {
            if (difference < minimum) {
                minimum = difference;
            }
        });
        if (minimum == 7) {
            return day;
        }
        let minimumIndex = differences.indexOf(minimum);
        return this.weekdays[minimumIndex];
    }

    getPrevWeekdayFromWeekdays(day: number) {
        let differences: Array<number> = new Array<number>();
        let difference: number;
        this.weekdays.forEach(wday => {
            difference = wday - day;
            if (difference > 0) {
                difference = (difference - 7);
            }
            difference = difference * (-1);
            differences.push(difference);
        });
        let minimum = 7;
        differences.forEach(difference => {
            if (difference < minimum) {
                minimum = difference;
            }
        });
        if (minimum == 7) {
            return day;
        }
        let minimumIndex = differences.indexOf(minimum);
        return this.weekdays[minimumIndex];
    }

    switchTable(): void {
        this.isNonShirtTable = !this.isNonShirtTable;
        if (!this.userService.logoutIfTokenExpired()) {
            if (this.isNonShirtTable)
                this.tableSwitchName = this.shirtsStr;
            else
                this.tableSwitchName = this.nonshirtsStr;
        }
    }

    /**
     * decrements the filterAccepted by 1 week
     */
    switchAcceptedDate(next: boolean) {
        this.resetFilter();
        let date = this.getFilterAcceptedDate();
        let nextDay = new Date(date);
        nextDay.setDate(date.getDate() + 1);
        let prevDay = new Date(date);
        prevDay.setDate(date.getDate() - 1);
        if (next) date.setDate(this.getNextAcceptedDate(nextDay));
        else date.setDate(this.getPrevAcceptedDate(prevDay));
        this.filterAccepted = this.getFilterDateFormat(date);
        this.getLaundriesByDate();
        
        this.updateShownDateModel(date);
    }
    
    private updateShownDateModel(dateToUpdateWith: Date) {
        this.dateModel = {
            year: dateToUpdateWith.getFullYear(),
            month: dateToUpdateWith.getMonth() + 1,
            day: dateToUpdateWith.getDate()
        }
    }

    private fillZeros(num: number): string {
        if (num < 10)
            return '0' + num;
        return num + '';
    }

    /**
     * Computes statistical data from the current selection
     * of laundries.
     */
    prepareStatistics(): void {
        // reset stat values
        this.stats = [];
        this.statsTotalCosts = 0;
        this.statsTotalClothAmount = 0;
        this.adminLaundryService.categories.forEach(c => {
            this.stats[c.id] = { "type": c.name, "amount": 0, "costs": 0 };
        });

        // gather aggregated cloth data (numbers and costs)
        this.aktLaundries.forEach(l => {
            l.clothes.forEach(c => {
                this.stats[c.category.id].amount += c.clothCount;
                this.stats[c.category.id].costs += c.costs;
                this.statsTotalCosts += c.costs;
                this.statsTotalClothAmount += c.clothCount;
            });
        });

        // remove undefined values from array
        this.stats = this.stats.filter(e => e != undefined);
    }

    /**
     * Resets all filters in the laundry list.
     */
    resetFilter(): void {
        this.filter = "";
        this.applyFilter();
    }

    /**
     * Query all laundries with the selected accpted date.
     * If no accpted date has been selected, throw an error.
     */
    getLaundriesByDate() {
        if (!this.userService.logoutIfTokenExpired()) {
            this.adminLaundryService.queryLaundriesByDate(this.filterAccepted).subscribe(
                data => {
                    this.adminLaundryService.laundries = this.aktLaundries = data;
                    this.fillShirtsNonShirts();
                });
        }
    }

    /**
     * Apply all given filters to the laundry collection.
     */
    applyFilter(): void {
        let aktlaundries: Array<Laundry> = this.adminLaundryService.laundries;
        if (this.filter !== "" && this.filter !== undefined) {
            this.aktLaundries = aktlaundries.filter(x => String(x.id) == this.filter);
            this.aktLaundries = this.aktLaundries.concat(aktlaundries.filter(x => x.laundryOwner.indexOf(this.filter) >= 0));
        }
        else {
            this.aktLaundries = aktlaundries;
        }
    }

    /**
     * Sets the paid status of the given laundry to true.
     * @param  {number}  id id of laundry
     * @return {boolean}    return false to prevent href of a tags
     */
    markAsPaid(id: number): boolean {
        if (!this.userService.logoutIfTokenExpired()) {
            this.adminLaundryService.updatePaymentStatus(id).subscribe(data => {
                let index = this.adminLaundryService.laundries.findIndex(x => x.id === id);
                let laundry = this.adminLaundryService.laundries[index];
                if (laundry.totalCost == laundry.paid) this.adminLaundryService.laundries[index].paid = this.aktLaundries[index].paid = 0;
                else this.adminLaundryService.laundries[index].paid = this.aktLaundries[index].paid = laundry.totalCost;
            }, () => AlertComponent.show.next("savingError"));
        }
        return false;
    }

    /**
     * Sets the fetchedByCleaners status of the given laundry to true.
     * @param  {number}  id id of laundry
     * @return {boolean}    return false to prevent href of a tags
    
    markAsFetchedByCleaners(id: number): boolean {
        this.adminLaundryService.updateFetchedByCleanersStatus(id).subscribe(data => {
            let index = this.adminLaundryService.laundries.findIndex(x => x.id === id);
            this.adminLaundryService.laundries[index].fetchedByCleaners = true;
        });
        return false;
    } */

    /**
    * Sets the fetchedByCleaners status of all laundries in the adminLaundryService to true.
    * @param  {number}  idList List of Laundry id's
    * @return {boolean}    return false to prevent href of a tags
    
    markAllAsFetchedByCleaners(): boolean {
        //Marks all laundries of the unfiltered laundrylist
        for (var i = 0; i < this.adminLaundryService.laundries.length; i++) {
            this.markAsFetchedByCleaners(this.adminLaundryService.laundries[i].id);
        }
        return false;
    }*/

    /**
     * Compute and return total amount of clothes in laundry.
     */
    calcClothAmount() {
        let result = 0;
        this.selectedLaundry.clothes.forEach(x => result += x.clothCount);
        return result;
    }

    /**
     * Permanently remove laundry from dataset.
     
    deleteLaundry() {
        this.adminLaundryService.deleteLaundry(this.selectedLaundry.id).subscribe();
    } */

    sortById() {
        this.aktLaundries.sort((l1: Laundry, l2: Laundry) => {
            if (l1.id > l2.id)
                return 1;
            return -1;
        });
    }

    print() {
        if (!this.userService.logoutIfTokenExpired()) {
            this.cookieService.set("authToken", this.cookieService.get("accessToken"), 0.05);
            if (this.isNonShirtTable && this.nonshirts.length > 0) {
                this.adminLaundryService.getNonShirtsPdf(this.filterAccepted);
            }
            else if (!this.isNonShirtTable && this.shirts.length > 0) {
                this.adminLaundryService.getShirtsPdf(this.filterAccepted);
            }
            else {
                AlertComponent.show.next("noLaundryToPrint");
            }
        }
    }

    fillShirtsNonShirts() {
        this.shirts = [];
        this.nonshirts = [];
        this.aktLaundries.forEach(
            l => {
                l.clothes.forEach(
                    c => {
                        if (c.clothCount != 0) {
                            if (c.categoryId == this.categoryService.categories[0].id) {
                                this.shirts.push(c);
                            }
                            else {
                                this.nonshirts.push(c);
                            }
                        }
                    }
                );
            }
        );
    }

    private getSumOfClothes(laundry: Laundry) {
        let sumOfClothes = 0;
        laundry.clothes.forEach(c => {
            if (c.id != 1)
                sumOfClothes += c.clothCount;
        });
        return sumOfClothes;
    }

    openDetail() {
        if (!this.userService.logoutIfTokenExpired()) {
            let modalRefDetail = this.modalService.open(LaundryDetailModalComponent);
            this.selectedLaundry.image = "";
            modalRefDetail.componentInstance.laundry = this.selectedLaundry;
            modalRefDetail.componentInstance.deletable = false;
        }
    }

    //__________________________________________________DATEPICKER_METHODS_____________________________________
    isDisabled = (date: NgbDate) => !this.weekdays.includes(this.calendar.getWeekday(date));
    isEnabled = (date: NgbDate) => this.weekdays.includes(this.calendar.getWeekday(date));

    saveDate(date: NgbDate) {
        if (this.isEnabled(date)) setTimeout(
            () => {
                this.filterAccepted = date.year + "-" + this.fillZeros(date.month) + "-" + this.fillZeros(date.day);
                let ddate = this.getFilterAcceptedDate();
                this.filterAccepted = this.getFilterDateFormat(ddate);
                this.getLaundriesByDate();
            },
            0);
    }

}
