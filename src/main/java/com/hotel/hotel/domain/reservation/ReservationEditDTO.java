package com.hotel.hotel.domain.reservation;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.FutureOrPresent;

public record ReservationEditDTO(
    @FutureOrPresent
    LocalDate checkInDate,
    @FutureOrPresent
    LocalDate checkOutDate,
    BigDecimal discountAmount,
    BigDecimal serviceFee,
    Status status,
    Long roomId
) {}
