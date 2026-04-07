package com.hotel.hotel.modules.user.dtos;

import com.hotel.hotel.modules.user.model.Role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserSaveDTO(
    @NotBlank
    String name,
    @NotBlank 
    String login,
    @NotBlank 
    String password,
    @NotNull 
    Role role
) {

}