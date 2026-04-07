package com.hotel.hotel.infra.dtos;

public record MessageResponse(String message) {
    public MessageResponse(String message) {
        this.message = message;
    }
}