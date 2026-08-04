package com.app.ecom_app.service;

import com.app.ecom_app.dto.CartItemRequest;
import com.app.ecom_app.model.CartItem;
import com.app.ecom_app.model.Product;
import com.app.ecom_app.model.User;
import com.app.ecom_app.repository.CartItemRepo;
import com.app.ecom_app.repository.ProductRepo;
import com.app.ecom_app.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final ProductRepo productRepo;
    private final UserRepo userRepo;
    private final CartItemRepo cartItemRepo;

    public boolean addToCart(String userId, CartItemRequest request) {
        // Task: look for the product(from ProductRepo) , look for the User(from UserRepo)
        // add the product to the cart(from the cartRepo)
        Optional<Product> productOpt = productRepo.findById(request.getProductId());
        Optional<User> userOpt = userRepo.findById(userId);

        if(request.getQuantity().compareTo(BigInteger.ZERO)<=0) return false;

        if (productOpt.isEmpty() || userOpt.isEmpty() || productOpt.get().getQuantity().compareTo(request.getQuantity()) <= 0) {
//            invalid if product is null or user is null or if product qty is not greater than request qty
            return false;
        }
        Product product = productOpt.get();
        User user = userOpt.get();

        CartItem existingCartItem = cartItemRepo.findByUserAndProduct(user,product);
        if(existingCartItem != null){
//            if item already exists in the cart then update the qty and price
            existingCartItem.setQuantityOnHand(existingCartItem.getQuantityOnHand().add(request.getQuantity()));
            existingCartItem.setPrice(product.getPrice().multiply(new BigDecimal(existingCartItem.getQuantityOnHand())));
            cartItemRepo.save(existingCartItem);
        }else{
//            orElse push the product with the no of items(new cartItem)
            CartItem cartItem = new CartItem();
            cartItem.setUser(user);
            cartItem.setProduct(product);
            cartItem.setQuantityOnHand(request.getQuantity());
            cartItem.setPrice(product.getPrice().multiply(new BigDecimal(request.getQuantity())));
            cartItemRepo.save(cartItem);
        }
        return true;
    }

    public boolean deleteItemFromCart(String userId, String productId) {
        Optional<Product> productOpt = productRepo.findById(productId);
        Optional<User> userOpt = userRepo.findById(userId);

        if (productOpt.isPresent() && userOpt.isPresent()) {
            cartItemRepo.deleteByUserAndProduct(userOpt.get(),productOpt.get());
            return true;
        }

        return false;
    }

    public List<CartItem> getCartItems(String userId) {
        Optional<User> userOpt = userRepo.findById(userId);

        if(userOpt.isPresent()){
            return cartItemRepo.findAll();
        }
        return null;
    }

    public void clearCart(String userId) {
        userRepo.findById(userId).ifPresent(cartItemRepo::deleteByUser);
    }
}
