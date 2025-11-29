package com.pfe.projet.exception;

import lombok.*;

@EqualsAndHashCode(callSuper = true)



public class UserNotFoundException extends RuntimeException {
    private final String msg;

    public UserNotFoundException() {
        super("User not found");
        this.msg = "User not found";
    }

    public UserNotFoundException(String msg) {
        super(msg);
        this.msg = msg;
    }


    public String getMsg() {
        return msg;
    }



}