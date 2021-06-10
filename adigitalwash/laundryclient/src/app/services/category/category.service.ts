import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs';
import { Category } from '../../models/category';
import { UserService } from '../user/user.service';

const url = '/categoryservice';

@Injectable()
export class CategoryService {

  constructor(private http: Http, private userService:UserService) { }

  categories: Array<Category> = [];

  getAllCategories(): Observable<Array<Category>> {
    return this.http.get(url + '/categories', this.userService.getHeaders())
      .map((response: Response) => response.json());
  }

  setCategories() {
    return this.getAllCategories().subscribe(data => {
      this.categories = data;
    });
  }

  getAllPrices(): Observable<Array<number>> {
    return this.http.get(url + '/prices', this.userService.getHeaders())
      .map((response: Response) => response.json());
  }

  updateCategories(allCategories: Array<number>): Observable<Array<Category>> {
    return this.http.put(url + '/update', allCategories, this.userService.getHeaders())
      .map((response: Response) => response.json());
  }
}
