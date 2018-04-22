package cn.joker.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {

    private DateHelper() {
        throw new IllegalStateException("Utility class");
    }
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     *  日期的表示形式是"2018-03-23 23:36:23"
     */
    public static String convertDateToString(Date date){
        return dateFormat.format(date);
    }

    /**
     * 默认String保存的json格式是"2018#03#23#23#36#23"
     */
    public static Date convertStringtoDate(String dateInString){
        String[] temp = dateInString.split("#");
        String date = temp[0] + "-" + temp[1] + "-" + temp[2] + " " + temp[3] + ":" + temp[4] + ":" + temp[5];

        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            //返回空值
            return null;
        }
    }
}
