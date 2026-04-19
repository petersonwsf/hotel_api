package com.hotel.hotel.modules.roomTypes.dtos;

import java.math.BigDecimal;

import com.hotel.hotel.modules.roomTypes.model.Category;
import com.hotel.hotel.modules.roomTypes.model.RoomType;

public record RoomTypeDetailsDTO(Long id , String name, Integer capacity, BigDecimal basePrice, String bedConfig, String amenities, Category category) {
    public RoomTypeDetailsDTO(RoomType room) {
        this(room.getId() ,room.getName(), room.getCapacity(), room.getBasePrice(), room.getBedConfig(), room.getAmenities(), room.getCategory());
    }
}
