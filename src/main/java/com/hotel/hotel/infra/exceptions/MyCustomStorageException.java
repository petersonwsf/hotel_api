package com.hotel.hotel.infra.exceptions;

public class MyCustomStorageException extends RuntimeException {
    public MyCustomStorageException(String message) {
        super(message);
    }
}
