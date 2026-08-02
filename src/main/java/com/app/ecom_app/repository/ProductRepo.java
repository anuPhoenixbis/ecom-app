package com.app.ecom_app.repository;

import com.app.ecom_app.dto.ProductResponse;
import com.app.ecom_app.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface ProductRepo extends JpaRepository<Product, String> {
    List<Product> findByActiveTrue();

//    making our own query
//    here search the active and in stocks products whose names match
    @Query("SELECT p FROM products p WHERE p.active=true AND p.quantity>0 AND LOWER(p.name) LIKE LOWER(CONCAT('%',:keyword, '%'))")
    List<Product> searchProducts(@Param("keyword") String keyword);
}
