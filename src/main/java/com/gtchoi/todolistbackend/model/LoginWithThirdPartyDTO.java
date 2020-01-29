package com.gtchoi.todolistbackend.model;

import com.gtchoi.todolistbackend.enums.TokenType;

public class LoginWithThirdPartyDTO {

    private TokenType tokenType;
    private String authorizationCode;

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

}
