package org.project1.shopweb.controller;

import lombok.RequiredArgsConstructor;
import org.project1.shopweb.respon.CouponRespon;
import org.project1.shopweb.service.coupon.ICouponService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("${api.prefix}/coupons")
@RestController


public class CouponController {
    private final ICouponService iCouponService;

    @GetMapping("")
    public ResponseEntity<CouponRespon> calculateCoupon(
            @RequestParam String couponCode,
            @RequestParam double totalAmount
    ){
        try {
            double finalAmount = iCouponService.calculateFinalPrice(couponCode, totalAmount);
            CouponRespon couponRespon = CouponRespon.builder()
                    .message("")
                    .result(finalAmount)
                    .build();
            return ResponseEntity.ok(couponRespon);

        }catch (Exception e){
            return ResponseEntity.badRequest().body(CouponRespon.builder()
                    .result(totalAmount)
                    .message(e.getMessage())
                    .build());
        }
    }


}
