package com.app.ecom_app.dto;


import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
public class ProductResponse {
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
    private BigInteger quantity;
    private String category;
    private String imageUrl;
    private Boolean active;
}
