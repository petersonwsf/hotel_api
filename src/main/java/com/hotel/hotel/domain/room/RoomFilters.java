package com.hotel.hotel.domain.room;

import java.math.BigDecimal;
import java.time.LocalDate;

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
