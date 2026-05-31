package com.hotel.hotel.modules.room.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.hotel.hotel.modules.room.model.Category;
import com.hotel.hotel.modules.room.model.StatusRoom;
import jakarta.validation.constraints.FutureOrPresent;

public record RoomFiltersDTO(
    @FutureOrPresent
    LocalDate checkInDate,
    @FutureOrPresent
    LocalDate checkOutDate,
    String code,
    String floor,
    BigDecimal minPrice,
    BigDecimal maxPrice,
    StatusRoom status,
    Boolean active,
    Integer capacity,
    Category category
) {}
