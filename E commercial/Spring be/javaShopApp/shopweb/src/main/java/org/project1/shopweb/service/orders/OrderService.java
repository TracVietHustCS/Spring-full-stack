package org.project1.shopweb.service.orders;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.project1.shopweb.dto.CartItemDTO;
import org.project1.shopweb.dto.OrderDTO;
import org.project1.shopweb.model.*;
import org.project1.shopweb.repository.OrderDetailRepository;
import org.project1.shopweb.repository.OrderRepository;
import org.project1.shopweb.repository.ProductRepository;
import org.project1.shopweb.repository.UserRepository;
import org.project1.shopweb.exception.NotFoundException;
import org.project1.shopweb.respon.OrderRespon;
import org.project1.shopweb.service.orders.iOrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService  implements iOrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;
    @Override
    public Order creatOrder(OrderDTO orderDTO) {
        User user = userRepository.findById(orderDTO.getUserId()).orElseThrow(() -> new NotFoundException("not found this user"));
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        Order order = new Order();
        modelMapper.map(orderDTO,order);
        order.setUser(user);
        order.setOrderDate(LocalDate.now());//lấy thời điểm hiện tại
        order.setStatus(OrderStatus.PENDING);
        LocalDate shippingDate = orderDTO.getShippingDate() == null ? LocalDate.now() : orderDTO.getShippingDate();
        if(shippingDate.isBefore(LocalDate.now())){
            throw new NotFoundException("Invalid shipping date");

        }
        order.setShippingDate(shippingDate);
        order.setActive(true);
        orderRepository.save(order);

        List<OrderDetail> orderDetails = new ArrayList<>();
        for(CartItemDTO cartItemDTO : orderDTO.getCartItems()){
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            Long producId = cartItemDTO.getProductId();
            int quantity = cartItemDTO.getQuantity();

            Product product = productRepository.findById(cartItemDTO.getProductId()).orElseThrow(() -> new NotFoundException("Not found"));

            orderDetail.setProduct(product);
            orderDetail.setNumberOfProducts(quantity);
            orderDetail.setPrice(product.getPrice());
            orderDetails.add(orderDetail);
        }
        orderDetailRepository.saveAll(orderDetails);
        return order;

    }

    @Override
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id).orElse(null);
        if(order != null){
            order.setActive(false);
            orderRepository.save(order);
        }

    }

    @Override
    @Transactional
    public Order updateOrder(Long id, OrderDTO orderDTO)
            throws NotFoundException {
        Order order = orderRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Cannot find order with id: " + id));

        User existingUser = userRepository.findById(
                orderDTO.getUserId()).orElseThrow(() ->
                new NotFoundException("Cannot find user with id: " + id));
        log.info(orderDTO.getStatus());
        // Tạo một luồng bảng ánh xạ riêng để kiểm soát việc ánh xạ
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        // Cập nhật các trường ủa đơn hàng từ orderDTO
        modelMapper.map(orderDTO, order);
        order.setUser(existingUser);
        return orderRepository.save(order);

    }

    @Override
    public Order getOrder(Long id) {
        return orderRepository.findById(id).
                orElseThrow(() -> new NotFoundException("this order is not exist"));

    }

    @Override
    public List<Order> findByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public Page<OrderRespon> getALlOrder(int pageNum,String sortField,String sortDir,String keyword){
        int pageSize = 4;
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize,
                sortDir.equals("asc") ? Sort.by(sortField).ascending()
                        : Sort.by(sortField).descending());

        log.info("da vo");
        log.info("mngu",orderRepository.findByKeyword(keyword,pageable));

       return orderRepository.findByKeyword(keyword,pageable).map(OrderRespon::fromOrder);

    }
}
