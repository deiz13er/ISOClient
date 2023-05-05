package com.example.ISO8583.exceptions;

public class ISOException extends Exception {

    public ISOException(String message) {
        super(message);
    }

    public ISOException(String message, Throwable cause) {
        super(message, cause);
    }
}
