package com.ahmadyosef.app.data;

public class ShiftRequest {

    private String username;
    private Shift shift;
    private ShiftRequestType type;

    public ShiftRequest() {
    }

    public ShiftRequest(String username, Shift shift, ShiftRequestType type) {
        this.username = username;
        this.shift = shift;
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public ShiftRequestType getType() {
        return type;
    }

    public void setType(ShiftRequestType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ShiftRequest{" +
                "username='" + username + '\'' +
                ", shift=" + shift +
                ", type=" + type +
                '}';
    }
}
