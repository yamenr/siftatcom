package com.ahmadyosef.app;

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