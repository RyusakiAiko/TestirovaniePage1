package com.example;

public class Validate {
    public boolean isLoginValid(String login) {
        String regex = "^[a-zA-Z0-9]+$";
        return login != null && !login.isEmpty() && login.matches(regex);
    }

    public boolean isPasswordValid(String password) {
        String regex = "^[a-zA-Z0-9]+$";
        return password != null && !password.isEmpty() && password.matches(regex);
    }

    public boolean validate(String login, String password) {
        return isLoginValid(login) && isPasswordValid(password);
    }
}
