package org.project1.shopweb.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.project1.shopweb.model.ProductCacheWrapper;
import org.project1.shopweb.respon.ProductRespon;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface IProductRedisService {
    void clear();//clear cache
    ProductCacheWrapper getAllProducts(
            String keyword,
            Long categoryId, PageRequest pageRequest) throws JsonProcessingException;
    void saveAllProducts(ProductCacheWrapper productCacheWrapper, String keyword, Long categoryId,
                         PageRequest pageRequest) throws JsonProcessingException;
}
