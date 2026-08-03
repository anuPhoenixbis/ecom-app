package com.app.ecom_app.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@Data
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

//    many items in a singular cart of a user hence, manyToOne relationship
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)//join at user_id
    private User user;

//    multiple products in a singular cart of a user hence, manyToOne
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    private BigInteger quantityOnHand;
    private BigDecimal price;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
