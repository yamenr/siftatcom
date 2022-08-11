package com.ahmadyosef.app.data;

public class ShiftUser {

    String username;
    String date;
    ShiftType type;

    public ShiftUser() {
    }

    public ShiftUser(String username, String date, ShiftType type) {
        this.username = username;
        this.date = date;
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ShiftType getType() {
        return type;
    }

    public void setType(ShiftType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ShiftUser{" +
                "username='" + username + '\'' +
                ", date='" + date + '\'' +
                ", type=" + type +
                '}';
    }
}
