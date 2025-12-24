package com.hotel.hotel.infra.exceptions;

public class RoomNotAvailable extends RuntimeException {
    public RoomNotAvailable(String message) {
        super(message);
    } 
}
