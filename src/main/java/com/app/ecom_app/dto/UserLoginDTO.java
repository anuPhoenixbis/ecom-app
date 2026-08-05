package com.app.ecom_app.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserLoginDTO {

    @Email
    @NotBlank
    private String email;

    @NotBlank(message = "Password is required")
    private String password;
}
