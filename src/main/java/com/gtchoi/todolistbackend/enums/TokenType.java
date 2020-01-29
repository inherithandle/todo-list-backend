package com.gtchoi.todolistbackend.enums;

public enum TokenType {
    GOOGLE("google"),
    NAVER("naver");


    private String value;

    TokenType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}