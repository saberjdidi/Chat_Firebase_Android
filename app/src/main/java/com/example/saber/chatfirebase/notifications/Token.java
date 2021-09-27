package com.example.saber.chatfirebase.notifications;

public class Token {
    //An FCM Token, or much commonly known as a registration token
    String token;

    public Token(String token) {
        this.token = token;
    }

    public Token() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
