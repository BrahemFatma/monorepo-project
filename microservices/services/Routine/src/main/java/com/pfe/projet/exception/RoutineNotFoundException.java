package com.pfe.projet.exception;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)



public class RoutineNotFoundException extends RuntimeException {
    private final String msg;

    public RoutineNotFoundException() {
        super("Reunion not found");
        this.msg = "Reunion not found";
    }

    public RoutineNotFoundException(String msg) {
        super(msg);
        this.msg = msg;
    }


    public String getMsg() {
        return msg;
    }



}