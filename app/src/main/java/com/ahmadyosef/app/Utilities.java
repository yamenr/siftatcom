package com.ahmadyosef.app;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.regex.Pattern;

enum ErrorCodes
{
    IncorrectAuth, FieldsEmpty, True, False
}

public class Utilities {
    private static Utilities instance;
    private final String restsStr = "restaurants";
    private final String usersStr = "users";

    public Utilities() {
    }

    public static Utilities getInstance() {
        if (instance == null)
            instance = new Utilities();

        return instance;
    }

    public Date convertDate(String dateStr)
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date date = null;
        try {
            date = format.parse(dateStr);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }
    // TODO: offline email and password validation
/*
    public boolean validateEmail(String username) {
        return true;
    }


    public boolean emailPatternMatches(String emailAddress, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }

    public boolean validatePassword(String password) {
        return true;
    }

    public boolean checkTrimEmpty(String text) {
        return text.trim().isEmpty();
    }

/*
    public boolean validateEmailPassword(String username, String password) {
        return
                emailPatternMatches(username, R.string.email_regex_pattern)&&
    }*/
}