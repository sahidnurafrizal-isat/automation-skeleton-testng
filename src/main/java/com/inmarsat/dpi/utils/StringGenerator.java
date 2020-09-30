package com.inmarsat.dpi.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringGenerator {

    public static String getFormattedDateForReport() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm");
        Date date = new Date();
        return dateFormat.format(date);
    }

}
