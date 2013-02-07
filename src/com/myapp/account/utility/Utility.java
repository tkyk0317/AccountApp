package com.myapp.account.utility;

import java.util.*;
import java.text.*;

/**
 * Utility Class.
 */
public class Utility {

    public static final String DATE_FORMAT = "yyyy/MM/dd";

    /**
     * Get Current Date.
     * @return Current Date(String Type).
     */
    public static String getCurrentDate() {
        Calendar cal_date = Calendar.getInstance(TimeZone.getDefault());
        return (new SimpleDateFormat(DATE_FORMAT)).format(cal_date.getTime());
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
}
