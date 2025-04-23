package org.project1.shopweb.repository;

import org.project1.shopweb.model.Category;
import org.project1.shopweb.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {
   boolean existsByName(String name);
   Page<Product> findAll(Pageable pageable);
   Page<Product> findByCategory(Category category,Pageable pageable);

   @Query("SELECT p FROM Product p WHERE (:categoryId IS NULL OR :categoryId = 0 OR p.category.id = :categoryId) AND (:keyword IS NULL OR :keyword = '' OR p.name LIKE %:keyword% OR p.description LIKE %:keyword%)")
   Page<Product> searchProducts(@Param("categoryId") Long categoryId, @Param("keyword") String keyword, Pageable pageable);

   @Query("SELECT p FROM Product p WHERE p.id IN :productIds")
   List<Product> findProductsByIds(@Param("productIds") List<Long> listId);
}
