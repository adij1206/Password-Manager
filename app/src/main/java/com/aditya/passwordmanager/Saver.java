package com.aditya.passwordmanager;

public class Saver {

    private String name,userId,password,date;

    public Saver(){}

    public Saver(String name, String userId, String password, String date) {
        this.name = name;
        this.userId = userId;
        this.password = password;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
