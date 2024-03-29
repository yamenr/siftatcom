package com.ahmadyosef.app.data;

import java.util.ArrayList;

/*
Model data class: holds user data (in addition to authentication )
*/
public class User {
    private String id;
    private String name;
    private String username;
    private String address;
    private String phone;
    private String photo;
    private UserType type;
    private ArrayList<Shift> shifts;

    public User() {
    }

    public User(String id, String name, String username, String address, String phone, String photo, UserType type) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.address = address;
        this.phone = phone;
        this.photo = photo;
        this.type = type;
        this.shifts = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
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
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", photo='" + photo + '\'' +
                ", type=" + type +
                ", shifts=" + shifts +
                '}';
    }
}
