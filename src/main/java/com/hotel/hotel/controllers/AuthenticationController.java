package com.hotel.hotel.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.hotel.domain.user.User;
import com.hotel.hotel.domain.user.UserJsonDTO;
import com.hotel.hotel.domain.user.UserLoginDTO;
import com.hotel.hotel.domain.user.UserRepository;
import com.hotel.hotel.domain.user.UserSaveDTO;
import com.hotel.hotel.infra.security.TokenService;

import jakarta.validation.Valid;

@RestController
public class AuthenticationController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private UserRepository userRepository;
    
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid UserLoginDTO loginData) {
        var token = new UsernamePasswordAuthenticationToken(loginData.login(), loginData.password());
        var authentication = manager.authenticate(token);
        var tokenJWT = tokenService.createToken((User) authentication.getPrincipal());
        
        return ResponseEntity.ok(new UserJsonDTO(tokenJWT));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid UserSaveDTO data) {
        var user = new User(data);
        userRepository.save(user);
        var token = tokenService.createToken(user);

        return ResponseEntity.ok(new UserJsonDTO(token));
    }
}
