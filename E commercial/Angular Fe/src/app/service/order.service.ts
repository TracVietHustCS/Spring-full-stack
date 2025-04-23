import { HttpClient,HttpHeaders,HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment

 } from "../enviroments/enviroments";
import { Product } from "../models/product";
import { OrderDTO } from "../dtos/orders/order.dto";
import { OrderResponse } from "../responses/user/order/order.respon";



@Injectable({
    providedIn: 'root',
  })
  export class OrderService {
    private apiUrl = `${environment.apiUrl}/orders`;
    private apiGetAllOrders = `${environment.apiUrl}/orders/get-orders-by-keyword`;
  
    constructor(private http: HttpClient) {}
  
    placeOrder(orderData: OrderDTO): Observable<any> {    
      // Gửi yêu cầu đặt hàng
      return this.http.post(this.apiUrl, orderData);
    }

    getOrderById(orderId: number): Observable<any> {
      const url = `${environment.apiUrl}/orders/${orderId}`;
      return this.http.get(url);
    }


    getAllOrders(pageNum: number,keyword:string, sortField: string, sortDir: string
    ): Observable<OrderResponse[]> {
      const params = new HttpParams()
      .set('pageNum', pageNum)
      .set('keyword', keyword)
      .set('sortField', sortField)
      .set('sortDir', sortDir)
    ;             
        return this.http.get<any>(this.apiGetAllOrders, { params });
    }
    updateOrder(orderId: number, orderData: OrderDTO): Observable<any> {
      const url = `${environment.apiUrl}/orders/${orderId}`;
      return this.http.put(url, orderData);
    }
    deleteOrder(orderId: number): Observable<any> {
      const url = `${environment.apiUrl}/orders/${orderId}`;
      const headers = new HttpHeaders({
        'Content-Type': 'application/json',
        'Accept-Language': 'vi'
      });
      return this.http.delete(url, { 
        responseType: 'text',
        headers: headers 
      });
    }


  }