package com.app.ecom_app.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NonNull;

@Data
public class UserRequest {

    @NotBlank
    @Size(min=1)
    private String firstName;
    private String lastName;

    @Email
    @NotBlank
    private String email;

    @Pattern(
            regexp = "^(\\+91|91)?[6-9]\\d{9}$",
            message = "Invalid phone number"
    )
    private String phone;

    @NotBlank(message = "Password is required")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&^#()_+\\-={}\\[\\]:;\"'<>,./\\\\|`~])[A-Za-z\\d@$!%*?&^#()_+\\-={}\\[\\]:;\"'<>,./\\\\|`~]{8,}$",
            message = "Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one digit, and one special character."
    )
    private String password;

    @NonNull
    private AddressDTO address;
}
