package com.ahmadyosef.app.data;

import android.util.Log;

import com.ahmadyosef.app.Utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Comparator;

/*
Model data class: holds shift data
*/
public class Shift implements Comparable<Shift> {
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

    @Override
    public int compareTo(Shift shift) {
        return (LocalDate.parse(date)).compareTo(LocalDate.parse(shift.date));
    }
}
