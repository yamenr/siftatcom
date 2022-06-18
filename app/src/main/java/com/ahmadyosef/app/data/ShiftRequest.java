package com.ahmadyosef.app.data;

public class ShiftRequest {
    private String username;
    private Shift shift;

    public ShiftRequest() {
    }

    public ShiftRequest(String username, Shift shift) {
        this.username = username;
        this.shift = shift;
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

    @Override
    public String toString() {
        return "ShiftRequest{" +
                "username='" + username + '\'' +
                ", shift=" + shift +
                '}';
    }
}
