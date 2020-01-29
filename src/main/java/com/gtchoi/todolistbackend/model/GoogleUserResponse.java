package com.gtchoi.todolistbackend.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GoogleUserResponse {
    /*
        {
          "iss": "https://accounts.google.com",
          "azp": "1234987819200.apps.googleusercontent.com",
          "aud": "1234987819200.apps.googleusercontent.com",
          "sub": "10769150350006150715113082367",
          "at_hash": "HK6E_P6Dh8Y93mRNtsDB1Q",
          "hd": "example.com",
          "email": "jsmith@example.com",
          "email_verified": "true",
          "iat": 1353601026,
          "exp": 1353604926,
          "nonce": "0394852-3190485-2490358"
        }
     */

    private String iss;
    private String azp;
    private String aud;
    private String sub;
    @JsonProperty("at_hash")
    private String atHash;
    private String hd;
    private String email;
    @JsonProperty("email_verified")
    private boolean emailVerified;
    private int iat;
    private int exp;
    private String nonce;

    public String getIss() {
        return iss;
    }

    public void setIss(String iss) {
        this.iss = iss;
    }

    public String getAzp() {
        return azp;
    }

    public void setAzp(String azp) {
        this.azp = azp;
    }

    public String getAud() {
        return aud;
    }

    public void setAud(String aud) {
        this.aud = aud;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getAtHash() {
        return atHash;
    }

    public void setAtHash(String atHash) {
        this.atHash = atHash;
    }

    public String getHd() {
        return hd;
    }

    public void setHd(String hd) {
        this.hd = hd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public int getIat() {
        return iat;
    }

    public void setIat(int iat) {
        this.iat = iat;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }
}
