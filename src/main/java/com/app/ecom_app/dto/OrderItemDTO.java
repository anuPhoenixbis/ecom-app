package com.app.ecom_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {
    private String id;
    private String productId;
    private BigDecimal price;
    private BigInteger quantity;
    private BigDecimal totalPrice;
}
