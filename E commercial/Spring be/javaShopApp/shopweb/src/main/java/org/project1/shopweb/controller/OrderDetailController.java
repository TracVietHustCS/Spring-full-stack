package org.project1.shopweb.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.project1.shopweb.component.LocalizationUtils;
import org.project1.shopweb.dto.OrderDetailDTO;
import org.project1.shopweb.model.OrderDetail;
import org.project1.shopweb.service.OrderDetailService;
import org.project1.shopweb.respon.OrderDetailResponse;
import org.project1.shopweb.utils.MessageKeys;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/order_details")
@RequiredArgsConstructor
public class OrderDetailController {
    private final OrderDetailService orderDetailService;
    private final LocalizationUtils localizationUtils;

    @PostMapping()
    public ResponseEntity<?> creatOderDetail(@Valid @RequestBody OrderDetailDTO orderDetailDTO) {

            OrderDetail orderDetail = orderDetailService.creatOrderDetail(orderDetailDTO);
            return ResponseEntity.ok(orderDetail);

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDetail(@PathVariable long id) {

            OrderDetail orderDetail = orderDetailService.getOrder(id);
            return ResponseEntity.ok(OrderDetailResponse.fromOrderDetail(orderDetail));

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(
            @Valid @PathVariable("id") Long id,
            @RequestBody OrderDetailDTO newOrderDetailData) {

            orderDetailService.updateOrderDetail(id, newOrderDetailData);
            return ResponseEntity.ok().body("update successfully");


    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> getOder(@PathVariable long id) {
        orderDetailService.deleteOrderDetail(id);
        return ResponseEntity.ok(localizationUtils.getLocalizationMessages(MessageKeys.DELETE_ORDER_DETAIL));


    }

    @GetMapping("order/{orderId}")
    public ResponseEntity<?> findByOrderId(Long id) {

            List<OrderDetail> orderDetail = orderDetailService.findByOrderId(id);
            List<OrderDetailResponse> orderDetailResponses = orderDetail.stream().map(OrderDetailResponse::fromOrderDetail).toList();
            return ResponseEntity.ok(orderDetailResponses);


    }
}
