package com.gtchoi.todolistbackend.entity;

import javax.persistence.*;

@Entity
public class UserToken {

    @Id
    private String accessToken;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userNo")
    private User user;

    public UserToken(String accessToken, User user) {
        this.accessToken = accessToken;
        this.user = user;
    }

    public UserToken() {

    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
