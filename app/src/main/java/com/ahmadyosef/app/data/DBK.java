package com.ahmadyosef.app.data;

public class DBK {

    private String key;
    private String value;

    public DBK() {
    }

    public DBK(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "DBK{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
