package com.ahmadyosef.app.data;

import com.ahmadyosef.app.Utilities;

import java.util.Date;
import java.time.format.DateTimeFormatter;

public class Shift /*implements Comparable*/{
    private String id;
    private String date;
    private ShiftType type;
    private Utilities utils;

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

    /* TODO: sorting by date
    @Override
    public int compareTo(Object o) {
        Shift shift = (Shift)o;
        Date d = Utilities.getInstance().convertDate(shift.getDate());
        return (int) (d.getTime()/1000);
    } */
}
