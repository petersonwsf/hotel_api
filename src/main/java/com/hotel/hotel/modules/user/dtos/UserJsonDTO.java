package com.hotel.hotel.modules.user.dtos;

import jakarta.validation.constraints.NotBlank;

public record UserJsonDTO(
    @NotBlank
    String token
) {}
