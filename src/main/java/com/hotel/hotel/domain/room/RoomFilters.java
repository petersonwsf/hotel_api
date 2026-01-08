package com.hotel.hotel.domain.room;

import java.math.BigDecimal;

public record RoomFilters(
    String code,
    String floor,
    Long roomTypeId,
    BigDecimal minPrice,
    BigDecimal maxPrice,
    StatusRoom status,
    Boolean active
) {}
