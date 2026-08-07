package com.app.ecom_app.service;

import com.app.ecom_app.dto.OrderItemDTO;
import com.app.ecom_app.dto.OrderResponse;
import com.app.ecom_app.dto.OrderStatusDTO;
import com.app.ecom_app.enums.OrderStatus;
import com.app.ecom_app.model.*;
import com.app.ecom_app.repository.OrderRepo;
import com.app.ecom_app.repository.ProductRepo;
import com.app.ecom_app.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartService cartService;
    private final ProductService productService;
    private final OrderRepo orderRepo;
    private final ProductRepo productRepo;

    @Transactional
    public OrderResponse createOrder(User user) {
//        tasks: validate for cart items then, validate for user, calculate total price then, create order then, clear cart
        OrderResponse orderResponse = new OrderResponse();

        String userId = user.getId();

        List<CartItem> cartItems = cartService.getCartItems(userId);
        if (cartItems == null || cartItems.isEmpty()) {
            return orderResponse;
        }

        BigDecimal totalPrice = cartItems.stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(totalPrice);

//        convert cartItems to orderItems
        List<OrderItem> orderItems = cartItems.stream()
                .map(item-> new OrderItem(
                        null,
                        item.getProduct(),
                        item.getQuantityOnHand(),
                        item.getPrice(),
                        order
                ))
                .toList();

        order.setItems(orderItems);
        Order savedOrder = orderRepo.save(order);

//        update the stock quantity
        productService.updateStockQuantity(userId);

//        clear the cart
        cartService.clearCart(userId);

        return mapToOrderResponse(savedOrder);
    }

    private OrderResponse mapToOrderResponse(Order savedOrder) {
        return new OrderResponse(
            savedOrder.getId(),
            savedOrder.getTotalAmount(),
            savedOrder.getStatus(),
            savedOrder.getItems().stream()
                    .map(orderItem -> new OrderItemDTO(
                            orderItem.getId(),
                            orderItem.getProduct().getId(),
                            orderItem.getPrice(),
                            orderItem.getQuantity(),
                            orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity()))
                    ))
                    .toList(),
            savedOrder.getCreatedAt()
        );
    }

    public List<OrderResponse> getOrders(User user) {
        List<Order> ordersList = orderRepo.findByUser_Id(user.getId());

        List<OrderResponse> ordersResponse = new ArrayList<>();
        for (Order order : ordersList) {
            ordersResponse.add(mapToOrderResponse(order));
        }
        return ordersResponse;
    }

    public OrderResponse getOrderById(String userId, String orderId) {
        Optional<Order> order = orderRepo.findByIdAndUser_Id(orderId,userId);

        if(order.isPresent()) {
            return mapToOrderResponse(order.get());
        }
        return null;
    }

    @Transactional
    public OrderResponse cancelOrderById(String userId, String orderId) {
        Order order = orderRepo.findByIdAndUser_Id(orderId, userId)
                .orElse(null);

        if (order == null) {
            return null;
        }

        OrderStatus status = order.getStatus();

        if (status == OrderStatus.PENDING ||
                status == OrderStatus.CONFIRMED ||
                status == OrderStatus.PACKED ||
                status == OrderStatus.SHIPPED) {

            order.setStatus(OrderStatus.CANCELLED);

            updateStockQuantityAfterCancel(userId,orderId);

            orderRepo.save(order);
        }

        return mapToOrderResponse(order);
    }

    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepo.findAll();
        List<OrderResponse> ordersResponse = new ArrayList<>();
        for (Order order : orders) {
            ordersResponse.add(mapToOrderResponse(order));
        }
        return ordersResponse;
    }

    public OrderResponse getOrderByIdAdmin(String orderId) {
        Order order = orderRepo.findById(orderId).get();
        if(order == null) {
            return null;
        }
        return mapToOrderResponse(order);
    }

    public OrderResponse updateOrderById(String orderId, OrderStatusDTO status) {
        Order order = orderRepo.findById(orderId).get();
        if(order == null) {
            return null;
        }
        order.setStatus(status.getOrderStatus());
        orderRepo.save(order);
        return mapToOrderResponse(order);
    }


    private void updateStockQuantityAfterCancel(String userId, String orderId) {
        Optional<Order> order = orderRepo.findByIdAndUser_Id(orderId, userId);
        if(order.isPresent()) {
            for(OrderItem orderItem : order.get().getItems()) {
                Product product = productRepo.findById(orderItem.getProduct().getId()).get();
                product.setQuantity(product.getQuantity().add(orderItem.getQuantity()));
                productRepo.save(product);
            }
        }
    }
}
