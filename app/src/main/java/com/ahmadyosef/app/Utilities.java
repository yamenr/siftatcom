package com.ahmadyosef.app;

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

    public boolean validateEmail(String username) {
        return true;
    }

    public boolean validatePassword(String password) {
        return true;
    }

    public boolean checkTrimEmpty(String text) {
        return text.trim().isEmpty();
    }
}