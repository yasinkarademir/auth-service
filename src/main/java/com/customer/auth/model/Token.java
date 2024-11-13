package com.customer.auth.model;

public class Token {
    private String token;
    private String refreshToken;
    private String userName;

    public Token(String token, String refreshToken, String userName) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.userName = userName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
