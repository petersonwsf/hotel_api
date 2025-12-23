package com.hotel.hotel.domain.dtos;

public record MessageResponse(String message) {
    public MessageResponse(String message) {
        this.message = message;
    }
}