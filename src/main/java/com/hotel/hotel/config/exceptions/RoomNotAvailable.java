package com.hotel.hotel.config.exceptions;

public class RoomNotAvailable extends RuntimeException {
    public RoomNotAvailable(String message) {
        super(message);
    } 
}
