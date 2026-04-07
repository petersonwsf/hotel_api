package com.hotel.hotel.modules.user.dtos;

import jakarta.validation.constraints.NotBlank;

public record UserLoginDTO(
    @NotBlank
    String login,
    @NotBlank 
    String password
) {}
