package com.app.ecom_app.dto;

import lombok.Data;

@Data
public class AddressDTO {

    private String street;
    private String city;
    private String state;
    private String country;
    private String zip;
}
