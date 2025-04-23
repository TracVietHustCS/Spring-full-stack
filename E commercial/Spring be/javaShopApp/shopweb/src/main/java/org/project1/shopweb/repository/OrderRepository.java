package org.project1.shopweb.repository;

import org.project1.shopweb.model.Order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    //    boolean existsByName(String name);
    List<Order> findByUserId(Long id);
    @Query("SELECT o FROM Order o WHERE o.active = true AND  (:keyword IS NULL OR :keyword = '' OR o.address LIKE %:keyword% OR o.note LIKE %:keyword% or o.email LIKE %:keyword%)")
    Page<Order> findByKeyword(String keyword, Pageable pageable);


}
