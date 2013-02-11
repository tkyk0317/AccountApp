package com.myapp.account.utility;

import java.util.*;
import java.text.*;

/**
 * Utility Class.
 */
public class Utility {

    public static final String DATE_FORMAT = "yyyy/MM/dd";
    public static final String DATE_AND_TIME_FORMAT = "yyyy/MM/dd-HH-mm-ss";
    public static final int DATE_YEAR_ST_POS = 0;
    public static final int DATE_YEAR_SIZE = 4;
    public static final int DATE_MONTH_ST_POS = 5;
    public static final int DATE_MONTH_SIZE = 2;
    public static final int DATE_DAY_ST_POS = 8;
    public static final int DATE_DAY_SIZE = 2;
    public static final int DATE_MONTH_END_SLASH_POS = 7;
    public static final String DATE_DELIMITER = "/";

    /**
     * Is String is NULL.
     * @return true if String Data is NULL.
     */
    public static boolean isStringNULL(String str) {
        if( str == null || 0 == str.length() ) {
            return true;
        }
        return false;
    }

    /**
     * Create Date Format.
     * @return Current Date of String Data Type (yyyy/MM/dd).
     */
    public static String CreateDateFormat(int year, int month, int day) {
        Calendar cal_date = Calendar.getInstance(TimeZone.getDefault());
        cal_date.set(year, month, day);

        return (new SimpleDateFormat(DATE_FORMAT)).format(cal_date.getTime());
    }

    /**
     * Get Current Date.
     * @return Current Date(String Type).
     */
    public static String getCurrentDate() {
        Calendar cal_date = Calendar.getInstance(TimeZone.getDefault());
        return (new SimpleDateFormat(DATE_FORMAT)).format(cal_date.getTime());
    }

    /**
     * Get Current Date.
     * @return Current Date and Time(String Type).
     */
    public static String getCurrentDateAndTime() {
        Calendar cal_date = Calendar.getInstance(TimeZone.getDefault());
        return (new SimpleDateFormat(DATE_AND_TIME_FORMAT)).format(cal_date.getTime());
    }

    /**
     * Get First Date of Current Month.
     * @return First Date of Month Date(String Type).
     */
    public static String getFirstDateOfCurrentMonth() {
        Calendar cal_date = Calendar.getInstance(TimeZone.getDefault());

        // set first day.
        cal_date.set(Calendar.DATE, 1);

        return (new SimpleDateFormat(DATE_FORMAT)).format(cal_date.getTime());
     }

    /**
     * Get Last Date of Current Month.
     * @return Last Date of Month Date(String Type).
     */
    public static String getLastDateOfCurrentMonth() {
        Calendar cal_date = Calendar.getInstance(TimeZone.getDefault());

        //  calculate last day of month.
        cal_date.add(Calendar.MONTH, 1);
        cal_date.set(Calendar.DAY_OF_MONTH, 1);
        cal_date.add(Calendar.DAY_OF_MONTH, -1);

        return (new SimpleDateFormat(DATE_FORMAT)).format(cal_date.getTime());
    }

    /**
     * Split Current Month and Day.
     * @param String Data that Current Date (format is yyyy/MM/dd).
     * @return String Date that Current Month and Day (format is MM/dd).
     */
    public static String splitCurrentMonthAndDay(String current_date) {
        return current_date.substring(DATE_MONTH_ST_POS);
    }

    /**
     * Get Current Year and Month.
     * return String Data that Current Year and Month (format is yyyy/MM).
     */
    public static String getCurrentYearAndMonth() {
        return getCurrentDate().substring(DATE_YEAR_ST_POS, DATE_MONTH_END_SLASH_POS);
     }
}
