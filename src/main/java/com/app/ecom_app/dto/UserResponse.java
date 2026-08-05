package com.app.ecom_app.dto;

import com.app.ecom_app.enums.UserRole;
import lombok.Data;

@Data
public class UserResponse {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private UserRole role;
    private AddressDTO address;
}
