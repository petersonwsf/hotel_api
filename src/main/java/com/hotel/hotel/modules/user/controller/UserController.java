package com.hotel.hotel.modules.user.controller;

import com.hotel.hotel.modules.user.dtos.UserLoginDTO;
import com.hotel.hotel.modules.user.dtos.UserFilters;
import com.hotel.hotel.modules.user.dtos.UserEditDTO;
import com.hotel.hotel.modules.user.dtos.UserSaveDTO;
import com.hotel.hotel.modules.user.dtos.UserResponseDTO;
import com.hotel.hotel.modules.user.dtos.UserJsonDTO;
import com.hotel.hotel.modules.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.hotel.hotel.modules.user.model.User;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService service;
    
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid UserLoginDTO loginData) {
        log.info("Starting login of user with user: {}", loginData.login());
        String token = service.login(loginData);
        log.info("Returning JWT Token");
        return ResponseEntity.ok(new UserJsonDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid UserSaveDTO data) {
        log.info("Starting login of user with user: {}", data.login());
        String token = service.register(data);
        log.info("Returning JWT Token");
        return ResponseEntity.ok(new UserJsonDTO(token));
    }

    @GetMapping
    public ResponseEntity list(UserFilters filters, Pageable pagination) {
        log.info("Request to list user");
        var users = service.list(pagination, filters).map(UserResponseDTO::new);
        log.info("Returning users list");
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity findById(@PathVariable long id) {
        log.info("Request to find user by ID: {}", id);
        User user = service.findById(id);
        return ResponseEntity.ok(new UserResponseDTO(user));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity delete(@PathVariable Long id) {
        log.info("Delete user by Id: {}", id);
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    @Transactional
    public ResponseEntity update(@RequestBody UserEditDTO userData, @PathVariable Long id) {
        log.info("Edit user by Id: {}", id);
        User user = service.edit(userData, id);
        log.info("User by id: {} successfully edited", id);
        return ResponseEntity.ok(new UserResponseDTO(user));
    }

}
