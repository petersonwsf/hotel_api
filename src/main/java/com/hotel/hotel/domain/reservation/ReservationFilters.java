package com.hotel.hotel.domain.reservation;

import java.time.LocalDate;

public record ReservationFilters(
    LocalDate checkInDate, 
    LocalDate checkOutDate, 
    Status status, 
    Source source, 
    Long client, 
    Long room
) {}
