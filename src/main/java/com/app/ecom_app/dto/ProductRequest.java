package com.app.ecom_app.dto;

import com.app.ecom_app.enums.Category;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
public class ProductRequest {
    @NotBlank(message = "Product name is required")
    @Size(min = 3, max = 100,
            message = "Product name must be between 3 and 100 characters")
    private String name;

    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 1000,
            message = "Description must be between 10 and 1000 characters")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", inclusive = true,
            message = "Price must be greater than 0")
    @Digits(integer = 10, fraction = 2,
            message = "Price can have up to 10 integer digits and 2 decimal places")
    private BigDecimal price;
    @NotNull(message = "Quantity is required")
    @DecimalMin(value = "0", inclusive = true,
            message = "Quantity cannot be negative")
    private BigInteger quantity;
    @NotNull(message = "Category is required")
    private Category category;
    @NotBlank(message = "Image URL is required")
    @Pattern(
            regexp = "^(https?://).+",
            message = "Image URL must be a valid HTTP or HTTPS URL"
    )
    @Size(max = 500, message = "Image URL is too long")
    private String imageUrl;

}
