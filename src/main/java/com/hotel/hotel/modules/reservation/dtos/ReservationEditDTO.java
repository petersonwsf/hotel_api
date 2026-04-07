package com.hotel.hotel.modules.reservation.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.hotel.hotel.modules.reservation.model.Status;

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
