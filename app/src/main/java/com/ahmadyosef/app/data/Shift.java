package com.ahmadyosef.app.data;

enum shiftType {
    Morning, Afternoon, Night
}

public class Shift {
    private String id;
    private String date;
    private shiftType type;

    public Shift() {
    }

    public Shift(String id, String date, shiftType type) {
        this.id = id;
        this.date = date;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public shiftType getType() {
        return type;
    }

    public void setType(shiftType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Shift{" +
                "id='" + id + '\'' +
                ", date='" + date + '\'' +
                ", type=" + type +
                '}';
    }
}
