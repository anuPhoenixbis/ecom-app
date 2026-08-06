package com.app.ecom_app.controller;

import com.app.ecom_app.dto.CartItemRequest;
import com.app.ecom_app.dto.CartQuantityChangeDTO;
import com.app.ecom_app.model.CartItem;
import com.app.ecom_app.model.User;
import com.app.ecom_app.repository.UserRepo;
import com.app.ecom_app.service.CartService;
import com.app.ecom_app.utils.AuthChecker;
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

    private final UserRepo userRepo;

    @PostMapping
    public ResponseEntity<String> addToCart(
            @Valid @RequestBody CartItemRequest request
        ) {

        AuthChecker auth = new AuthChecker(userRepo);
        User user = auth.checkAuth().orElse(null);
        if(user == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        String userId = user.getId();

        if(!cartService.addToCart(userId,request)){
            return ResponseEntity.badRequest().body("Product Out of Stock Or User Id is Invalid Or Product Id is Invalid");
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteFromCart(
            @PathVariable String productId
    ){

        AuthChecker auth = new AuthChecker(userRepo);
        User user = auth.checkAuth().orElse(null);
        if(user == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        String userId = user.getId();

        if(!cartService.deleteItemFromCart(userId,productId)){
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<List<CartItem>> getCartItems(){

        AuthChecker auth = new AuthChecker(userRepo);
        User user = auth.checkAuth().orElse(null);
        if(user == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        String userId = user.getId();

        List<CartItem> items = cartService.getCartItems(userId);
        if(items == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(items);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<List<CartItem>> updateCartItems(@PathVariable String productId, @Valid @RequestBody CartQuantityChangeDTO request){
        AuthChecker auth = new AuthChecker(userRepo);
        User user = auth.checkAuth().orElse(null);
        if(user == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        String userId = user.getId();
        List<CartItem> items = cartService.getCartItems(userId);

        if(items == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        cartService.updateProductQuantity(userId, productId,request.getQuantity());

        return ResponseEntity.ok(items);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteCartItems(){
        AuthChecker auth = new AuthChecker(userRepo);
        User user = auth.checkAuth().orElse(null);
        if(user == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        String userId = user.getId();

        cartService.clearCart(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
