package com.hotel.hotel.modules.roomTypes.dtos;

import java.math.BigDecimal;

import com.hotel.hotel.modules.roomTypes.model.Category;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RoomTypeSaveDTO(
    
    @NotBlank
    String name, 
    
    @Min(1)
    Integer capacity, 

    @NotNull
    @DecimalMin("100.00")
    BigDecimal basePrice,
    
    @NotBlank
    String bedConfig,
    
    @NotBlank
    String amenities,
    
    @NotNull
    Category category
    
) {}
