package org.project1.shopweb.service.coupon;

public interface ICouponService {
    double calculateFinalPrice(String couponCode,double total);
}
