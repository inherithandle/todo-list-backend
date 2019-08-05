package com.gtchoi.todolistbackend.exception;

public class UnAuthorizedException extends RuntimeException {


    public static final String MESSAGE = "either token or user credentials is not valid.";

}
