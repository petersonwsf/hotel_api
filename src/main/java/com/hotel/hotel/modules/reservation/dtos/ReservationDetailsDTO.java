package com.hotel.hotel.modules.reservation.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.hotel.hotel.modules.reservation.model.Reservation;
import com.hotel.hotel.modules.reservation.model.Status;
import com.hotel.hotel.modules.room.dtos.RoomDetailsDTO;
import com.hotel.hotel.modules.user.dtos.UserResponseDTO;

public record ReservationDetailsDTO(
    Long id,
    LocalDate checkInDate,
    LocalDate checkOutDate,
    BigDecimal dailyRate,
    BigDecimal discountAmount,
    BigDecimal totalAmount,
    BigDecimal serviceFee,
    Status status,
    RoomDetailsDTO room,
    UserResponseDTO user
) {
    public ReservationDetailsDTO(Reservation reservation) {
        this(
            reservation.getId(),
            reservation.getCheckInDate(),
            reservation.getCheckOutDate(),
            reservation.getDailyRate(),
            reservation.getDiscountAmount(),
            reservation.getTotalAmount(),
            reservation.getServiceFee(),
            reservation.getStatus(),
            new RoomDetailsDTO(reservation.getRoom()),
            new UserResponseDTO(reservation.getUser())
        );
    }
}
