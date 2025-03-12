package com.timur.taskmanagement.exceptions;

public class NoAccessException extends RuntimeException {
    public NoAccessException(String message) {
        super(message);
    }
}