package com.app.ecom_app.service;

import com.app.ecom_app.dto.OrderItemDTO;
import com.app.ecom_app.dto.OrderResponse;
import com.app.ecom_app.enums.OrderStatus;
import com.app.ecom_app.model.CartItem;
import com.app.ecom_app.model.Order;
import com.app.ecom_app.model.OrderItem;
import com.app.ecom_app.model.User;
import com.app.ecom_app.repository.OrderRepo;
import com.app.ecom_app.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartService cartService;
    private final UserRepo userRepo;
    private final OrderRepo orderRepo;

    public OrderResponse createOrder(String userId) {
//        tasks: validate for cart items then, validate for user, calculate total price then, create order then, clear cart
        OrderResponse orderResponse = new OrderResponse();

        User user = userRepo.findById(userId).orElse(null);
        if (user == null) {
            return orderResponse;
        }

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
}
