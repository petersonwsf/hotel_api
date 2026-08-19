package com.hotel.hotel.config.exceptions;

public class MyCustomStorageException extends RuntimeException {
    public MyCustomStorageException(String message) {
        super(message);
    }
}
