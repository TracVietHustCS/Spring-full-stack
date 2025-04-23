package org.project1.shopweb.service.coupon;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project1.shopweb.exception.NotFoundException;
import org.project1.shopweb.model.Coupon;
import org.project1.shopweb.model.CouponCondition;
import org.project1.shopweb.repository.CouponConditionRepository;
import org.project1.shopweb.repository.CouponRepository;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j

public class CouponService implements ICouponService{
    private final CouponRepository couponRepository;
    private final CouponConditionRepository couponConditionRepository;
    @Override
    public double calculateFinalPrice(String couponCode, double total) {
        Optional<Coupon> coupon = couponRepository.findByCode(couponCode);
        if(!coupon.isPresent()){
            throw new NotFoundException("Coupon is not exist");
        }
        Coupon coupon1 = coupon.get();
        if (!coupon1.getActive()) {
            throw new IllegalArgumentException("Coupon is not active");
        }
       double discount =  calculateDiscount(coupon1,total);
        double finalAmount = total - discount;

        return finalAmount;

    }

    public double calculateDiscount(Coupon coupon,double totalAmount){
        double discount = 0.0;
        List<CouponCondition> couponConditions = couponConditionRepository.findByCouponId(coupon.getId());
        for(CouponCondition couponCondition : couponConditions) {
            String attribute = couponCondition.getAttribute();
            String operator =
                    couponCondition.getOperator();
            String value = couponCondition.getValue();
            Double discountAmount = couponCondition.getDiscountAmount().doubleValue();
            // trying to creat the same with shoppe profession :
            // can use 2 coupon but coupon type must be differnce
            if (attribute.equals("minimum_amount")) {
                if (operator.equals(">") && totalAmount > Double.parseDouble(value)) {
                    discount += totalAmount * discountAmount / 100;
                    String a = String.valueOf(discount);
                    log.info(a);

                    break;
                }

            } else if (attribute.equals("applicable_date")) {
                LocalDate current = LocalDate.now();
                LocalDate applyDate = LocalDate.parse(value);
                if (operator.equals("BETWEEN") && current.isEqual(applyDate)) {
                    discount += totalAmount * discountAmount / 100;
                    break;
                }

            }
        }
            return discount;
    }
}
