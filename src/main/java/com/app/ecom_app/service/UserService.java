package com.app.ecom_app.service;

import com.app.ecom_app.model.User;
import com.app.ecom_app.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;

    public List<User> fetchAllUsers() {
        return userRepo.findAll();
    }

    public void addUser(User user) {
        userRepo.save(user);
    }

    public User fetchUserById(String id) {
//        fetch the user by id, filter then findFirst orElse null
        return userRepo.findById(id).orElse(null);
    }

    public boolean updateUser(String id, User UpdatedUser){
        User user = fetchUserById(id);
        if(user == null) return false;
        user.setFirstName(UpdatedUser.getFirstName());
        user.setLastName(UpdatedUser.getLastName());
        user.setEmail(UpdatedUser.getEmail());
        user.setPhone(UpdatedUser.getPhone());
        user.setPassword(UpdatedUser.getPassword());
        user.setAddress(UpdatedUser.getAddress());
        user.setRole(UpdatedUser.getRole());
        userRepo.save(user);
        return true;
    }
}
