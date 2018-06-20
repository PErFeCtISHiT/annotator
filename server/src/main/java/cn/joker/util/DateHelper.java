package cn.joker.util;

import cn.joker.namespace.StdName;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * 日期转换
 */
public class DateHelper {

    private DateHelper() {
        throw new IllegalStateException(StdName.UTILCLASS);
    }


    /**
     * 日期的表示形式是"2018-03-23 23:36:23"
     * @param date 日期
     * return string类型的提起
     */
    public static String convertDateToString(Date date) {
        if (date == null)
            return null;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    /**
     * 默认String保存的json格式是"2018-03-23 23:36:23"
     * @param dateInString string类型的日期
     * return 日期
     */
    public static Date convertStringToDate(String dateInString) {
        if (dateInString == null)
            return null;
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            return new java.sql.Date(dateFormat.parse(dateInString).getTime());
        } catch (ParseException e) {
            return null;
        }
    }
}
