package com.hotel.hotel.modules.room.dtos;

import java.math.BigDecimal;

import com.hotel.hotel.modules.room.model.Category;
import com.hotel.hotel.modules.room.model.StatusRoom;

import jakarta.validation.constraints.*;

public record RoomSaveDTO(

    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9]*$")
    String code,

    @NotBlank
    String floor,
    
    @NotNull
    StatusRoom status,
    
    @NotNull
    @DecimalMin("100.00")
    BigDecimal customPrice,

    @NotBlank
    String bedconfig,

    @NotBlank
    String amenities,

    @NotNull
    @Min(value = 1)
    Integer capacity,

    @NotNull
    Category category

) {}
