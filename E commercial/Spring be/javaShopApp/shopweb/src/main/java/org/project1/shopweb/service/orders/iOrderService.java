package org.project1.shopweb.service.orders;


import org.project1.shopweb.dto.OrderDTO;
import org.project1.shopweb.model.Order;
import org.project1.shopweb.respon.OrderRespon;
import org.springframework.data.domain.Page;


import java.util.List;

public interface iOrderService {
    Order creatOrder(OrderDTO orderDTO);
    void deleteOrder(Long id);

    Order updateOrder(Long id,OrderDTO orderDTO);
    Order getOrder(Long id);

    List<Order> findByUserId(Long userId);

    public Page<OrderRespon> getALlOrder(int pageNum, String sortField, String sortDir, String keyword);

}
