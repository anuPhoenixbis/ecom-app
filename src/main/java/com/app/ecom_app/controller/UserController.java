package com.app.ecom_app.controller;

import com.app.ecom_app.enums.UserRole;
import com.app.ecom_app.model.User;
import com.app.ecom_app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.fetchAllUsers();
        if (users.isEmpty()) {
            return new ResponseEntity<>(users, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        User user = userService.fetchUserById(id);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody User user) {
//        no conflicts checking as the id is auto-generated
        if(user.getRole() == null) user.setRole(UserRole.CUSTOMER);
        userService.addUser(user);
        return ResponseEntity.ok("User created");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable String id,@RequestBody User user) {
        if(userService.fetchUserById(id) == null) return new ResponseEntity<>(HttpStatus.CONFLICT);

        boolean isSuccessful = userService.updateUser(id,user);
        if(isSuccessful) return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
