package org.project1.shopweb.repository;

import org.project1.shopweb.model.CouponCondition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponConditionRepository extends JpaRepository<CouponCondition,Long> {
    List<CouponCondition> findByCouponId(Long id);
}
