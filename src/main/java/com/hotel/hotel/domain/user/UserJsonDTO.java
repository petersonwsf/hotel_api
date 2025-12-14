package com.hotel.hotel.domain.user;

import jakarta.validation.constraints.NotBlank;

public record UserJsonDTO(
    @NotBlank
    String token
) {}
