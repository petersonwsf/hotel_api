package com.hotel.hotel.domain.user;

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