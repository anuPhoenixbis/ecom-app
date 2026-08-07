package com.app.ecom_app.controller;

import com.app.ecom_app.dto.AddressDTO;
import com.app.ecom_app.dto.UserRequest;
import com.app.ecom_app.dto.UserResponse;
import com.app.ecom_app.model.User;
import com.app.ecom_app.repository.UserRepo;
import com.app.ecom_app.service.UserService;
import com.app.ecom_app.utils.AuthChecker;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final UserRepo userRepo;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {
        AuthChecker auth = new AuthChecker(userRepo);
        Optional<User> userOpt = auth.checkAuth();
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            UserResponse userResponse = new UserResponse();
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
            return new ResponseEntity<>(userResponse, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/me")
    public ResponseEntity<String> updateUser(@Valid @RequestBody UserRequest user) {
        AuthChecker auth = new AuthChecker(userRepo);
        Optional<User> userOpt = auth.checkAuth();
        if(!userOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String id = userOpt.get().getId();

        if(userService.fetchUserById(id) == null) return new ResponseEntity<>(HttpStatus.CONFLICT);

        boolean isSuccessful = userService.updateUser(id,user);
        if(isSuccessful) return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
