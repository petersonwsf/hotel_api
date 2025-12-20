package com.hotel.hotel.domain.reservation;

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
    Status status,
    @NotNull
    Source source,
    @NotNull
    Long clientId,
    @NotNull
    Long roomId
) {}