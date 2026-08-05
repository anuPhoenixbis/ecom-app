package com.app.ecom_app.service;

import com.app.ecom_app.dto.AddressDTO;
import com.app.ecom_app.dto.UserRequest;
import com.app.ecom_app.dto.UserResponse;
import com.app.ecom_app.enums.UserRole;
import com.app.ecom_app.model.Address;
import com.app.ecom_app.model.User;
import com.app.ecom_app.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;

    private final PasswordEncoder passwordEncoder;

    public List<UserResponse> fetchAllUsers() {
        return userRepo.findAll().stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    public void addUser(UserRequest user) {
        User userEntity = new User();
        updateUserFromRequest(userEntity,user);
        userRepo.save(userEntity);
    }

    public UserResponse fetchUserById(String id) {
//        fetch the user by id, filter then findFirst orElse null
        User user = userRepo.findById(id).orElse(null);
        return user!=null?mapToUserResponse(user):null;
    }

    public boolean updateUser(String id, UserRequest UpdatedUserRequest){
        User user = userRepo.findById(id).orElse(null);
        if(user == null) return false;
        updateUserFromRequest(user,UpdatedUserRequest);
        userRepo.save(user);
        return true;
    }

    private UserResponse mapToUserResponse(User user) {
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

    private void updateUserFromRequest(User user, UserRequest userRequest) {
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
