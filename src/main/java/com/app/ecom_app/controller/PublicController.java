package com.app.ecom_app.controller;

import com.app.ecom_app.dto.UserLoginDTO;
import com.app.ecom_app.dto.UserLoginResponse;
import com.app.ecom_app.dto.UserRequest;
import com.app.ecom_app.model.User;
import com.app.ecom_app.repository.UserRepo;
import com.app.ecom_app.service.UserService;
import com.app.ecom_app.utils.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class PublicController {

    private final UserRepo userRepo;

    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    private final JwtUtil jwtUtil;

    @GetMapping("/health-check")
    public String heathCheck(){
        return "ok";
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<String> createUser(@Valid @RequestBody UserRequest user) {
//        no conflicts checking as the id is auto-generated
        userService.addUser(user);
        return ResponseEntity.ok("User created");
    }

    @PostMapping("/auth/login")
    public ResponseEntity<UserLoginResponse> login(@Valid @RequestBody UserLoginDTO dto) {
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));

            Optional<User> userOpt = userRepo.findByEmail(dto.getEmail());

            if(userOpt.isPresent()){
                User user = userOpt.get();
                String jwt = jwtUtil.generateToken(user);

                UserLoginResponse response = new UserLoginResponse();
                response.setToken(jwt);
                response.setEmail(user.getEmail());
                response.setRole(user.getRole());

                return ResponseEntity.ok(response);

            }else{
                return ResponseEntity.badRequest().build();
            }

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
