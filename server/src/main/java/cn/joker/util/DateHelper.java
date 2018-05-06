package cn.joker.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {

    private DateHelper() {
        throw new IllegalStateException("Utility class");
    }


    /**
     * 日期的表示形式是"2018-03-23 23:36:23"
     */
    public static String convertDateToString(Date date) {
        if (date == null)
            return null;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }

    /**
     * 默认String保存的json格式是"2018-03-23 23:36:23"
     */
    public static Date convertStringToDate(String dateInString) {
        if (dateInString == null)
            return null;
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            return dateFormat.parse(dateInString);
        } catch (ParseException e) {
            return null;
        }
    }
}
