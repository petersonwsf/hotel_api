package com.hotel.hotel.modules.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.hotel.infra.security.TokenService;
import com.hotel.hotel.modules.user.dtos.UserJsonDTO;
import com.hotel.hotel.modules.user.dtos.UserLoginDTO;
import com.hotel.hotel.modules.user.dtos.UserSaveDTO;
import com.hotel.hotel.modules.user.model.User;
import com.hotel.hotel.modules.user.repository.UserRepository;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
        log.info("Starting login of user with user: {}", loginData.login());
        var token = new UsernamePasswordAuthenticationToken(loginData.login(), loginData.password());
        log.info("Authentication token generated");
        var authentication = manager.authenticate(token);
        log.info("Successfully authentication");
        var tokenJWT = tokenService.createToken((User) authentication.getPrincipal());
        log.info("Returning JWT Token");
        return ResponseEntity.ok(new UserJsonDTO(tokenJWT));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid UserSaveDTO data) {
        log.info("Starting login of user with user: {}", data.login());
        var user = new User(data);
        userRepository.save(user);
        log.info("User {} successfully created", data.login());
        var token = tokenService.createToken(user);
        log.info("Returning JWT Token");
        return ResponseEntity.ok(new UserJsonDTO(token));
    }
}
