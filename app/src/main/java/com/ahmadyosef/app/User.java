package com.ahmadyosef.app;

import java.util.ArrayList;

public class User {
    private String id;
    private String startTime;
    private String lastTime;
    private String username;
    private String address;
    private String phone;
    private ArrayList<Shift> shifts;

    public User() {
    }

    public User(String id, String startTime, String lastTime, String username, String address, String phone) {
        this.id = id;
        this.startTime = startTime;
        this.lastTime = lastTime;
        this.username = username;
        this.address = address;
        this.phone = phone;
        this.shifts = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ArrayList<Shift> getShifts() {
        return shifts;
    }

    public void setShifts(ArrayList<Shift> shifts) {
        this.shifts = shifts;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", startTime='" + startTime + '\'' +
                ", lastTime='" + lastTime + '\'' +
                ", username='" + username + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", shifts=" + shifts +
                '}';
    }
}
