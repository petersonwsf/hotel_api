package com.hotel.hotel.modules.room.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.hotel.hotel.modules.room.model.StatusRoom;

public record RoomFilters(
    LocalDate checkInDate,
    LocalDate checkOutDate,
    String code,
    String floor,
    Long roomTypeId,
    BigDecimal minPrice,
    BigDecimal maxPrice,
    StatusRoom status,
    Boolean active
) {}
