package com.idgs06.UTEQLive.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FechasUtils {

    public static String getFechaNow() {
        String dateTime = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm:ss")
                .format(LocalDateTime.now());
        return dateTime;
    }

    public static boolean esMenorDe18(String fecha) {
        if(fecha.isEmpty() && fecha.isBlank()) {
            return false;
        }
        LocalDate fechaNac = LocalDate.parse(fecha, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate hoy = LocalDate.now();
        LocalDate hace18 = hoy.minusYears(18);
        return fechaNac.isBefore(hace18);
    }

}
