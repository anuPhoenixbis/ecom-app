package com.app.ecom_app.repository;

import com.app.ecom_app.model.CartItem;
import com.app.ecom_app.model.Product;
import com.app.ecom_app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepo  extends JpaRepository<CartItem, String> {
    CartItem findByUserAndProduct(User user, Product product);

    void deleteByUserAndProduct(User user, Product product);

    void deleteByUser(User user);
}
