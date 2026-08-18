package com.hotel.hotel.config.exceptions;

public class AccessResourceDeniedException extends RuntimeException {
    public AccessResourceDeniedException(String message) {
        super(message);
    }
}
