package com.hotel.hotel.modules.reservation.dtos;

import java.time.LocalDate;
import java.util.List;

import com.hotel.hotel.modules.reservation.model.Status;

public record ReservationFilters(
    LocalDate checkInDate, 
    LocalDate checkOutDate, 
    List<Status> status,
    Long user,
    Long room
) {}
