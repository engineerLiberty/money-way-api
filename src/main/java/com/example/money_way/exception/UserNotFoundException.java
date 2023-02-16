package com.example.money_way.exception;

public class UserNotFound extends RuntimeException{

    public UserNotFound(String msg) {
        super(msg);
    }
}
