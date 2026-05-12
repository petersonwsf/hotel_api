package com.hotel.hotel.modules.reservation.dtos;

import java.time.LocalDate;

import com.hotel.hotel.modules.reservation.model.Source;
import com.hotel.hotel.modules.reservation.model.Status;

public record ReservationFilters(
    LocalDate checkInDate, 
    LocalDate checkOutDate, 
    Status status, 
    Source source, 
    Long user,
    Long room
) {}
