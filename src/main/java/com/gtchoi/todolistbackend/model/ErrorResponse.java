package com.gtchoi.todolistbackend.model;

public class ErrorResponse {

    private int status;

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ErrorResponse(int status) {
        this.status = status;
    }
}
