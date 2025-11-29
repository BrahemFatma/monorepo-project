package com.pfe.projet.exception;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)



public class ReunionNotFoundException extends RuntimeException {
    private final String msg;

    public ReunionNotFoundException() {
        super("Reunion not found");
        this.msg = "Reunion not found";
    }

    public ReunionNotFoundException(String msg) {
        super(msg);
        this.msg = msg;
    }


    public String getMsg() {
        return msg;
    }



}