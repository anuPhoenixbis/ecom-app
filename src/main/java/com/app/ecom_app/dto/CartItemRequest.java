package com.app.ecom_app.dto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class CartItemRequest {
    private String productId;
    private BigInteger quantity;
}
