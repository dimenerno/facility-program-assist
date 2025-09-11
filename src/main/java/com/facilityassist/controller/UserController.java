package com.facilityassist.controller;

import com.facilityassist.model.User;
import com.facilityassist.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/managers")
    public List<User> getManagers() {
        return userRepository.findByRole(com.facilityassist.model.UserRole.MANAGER);
    }
}
