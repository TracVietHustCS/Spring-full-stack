package org.project1.shopweb.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.project1.shopweb.dto.OrderDetailDTO;
import org.project1.shopweb.model.Order;
import org.project1.shopweb.model.OrderDetail;
import org.project1.shopweb.model.Product;
import org.project1.shopweb.repository.OrderDetailRepository;
import org.project1.shopweb.repository.OrderRepository;
import org.project1.shopweb.repository.ProductRepository;
import org.project1.shopweb.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetail{
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository   productRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    @Override
    public OrderDetail creatOrderDetail(OrderDetailDTO orderDetailDTO) {
        Product existProduct = productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(() -> new NotFoundException("Product is not exist"));
        Order existorder = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(() -> new NotFoundException("order is not exist"));
        OrderDetail orderDetail = OrderDetail.builder()
                .order(existorder)
                .product(existProduct)
                .numberOfProducts(orderDetailDTO.getNumberOfProduct())
                .price(orderDetailDTO.getPrice())
                .totalMoney(orderDetailDTO.getTotalMoney())
                .color(orderDetailDTO.getColor())
                .build();
        //lưu vào db

        return orderDetailRepository.save(orderDetail);
    }

    @Override
    public OrderDetail updateOrderDetail(long id, OrderDetailDTO orderDetailDTO) {
        Product existingProduct = productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(() -> new NotFoundException("Product is not exist"));
        Order existingOrder = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(() -> new NotFoundException("order is not exist"));
        OrderDetail existingOrderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order detaiil is not exist"));
        existingOrderDetail.setPrice(orderDetailDTO.getPrice());
        existingOrderDetail.setNumberOfProducts(orderDetailDTO.getNumberOfProduct());
        existingOrderDetail.setTotalMoney(orderDetailDTO.getTotalMoney());
        existingOrderDetail.setColor(orderDetailDTO.getColor());
        existingOrderDetail.setOrder(existingOrder);
        existingOrderDetail.setProduct(existingProduct);
        return orderDetailRepository.save(existingOrderDetail);
    }

    @Override
    public void deleteOrderDetail(Long id) {
       Optional<OrderDetail> orderDetail  =orderDetailRepository.findById(id);
       orderDetail.ifPresent(orderDetailRepository::delete);
    }

    @Override
    public OrderDetail getOrder(Long id) {
        return orderDetailRepository.findById(id).orElseThrow(() -> new NotFoundException("Not exist"));
    }

    @Override
    public Page<OrderDetail> findAll(int pageNum, String sortField, String sortDir) {
        int pageSize = 10;
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize,
                sortDir.equals("asc") ? Sort.by(sortField).ascending()
                        : Sort.by(sortField).descending());

        return  orderDetailRepository.findAll(pageable);
    }
    @Override
    public List<OrderDetail> findByOrderId(Long orderId){
        return  orderDetailRepository.findByOrderId(orderId);
    }
}
