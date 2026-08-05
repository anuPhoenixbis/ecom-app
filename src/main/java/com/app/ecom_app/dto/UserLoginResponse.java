package com.app.ecom_app.dto;

import com.app.ecom_app.enums.UserRole;
import lombok.Data;

@Data
public class UserLoginResponse {

    private String token;

    private String email;

    private UserRole role;
}
