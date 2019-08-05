package com.gtchoi.todolistbackend.exception;

public class InternalServerErrorException extends RuntimeException {
    private String message;

    public InternalServerErrorException(String message) {
        this.message = message;
    }
}
