package com.hotel.hotel.modules.room.dtos;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record VerifyDisponibilityDTO(
        @NotNull
        @FutureOrPresent(message = "A data de checkIn deve estar no futuro ou momento presente")
        LocalDate checkIn,
        @NotNull
        @FutureOrPresent(message = "A data de checkOut deve estar no futuro ou momento presente")
        LocalDate checkOut
) {
    @AssertTrue(message = "A data de check-out deve ser posterior à data de check-in")
    public boolean isPeriodoValido() {
        if (checkIn == null || checkOut == null) {
            return true;
        }
        return checkOut.isAfter(checkIn);
    }
}
