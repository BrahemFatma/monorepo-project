package com.pfe.projet.exception;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)



public class InterventionNotFoundException extends RuntimeException {
    private final String msg;

    public InterventionNotFoundException() {
        super("Intervention not found");
        this.msg = "Intervention not found";
    }

    public InterventionNotFoundException(String msg) {
        super(msg);
        this.msg = msg;
    }


    public String getMsg() {
        return msg;
    }



}