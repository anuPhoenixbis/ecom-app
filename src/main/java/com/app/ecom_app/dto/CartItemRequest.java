package com.app.ecom_app.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigInteger;

@Data
public class CartItemRequest {

    @NotBlank(message = "Product ID is required")
    private String productId;

    @NotNull(message = "Quantity is required")
    @DecimalMin(value="1", message = "Quantity must be at least 1")
    @DecimalMax(value="100", message = "Quantity cannot exceed 100")
    private BigInteger quantity;
}
