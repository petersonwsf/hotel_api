package com.hotel.hotel.modules.reservation.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.hotel.hotel.modules.reservation.model.Status;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.FutureOrPresent;

public record ReservationEditDTO(
    @FutureOrPresent(message = "A data de checkIn deve estar no futuro ou momento presente")
    LocalDate checkInDate,
    @FutureOrPresent(message = "A data de checkOut deve estar no futuro ou momento presente")
    LocalDate checkOutDate,
    BigDecimal discountAmount,
    BigDecimal serviceFee,
    Status status,
    Long roomId
) {
    @AssertTrue(message = "A data de check-out deve ser posterior à data de check-in")
    public boolean isPeriodoValido() {
        if (checkInDate() == null || checkOutDate() == null) {
            return true;
        }
        return checkOutDate().isAfter(checkInDate);
    }
}
