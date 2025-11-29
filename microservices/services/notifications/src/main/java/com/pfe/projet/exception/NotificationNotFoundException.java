package com.pfe.projet.exception;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)



public class NotificationNotFoundException extends RuntimeException {
    private final String msg;

    public NotificationNotFoundException() {
        super("User not found");
        this.msg = "User not found";
    }

    public NotificationNotFoundException(String msg) {
        super(msg);
        this.msg = msg;
    }


    public String getMsg() {
        return msg;
    }



}