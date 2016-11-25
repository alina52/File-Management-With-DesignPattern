package Util;

import java.text.SimpleDateFormat;

public class Date {
    public static java.lang.String getDate(){
        java.util.Date date = new java.util.Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd/HH/mm/");
        return formatter.format(date);
    }
}
