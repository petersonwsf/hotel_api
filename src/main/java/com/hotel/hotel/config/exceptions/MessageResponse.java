package com.hotel.hotel.config.exceptions;

public record MessageResponse(String message) {
    public MessageResponse(String message) {
        this.message = message;
    }
}