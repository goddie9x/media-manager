package com.god.MediaManager.service;

import com.god.MediaManager.DTO.PaginationRequest;
import com.god.MediaManager.model.auth.User;
import com.god.MediaManager.repository.auth.*;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@Service
public class AuthService {
    @Autowired
    private  UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public boolean registerUser(User user) {
        if(userRepository.findByUsername(user.getUsername())!=null){
            return false;
        }
        user.setAdmin(false);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }
    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username);
    }

    public boolean updateCurrentUser(User updatedUser) {
        User currentUser = getCurrentUser();
        if(currentUser.getUsername().equals(updatedUser.getUsername())) {
            currentUser.importUserData(updatedUser);
            currentUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            userRepository.save(currentUser);
            return true;
        }
        else{
            return false;
        }
    }
    public Page<User> getAccountsWithPagination(
           @Nullable PaginationRequest paginationRequest
    ) {
        return userRepository.findAll(
                PaginationRequest.getPageRequest(paginationRequest,"")
        );
    }

    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }}


