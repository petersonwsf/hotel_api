package com.hotel.hotel.modules.room.dtos;

import java.math.BigDecimal;

import com.hotel.hotel.modules.room.model.Category;
import com.hotel.hotel.modules.room.model.Room;
import com.hotel.hotel.modules.room.model.StatusRoom;

public record RoomDetailsDTO(
        Long id,
        String code,
        String floor,
        BigDecimal customPrice,
        Boolean active,
        StatusRoom statusRoom,
        String bedconfig,
        String amenities,
        Integer capacity,
        Category category
) {
    public RoomDetailsDTO(Room room) {
        this(
                room.getId(),
                room.getCode(),
                room.getFloor(),
                room.getCustomPrice(),
                room.getActive(),
                room.getStatus(),
                room.getBedConfig(),
                room.getAmenities(),
                room.getCapacity(),
                room.getCategory()
        );
    }
}
