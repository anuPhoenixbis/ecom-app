package com.app.ecom_app.utils;

import com.app.ecom_app.dto.AddressDTO;
import com.app.ecom_app.dto.UserRequest;
import com.app.ecom_app.dto.UserResponse;
import com.app.ecom_app.model.Address;
import com.app.ecom_app.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class UserMappers {

    private final PasswordEncoder passwordEncoder;

    public UserResponse mapToUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setEmail(user.getEmail());
        userResponse.setPhone(user.getPhone());
        userResponse.setRole(user.getRole());

        if(user.getAddress() != null) {
            AddressDTO addressDTO = new AddressDTO();
            addressDTO.setCity(user.getAddress().getCity());
            addressDTO.setCountry(user.getAddress().getCountry());
            addressDTO.setStreet(user.getAddress().getStreet());
            addressDTO.setZip(user.getAddress().getZip());
            addressDTO.setState(user.getAddress().getState());
            userResponse.setAddress(addressDTO);
        }
        return userResponse;
    }

    public void updateUserFromRequest(User user, UserRequest userRequest){
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));


        Address address = user.getAddress() != null ? user.getAddress() : new Address();
        address.setCity(userRequest.getAddress().getCity());
        address.setCountry(userRequest.getAddress().getCountry());
        address.setStreet(userRequest.getAddress().getStreet());
        address.setZip(userRequest.getAddress().getZip());
        address.setState(userRequest.getAddress().getState());
        user.setAddress(address);
    }
}
