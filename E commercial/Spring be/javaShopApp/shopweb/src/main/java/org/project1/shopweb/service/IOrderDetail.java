package org.project1.shopweb.service;

import org.project1.shopweb.dto.OrderDetailDTO;
import org.project1.shopweb.model.OrderDetail;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IOrderDetail {
    OrderDetail creatOrderDetail(OrderDetailDTO orderDetailDTO);

    OrderDetail updateOrderDetail(long id, OrderDetailDTO orderDetailDTO);

    void deleteOrderDetail(Long id);

    OrderDetail getOrder(Long id);

    Page<OrderDetail> findAll(int pageNum, String sortField, String sortDir);

    List<OrderDetail> findByOrderId(Long orderId);
}

