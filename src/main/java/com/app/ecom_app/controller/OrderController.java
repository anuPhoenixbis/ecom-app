package com.app.ecom_app.controller;

import com.app.ecom_app.dto.OrderResponse;
import com.app.ecom_app.model.Order;
import com.app.ecom_app.model.User;
import com.app.ecom_app.repository.UserRepo;
import com.app.ecom_app.service.CartService;
import com.app.ecom_app.service.OrderService;
import com.app.ecom_app.utils.AuthChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    private final UserRepo userRepo;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(){

        AuthChecker auth = new AuthChecker(userRepo);
        Optional<User> userOpt = auth.checkAuth();

        if(!userOpt.isPresent()){
            return ResponseEntity.badRequest().build();
        }

        User user = userOpt.get();

        OrderResponse order = orderService.createOrder(user);
        if(order == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders(){
        AuthChecker auth = new AuthChecker(userRepo);
        Optional<User> userOpt = auth.checkAuth();

        if(!userOpt.isPresent()){
            return ResponseEntity.badRequest().build();
        }

        User user = userOpt.get();

        List<OrderResponse> orders = orderService.getOrders(user);

        if(orders.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable String orderId){
        AuthChecker auth = new AuthChecker(userRepo);
        Optional<User> userOpt = auth.checkAuth();

        if(!userOpt.isPresent()){
            return ResponseEntity.badRequest().build();
        }

        User user = userOpt.get();

        OrderResponse order = orderService.getOrderById(user.getId(),orderId);
        if(order == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable String orderId){
        AuthChecker auth = new AuthChecker(userRepo);
        Optional<User> userOpt = auth.checkAuth();
        if(!userOpt.isPresent()){
            return ResponseEntity.badRequest().build();
        }
        User user = userOpt.get();

        OrderResponse order = orderService.cancelOrderById(user.getId(),orderId);
        if(order == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(order, HttpStatus.OK);
    }
}
