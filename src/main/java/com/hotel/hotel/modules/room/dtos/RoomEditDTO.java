package com.hotel.hotel.modules.room.dtos;

import java.math.BigDecimal;

import com.hotel.hotel.modules.room.model.StatusRoom;

import jakarta.validation.constraints.DecimalMin;

public record RoomEditDTO(
    StatusRoom status,
    @DecimalMin("100.00")
    BigDecimal customPrice,
    Long roomType
) {
}
