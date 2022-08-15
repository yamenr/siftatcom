package com.ahmadyosef.app.data;

import android.util.Log;

import com.ahmadyosef.app.Utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Comparator;

public class Shift implements Comparable<Shift> {
    private String id;
    private String date;
    private ShiftType type;
    private Utilities utils;
    private static final String TAG = "AdminFragment";

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

    /*
    @Override
    public int compare(Shift shift, Shift t1) {
        return (LocalDate.parse(shift.date)).compareTo(LocalDate.parse(t1.date));
    } */

    @Override
    public int compareTo(Shift shift) {
        return (LocalDate.parse(date)).compareTo(LocalDate.parse(shift.date));
    }

    /* TODO: sorting by date
    @Override
    public int compareTo(Object o) {
        Shift shift = (Shift)o;
        Date d = Utilities.getInstance().convertDate(shift.getDate());
        return (int) (d.getTime()/1000);
    } */
}
