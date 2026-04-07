package com.hotel.hotel.modules.client.dtos;

public record ClientFilter(
    String name, 
    String pin, 
    String email, 
    String phoneNumber, 
    Boolean deleted
) {}
