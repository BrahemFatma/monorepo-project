package com.pfe.projet.handler;

import java.util.Map;

public record ErrorResponse (
    Map<String, String> errors
){

}
