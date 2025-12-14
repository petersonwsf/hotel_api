package com.hotel.hotel.domain.user;

import jakarta.validation.constraints.NotBlank;

public record UserLoginDTO(
    @NotBlank
    String login,
    @NotBlank 
    String password
) {}
