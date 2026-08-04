package com.app.ecom_app.controller;

import com.app.ecom_app.dto.CartItemRequest;
import com.app.ecom_app.model.CartItem;
import com.app.ecom_app.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<String> addToCart(
            @RequestHeader("X-User-ID") String userId,
            @Valid @RequestBody CartItemRequest request
        ) {
        if(!cartService.addToCart(userId,request)){
            return ResponseEntity.badRequest().body("Product Out of Stock Or User Id is Invalid Or Product Id is Invalid");
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Void> deleteFromCart(
            @RequestHeader("X-User-ID") String userId,
            @PathVariable String productId
    ){
        if(!cartService.deleteItemFromCart(userId,productId)){
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<List<CartItem>> getCartItems(
            @RequestHeader("X-User-ID") String userId
    ){
        List<CartItem> items = cartService.getCartItems(userId);
        if(items == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(items);
    }
}
