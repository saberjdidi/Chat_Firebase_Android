package com.example.saber.chatfirebase.Models;

public class Chatlist {
    String id; //get Chatlist, sender/receiver uid

    public Chatlist() {
    }

    public Chatlist(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
