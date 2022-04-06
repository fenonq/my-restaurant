package com.fenonq.myrestaurant.validation;

import java.util.regex.Pattern;

public class Validation {
    private final static String REGEX_LOGIN = "^(?=.*[A-Za-z0-9]$)[A-Za-z][A-Za-z\\d.-]{0,19}$";
    private final static String REGEX_PASSWORD = "^([a-zA-Z0-9]{3,})$";
    private final static String REGEX_NAME = "^([А-ЯІЄЇ][а-яієї]{1,23}|[A-Z][a-z]{1,23})$";

    public static String validateName(String name) {
        if (!Pattern.matches(REGEX_NAME, name)){
            throw new IllegalArgumentException("Incorrect name");
        }
        return name;
    }

    public static int validatePositiveNumber(int number) {
        if (number < 1) {
            throw new IllegalArgumentException("Incorrect number");
        }
        return number;
    }

    public static String validateLogin(String login) {
        if (!Pattern.matches(REGEX_LOGIN, login)){
            throw new IllegalArgumentException("Incorrect login");
        }
        return login;
    }

    public static String validatePassword(String password) {
        if (!Pattern.matches(REGEX_PASSWORD, password)){
            throw new IllegalArgumentException("Incorrect password");
        }
        return password;
    }
}
