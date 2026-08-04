package com.app.ecom_app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddressDTO {

    @NotBlank(message = "Street is required")
    @Size(min = 5, max = 150, message = "Street must be between 5 and 150 characters")
    private String street;

    @NotBlank(message = "City is required")
    @Size(min = 2, max = 50, message = "City must be between 2 and 50 characters")
    @Pattern(
            regexp = "^[A-Za-z ]+$",
            message = "City can contain only letters and spaces"
    )
    private String city;

    @NotBlank(message = "State is required")
    @Size(min = 2, max = 50, message = "State must be between 2 and 50 characters")
    @Pattern(
            regexp = "^[A-Za-z ]+$",
            message = "State can contain only letters and spaces"
    )
    private String state;

    @NotBlank(message = "Country is required")
    @Size(min = 2, max = 60, message = "Country must be between 2 and 60 characters")
    @Pattern(
            regexp = "^[A-Za-z ]+$",
            message = "Country can contain only letters and spaces"
    )
    private String country;

    @NotBlank(message = "ZIP/Postal code is required")
    @Pattern(
            regexp = "^[A-Za-z0-9\\- ]{3,12}$",
            message = "Invalid ZIP/Postal code"
    )
    private String zip;
}
