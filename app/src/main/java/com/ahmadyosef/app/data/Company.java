package com.ahmadyosef.app.data;

import java.util.ArrayList;

public class Company {
    private String id;
    private String name;
    private String username;
    private String address;
    private String phone;
    private String photo;
    private ArrayList<String> usernames;

    public Company() {
    }

    public Company(String id, String name, String username, String address, String phone, String photo) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.address = address;
        this.phone = phone;
        this.photo = photo;
        this.usernames = new ArrayList<>();
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

    public ArrayList<String> getUsers() {
        return usernames;
    }

    public void setUsers(ArrayList<String> users) {
        this.usernames = users;
    }

    @Override
    public String toString() {
        return "Company{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", photo='" + photo + '\'' +
                ", users=" + usernames +
                '}';
    }
}
