package org.project1.shopweb.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project1.shopweb.component.LocalizationUtils;
import org.project1.shopweb.dto.OrderDTO;
import org.project1.shopweb.model.Order;
import org.project1.shopweb.service.orders.OrderService;
import org.project1.shopweb.respon.OrderListRespon;
import org.project1.shopweb.respon.OrderRespon;
import org.project1.shopweb.utils.MessageKeys;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j

@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor

public class OrderController {
    private final OrderService orderService;

    private final LocalizationUtils localizationUtils;
    @PostMapping("")
    public ResponseEntity<?> creatOrder(@Valid @RequestBody OrderDTO orderDto){

            log.info(orderDto.getFullName());
            Order orderRespon = orderService.creatOrder(orderDto);
            return ResponseEntity.ok(orderRespon);



    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(@PathVariable Long id){


             Order order = orderService.getOrder(id);
             OrderRespon orderRespon = OrderRespon.fromOrder(order);
           return ResponseEntity.ok(orderRespon);


    }
    @GetMapping("/user/{user_id}") // Thêm biến đường dẫn "user_id"
    //GET http://localhost:8088/api/v1/orders/4
    public ResponseEntity<?> getOrders( @PathVariable("user_id") Long userId) {
            List<Order> orderList = orderService.findByUserId(userId);
            return ResponseEntity.ok(orderList );

    }
    @PutMapping("/{id}")

    public ResponseEntity<?> updateOrder(
             @PathVariable long id,
             @RequestBody OrderDTO orderDTO) {
            Order order = orderService.updateOrder(id, orderDTO);
            return ResponseEntity.ok(order);

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id){
        orderService.deleteOrder(id);
        log.info("da delete dc");
        String result = localizationUtils.getLocalizationMessages(
                MessageKeys.DELETE_ORDER, id);
        return ResponseEntity.ok().body(result);
    }

        @GetMapping("/get-orders-by-keyword")
        @PreAuthorize("hasRole('ROLE_ADMIN')")
        public ResponseEntity<OrderListRespon> getOrderByKeyWored(@RequestParam(required = false) String keyword,
                                                                  @RequestParam(defaultValue = "0") int pageNum,
                                                                  @RequestParam(defaultValue = "orderDate") String sortField,
                                                                  @RequestParam(defaultValue = "asc") String sortDir){
            log.info("request da acp");
            Page<OrderRespon> page = orderService.getALlOrder(pageNum,sortField,sortDir,keyword);

            return ResponseEntity.ok(OrderListRespon.builder().totalPages(page.getTotalPages())
                    .orders(page.getContent()).build());

        }


}
