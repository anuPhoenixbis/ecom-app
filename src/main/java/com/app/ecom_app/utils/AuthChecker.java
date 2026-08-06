package com.app.ecom_app.utils;

import com.app.ecom_app.enums.UserRole;
import com.app.ecom_app.model.User;
import com.app.ecom_app.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@RequiredArgsConstructor
public class AuthChecker {

    private final UserRepo userRepo;

    public Optional<User> checkAuth(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth == null || !auth.isAuthenticated()) return Optional.empty();

        String email = auth.getName();

        if(email==null){
            return Optional.empty();
        }
        Optional<User> userOpt = userRepo.findByEmail(email);

        if(!userOpt.isPresent()){
            return Optional.empty();
        }

        return userOpt;
    }
}
