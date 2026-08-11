package com.hotel.hotel.modules.reservation.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.hotel.hotel.modules.reservation.model.Reservation;
import com.hotel.hotel.modules.reservation.model.Status;
import com.hotel.hotel.modules.room.dtos.RoomDetailsImageDTO;
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
    RoomDetailsImageDTO room,
    UserResponseDTO user
) {
    public ReservationDetailsDTO(Reservation reservation, RoomDetailsImageDTO room) {
        this(
            reservation.getId(),
            reservation.getCheckInDate(),
            reservation.getCheckOutDate(),
            reservation.getDailyRate(),
            reservation.getDiscountAmount(),
            reservation.getTotalAmount(),
            reservation.getServiceFee(),
            reservation.getStatus(),
            room,
            new UserResponseDTO(reservation.getUser())
        );
    }
}
