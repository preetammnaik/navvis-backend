package com.example.navvis.exceptions;

public class PointLocalizationException extends RuntimeException {
    public PointLocalizationException(String message) {
        super(message);
    }

    public PointLocalizationException(String message, Throwable cause) {
        super(message, cause);
    }
}
