package org.project1.shopweb.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project1.shopweb.model.ProductCacheWrapper;
import org.project1.shopweb.respon.ProductRespon;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor

public class   ProductRedisService implements IProductRedisService{
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    private String getKeyFrom(String keyword,
                              Long categoryId,
                              PageRequest pageRequest) {
        int pageNumber = pageRequest.getPageNumber();


        Sort sort = pageRequest.getSort();
        String sortDirection = sort.getOrderFor("id")
                .getDirection() == Sort.Direction.ASC ? "asc": "desc";

        String key = String.format("all_products:%s:%d:%s:%s",
                keyword, categoryId, pageNumber, sortDirection);
        return key;

    }
    @Override
    public void clear() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();


    }

        @Override
        public ProductCacheWrapper getAllProducts(String keyword, Long categoryId, PageRequest pageRequest) throws JsonProcessingException {

            String key = getKeyFrom(keyword,categoryId,pageRequest);
            String json = (String) redisTemplate.opsForValue().get(key);


            ProductCacheWrapper wrapper =
                    json != null ?  objectMapper.readValue(json, ProductCacheWrapper.class) : null;
            log.info("lay tu cacche");
            return wrapper;


    }

    @Override
    public void saveAllProducts(ProductCacheWrapper productCacheWrapper, String keyword, Long categoryId,
                                PageRequest pageRequest) throws JsonProcessingException {
        log.info("vao toi reids");
        String key = getKeyFrom(keyword,categoryId,pageRequest);

        String json = objectMapper.writeValueAsString(productCacheWrapper);
        redisTemplate.opsForValue().set(key,json);

    }



}
