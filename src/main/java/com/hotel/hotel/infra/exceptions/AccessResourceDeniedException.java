package com.hotel.hotel.infra.exceptions;

public class AccessResourceDeniedException extends RuntimeException {
    public AccessResourceDeniedException(String message) {
        super(message);
    }
}
