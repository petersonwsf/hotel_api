package com.hotel.hotel.modules.reservation.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

public record ReservationSaveDTO(
    @NotNull
    @FutureOrPresent
    LocalDate checkInDate,
    @NotNull
    @FutureOrPresent
    LocalDate checkOutDate,
    @NotNull
    BigDecimal dailyRate,
    @NotNull
    BigDecimal discountAmount,
    @NotNull
    BigDecimal serviceFee,
    @NotNull
    Long userId,
    @NotNull
    Long roomId
) {}