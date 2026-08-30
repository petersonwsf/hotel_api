package com.hotel.hotel.modules.room.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.hotel.hotel.modules.room.model.Category;
import com.hotel.hotel.modules.room.model.StatusRoom;
import jakarta.validation.constraints.FutureOrPresent;

public record RoomFiltersDTO(
    @FutureOrPresent
    LocalDate checkInDate,
    @FutureOrPresent
    LocalDate checkOutDate,
    String code,
    List<String> floor,
    BigDecimal minPrice,
    BigDecimal maxPrice,
    List<StatusRoom> status,
    Boolean active,
    Integer capacity,
    List<Category> category
) {}
