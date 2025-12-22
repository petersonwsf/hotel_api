package com.hotel.hotel.infra.exceptions;

public class ResourceAlreadyExists  extends RuntimeException {
    public ResourceAlreadyExists(String message) {
        super(message);
    }
    
}
