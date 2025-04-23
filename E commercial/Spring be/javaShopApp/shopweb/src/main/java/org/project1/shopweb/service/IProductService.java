package org.project1.shopweb.service;


import org.project1.shopweb.dto.ProductDTO;
import org.project1.shopweb.dto.ProductImageDTO;
import org.project1.shopweb.model.Product;
import org.project1.shopweb.model.ProductImage;
import org.project1.shopweb.respon.ProductRespon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IProductService {
    void deleteProduct(long id);
    Product getProductById(long id);

    Product creatProduct(ProductDTO productDTO);
    Product updateProduct(long id,ProductDTO productDTO);

    Page<ProductRespon> findAll(Long categoryId, String keyword, Pageable pageable);

    boolean existByName(String name);
    ProductImage createProductImage(
            ProductImageDTO productImageDTO,int img) throws Exception;

    List<Product> findProductsbyIds(List<Long> listId);
}
