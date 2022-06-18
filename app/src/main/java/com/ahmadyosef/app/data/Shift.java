package com.ahmadyosef.app.data;

public class Shift {
    private String id;
    private String date;
    private ShiftType type;

    public Shift() {
    }

    public Shift(String id, String date, ShiftType type) {
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

    public ShiftType getType() {
        return type;
    }

    public void setType(ShiftType type) {
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
