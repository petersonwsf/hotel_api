package com.hotel.hotel.modules.room.dtos;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import com.hotel.hotel.modules.files.dto.FileResponse;
import com.hotel.hotel.modules.room.model.Category;
import com.hotel.hotel.modules.room.model.Room;
import com.hotel.hotel.modules.room.model.StatusRoom;

public record RoomListDTO(
        Long id,
        String code,
        String floor,
        BigDecimal customPrice,
        Boolean active,
        StatusRoom statusRoom,
        List<String> amenities,
        Integer capacity,
        Category category,
        FileResponse images
) {
    public RoomListDTO(Room room, FileResponse images) {
        this(
                room.getId(),
                room.getCode(),
                room.getFloor(),
                room.getCustomPrice(),
                room.getActive(),
                room.getStatus(),
                Arrays.asList(room.getAmenities()),
                room.getCapacity(),
                room.getCategory(),
                images
        );
    }
}
