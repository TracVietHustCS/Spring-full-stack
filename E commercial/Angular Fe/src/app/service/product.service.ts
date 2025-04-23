import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../enviroments/enviroments';
import { Product } from '../models/product';



@Injectable({
    providedIn: 'root'
  })
  export class ProductService {
    private apiGetProducts = `${environment.apiUrl}/products`;
  
    constructor(private http: HttpClient) { }
  
    getProducts(pageNum: number = 0, sortField: string = 'id', sortDir: string = 'asc',categoryId: number = 0,keyword: string 
      ): Observable<Product[]> {
      const params = new HttpParams()
      .set('pageNum', pageNum)
      .set('keyword', keyword)
      .set('sortField', sortField)
      .set('sortDir', sortDir)
      .set('categoryId', categoryId);          
      return this.http.get<Product[]>(this.apiGetProducts, { params });
    }
    getDetailProduct(productId: number) {
      return this.http.get(`${environment.apiUrl}/products/${productId}`);
    }

    getProductsByIds(productIds: number[]): Observable<Product[]> {
      // Chuyển danh sách ID thành một chuỗi và truyền vào params
      debugger
      const params = new HttpParams().set('ids', productIds.join(',')); 
      return this.http.get<Product[]>(`${this.apiGetProducts}/by-ids`, { params });
    }
  }
