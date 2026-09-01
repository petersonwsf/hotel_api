package com.hotel.hotel.modules.reservation.dtos;

import java.time.LocalDate;
import java.util.List;

import com.hotel.hotel.modules.reservation.model.Status;
import com.hotel.hotel.modules.room.model.Category;

public record ReservationFilters(
    LocalDate checkInDate, 
    LocalDate checkOutDate, 
    List<Status> status,
    List<Category> category,
    List<String> floor,
    String guestName
) {}
