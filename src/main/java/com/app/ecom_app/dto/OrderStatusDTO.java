package com.app.ecom_app.dto;

import com.app.ecom_app.enums.OrderStatus;
import lombok.Data;
import lombok.NonNull;

@Data
public class OrderStatusDTO {
    @NonNull
    private OrderStatus orderStatus;
}
