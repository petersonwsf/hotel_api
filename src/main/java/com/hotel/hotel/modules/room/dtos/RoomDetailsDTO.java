package com.hotel.hotel.modules.room.dtos;

import java.math.BigDecimal;

import com.hotel.hotel.modules.room.model.Room;
import com.hotel.hotel.modules.room.model.StatusRoom;
import com.hotel.hotel.modules.roomTypes.dtos.RoomTypeDetailsDTO;

public record RoomDetailsDTO(Long id, String code, String floor, BigDecimal customPrice, Boolean active, StatusRoom statusRoom, RoomTypeDetailsDTO roomTypeDetails) {
    public RoomDetailsDTO(Room room) {
        this(room.getId(), room.getCode(), room.getFloor(), room.getCustomPrice(), room.getActive(), room.getStatus(), new RoomTypeDetailsDTO(room.getRoomType()));
    }
}
