package org.project1.shopweb.model;

import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project1.shopweb.service.ProductRedisService;
@RequiredArgsConstructor
@Slf4j
public class ProductListener {

    private  final ProductRedisService productRedisService;

    @PrePersist
    public void prePersist(Product product) {
        log.info("prePersist");
    }

    @PostPersist
    public void postPersist(Product product){
        productRedisService.clear();

    }

    @PreUpdate
    public void preUpdate(Product product) {
        //ApplicationEventPublisher.instance().publishEvent(event);
        log.info("preUpdate");
    }

    @PostUpdate
    public void postUpdate(Product product){
        log.info("post update");
        productRedisService.
                clear();
    }


    @PreRemove
    public void preRemove(Product product) {
        //ApplicationEventPublisher.instance().publishEvent(event);
        log.info("preRemove");
    }

    @PostRemove
    public void postRemove(Product product) {
        // Update Redis cache
        log.info("postRemove");
        productRedisService.clear();
    }


}
