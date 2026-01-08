package com.hotel.hotel.domain.client;

public record ClientFilter(
    String name, 
    String pin, 
    String email, 
    String phoneNumber, 
    Boolean deleted
) {}
