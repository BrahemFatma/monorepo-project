package com.pfe.projet.Routine;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateUtil {

    public String extractHeure(Date date) {
        LocalDateTime localDateTime = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        return localDateTime.toLocalTime().toString();
    }

    public static void main(String[] args) {
        Date date = new Date();
        DateUtil dateUtil = new DateUtil();
        String heure = dateUtil.extractHeure(date);
        System.out.println("L'heure extraite : " + heure);
    }
}
