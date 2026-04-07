package com.hotel.hotel.modules.reservation.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.hotel.hotel.modules.client.dtos.ClientDetailsDTO;
import com.hotel.hotel.modules.reservation.model.Reservation;
import com.hotel.hotel.modules.reservation.model.Source;
import com.hotel.hotel.modules.reservation.model.Status;
import com.hotel.hotel.modules.room.dtos.RoomDetailsDTO;

public record ReservationDetailsDTO(
    Long id,
    LocalDate checkInDate,
    LocalDate checkOutDate,
    BigDecimal dailyRate,
    BigDecimal discountAmount,
    BigDecimal totalAmount,
    BigDecimal serviceFee,
    Status status,
    Source source,
    RoomDetailsDTO room,
    ClientDetailsDTO client
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
            reservation.getSource(),
            new RoomDetailsDTO(reservation.getRoom()),
            new ClientDetailsDTO(reservation.getClient())
        );
    }
}
