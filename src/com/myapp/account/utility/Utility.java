package com.myapp.account.utility;

import java.util.*;
import java.text.*;
import android.util.Log;
import android.app.Activity;

import com.myapp.account.R;
import com.myapp.account.config.AppConfigurationData;

/**
 * @brief Utility Class.
 */
public class Utility {

    public static final String HALF_SPACE = " ";
    public static final String FULL_SPACE = "　";
    public static final String EMPTY_STRING = "";
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
    public static final String ZERO = "0";
    public static final int NUMBER_THRESHOLD = 10;
    public static final String YEAR_OF_FIRST_DATE = "01/01";
    public static final String YEAR_OF_LAST_DATE = "12/31";

    /**
     * @brief Is String is NULL.
     * @return true if String Data is NULL.
     */
    public static boolean isStringNULL(String str) {
        if( str == null || 0 == str.length() ) {
            return true;
        }
        return false;
    }

    /**
     * @brief Delete String.
     *
     * @param target_string target string.
     *
     * @return deleteted string.
     */
    public static String deleteSpace(String target_string) {
        String deleted_string = target_string.replaceAll(HALF_SPACE, EMPTY_STRING);
        deleted_string = deleted_string.replaceAll(FULL_SPACE, EMPTY_STRING);
        return deleted_string;
    }

    /**
     * @brief Create Date Format.
     * @return Current Date of String Data Type (yyyy/MM/dd).
     */
    public static String createDateFormat(int year, int month, int day) {
        return String.valueOf(year) + DATE_DELIMITER + convertNumberToString(month) + DATE_DELIMITER + convertNumberToString(day);
    }

    /**
     * @brief Convert To Date From StringDate.
     * @param target_str converted string date(yyyy/mm/dd).
     * @return converted Date instance.
     */
    public static Date convertStringDateToDate(String target_str) {
        Date converted_date = null;
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        try {
            converted_date = format.parse(target_str);
        } catch(ParseException exception) {
            Log.d("Utility", "convertStringDateToDate Exception");
        }
        return converted_date;
    }

    /**
     * @brief Convert Number To String.
     * @param number target number.
     * @return converted number string.
     */
    public static String convertNumberToString(int number) {
        String number_str = "";
        if( NUMBER_THRESHOLD > number ) {
            number_str = ZERO + String.valueOf(number);
        } else {
            number_str = String.valueOf(number);
        }
        return number_str;
    }

    /**
     * @brief Get Current Date.
     * @return Current Date(String Type).
     */
    public static String getCurrentDate() {
        Calendar cal_date = Calendar.getInstance(TimeZone.getDefault());
        return (new SimpleDateFormat(DATE_FORMAT)).format(cal_date.getTime());
    }

    /**
     * @brief Get Previous Date.
     * @return Previous Date(String Type).
     */
    public static String getPreviousMonthDate(String current_date) {
        int year = Integer.valueOf(current_date.substring(DATE_YEAR_ST_POS, DATE_YEAR_ST_POS + DATE_YEAR_SIZE));
        int month = Integer.valueOf(current_date.substring(DATE_MONTH_ST_POS, DATE_MONTH_ST_POS + DATE_MONTH_SIZE));
        int day = Integer.valueOf(current_date.substring(DATE_DAY_ST_POS));

        Calendar previous_date = Calendar.getInstance(TimeZone.getDefault());
        previous_date.set(year, month - 1, day);
        previous_date.add(Calendar.MONTH, -1);
        return (new SimpleDateFormat(DATE_FORMAT)).format(previous_date.getTime());
    }

    /**
     * @brief Get Next Date.
     * @return Next Date(String Type).
     */
    public static String getNextMonthDate(String current_date) {
        int year = Integer.valueOf(current_date.substring(DATE_YEAR_ST_POS, DATE_YEAR_ST_POS + DATE_YEAR_SIZE));
        int month = Integer.valueOf(current_date.substring(DATE_MONTH_ST_POS, DATE_MONTH_ST_POS + DATE_MONTH_SIZE));
        int day = Integer.valueOf(current_date.substring(DATE_DAY_ST_POS));

        Calendar previous_date = Calendar.getInstance(TimeZone.getDefault());
        previous_date.set(year, month - 1, day);
        previous_date.add(Calendar.MONTH, 1);
        return (new SimpleDateFormat(DATE_FORMAT)).format(previous_date.getTime());
    }

    /**
     * @brief Get Previous Date.
     * @return Previous Date(String Type).
     */
    public static String getPreviousYearDate(String current_date) {
        int year = Integer.valueOf(current_date.substring(DATE_YEAR_ST_POS, DATE_YEAR_ST_POS + DATE_YEAR_SIZE));
        int month = Integer.valueOf(current_date.substring(DATE_MONTH_ST_POS, DATE_MONTH_ST_POS + DATE_MONTH_SIZE));
        int day = Integer.valueOf(current_date.substring(DATE_DAY_ST_POS));

        Calendar previous_date = Calendar.getInstance(TimeZone.getDefault());
        previous_date.set(year, month - 1, day);
        previous_date.add(Calendar.YEAR, -1);
        return (new SimpleDateFormat(DATE_FORMAT)).format(previous_date.getTime());
    }

    /**
     * @brief Get Next Date.
     * @return Next Date(String Type).
     */
     public static String getNextYearDate(String current_date) {
        int year = Integer.valueOf(current_date.substring(DATE_YEAR_ST_POS, DATE_YEAR_ST_POS + DATE_YEAR_SIZE));
        int month = Integer.valueOf(current_date.substring(DATE_MONTH_ST_POS, DATE_MONTH_ST_POS + DATE_MONTH_SIZE));
        int day = Integer.valueOf(current_date.substring(DATE_DAY_ST_POS));

        Calendar previous_date = Calendar.getInstance(TimeZone.getDefault());
        previous_date.set(year, month - 1, day);
        previous_date.add(Calendar.YEAR, 1);
        return (new SimpleDateFormat(DATE_FORMAT)).format(previous_date.getTime());
    }

    /**
     * @brief Get Current Date.
     * @return Current Date and Time(String Type).
     */
    public static String getCurrentDateAndTime() {
        Calendar cal_date = Calendar.getInstance(TimeZone.getDefault());
        return (new SimpleDateFormat(DATE_AND_TIME_FORMAT)).format(cal_date.getTime());
    }

    /**
     * @brief Get First Date of Target Date.
     * @param target_date Target Date of First Date(yyyy/MM/dd).
     * @return First Date of Month Date(String Type).
     */
    public static String getFirstDateOfTargetMonth(String target_date) {
        Calendar cal_date = Calendar.getInstance(TimeZone.getDefault());
        int year = Integer.valueOf(target_date.substring(DATE_YEAR_ST_POS, DATE_YEAR_ST_POS + DATE_YEAR_SIZE));
        int month = Integer.valueOf(target_date.substring(DATE_MONTH_ST_POS, DATE_MONTH_ST_POS + DATE_MONTH_SIZE));

        cal_date.set(year, month - 1, 1);

        return (new SimpleDateFormat(DATE_FORMAT)).format(cal_date.getTime());
    }

    /**
     * @brief Get Last Date of Target Date.
     * @param target_date Target Date of First Date(yyyy/MM/dd).
     */
    public static String getLastDateOfTargetMonth(String target_date) {
        Calendar cal_date = Calendar.getInstance(TimeZone.getDefault());
        int year = Integer.valueOf(target_date.substring(DATE_YEAR_ST_POS, DATE_YEAR_ST_POS + DATE_YEAR_SIZE));
        int month = Integer.valueOf(target_date.substring(DATE_MONTH_ST_POS, DATE_MONTH_ST_POS + DATE_MONTH_SIZE));
        int day = Integer.valueOf(target_date.substring(DATE_DAY_ST_POS));

        //  calculate last day of month.
        cal_date.set(year, month - 1, day);

        //  calculate last day of month.
        cal_date.add(Calendar.MONTH, 1);
        cal_date.set(Calendar.DAY_OF_MONTH, 1);
        cal_date.add(Calendar.DAY_OF_MONTH, -1);

        return (new SimpleDateFormat(DATE_FORMAT)).format(cal_date.getTime());
    }

    /**
     * @brief Get First Date At Target Year.
     * @param target_date spcified target date(yyyy/mm/dd).
     * @return First Date of Specified Year.
     */
    public static String getFirstDateOfTargetYear(String target_date) {
        return Utility.splitYear(target_date) + DATE_DELIMITER + YEAR_OF_FIRST_DATE;
    }

    /**
     * @brief Get Last Date At Target Year.
     * @param target_date spcified target date(yyyy/mm/dd).
     * @return Last Date of Specified Year.
     */
    public static String getLastDateOfTargetYear(String target_date) {
        return Utility.splitYear(target_date) + DATE_DELIMITER + YEAR_OF_LAST_DATE;
    }

    /**
     * @brief Get Estimate Start Date.
     *
     * @param activity Activity Instance.
     *
     * @return estimate start date.
     */
    public static String getEstimateStartDate(Activity activity) {
        AppConfigurationData app_config = new AppConfigurationData(activity);
        int estimate_start_day = app_config.getEstimateStartDay();

        Calendar estimate_start_date = Calendar.getInstance(TimeZone.getDefault());
        Calendar last_date = (Calendar)estimate_start_date.clone();

        // calculate last date.
        last_date.add(Calendar.MONTH, 1);
        last_date.set(Calendar.DAY_OF_MONTH, 1);
        last_date.add(Calendar.DAY_OF_MONTH, -1);

        // check estimate start day.
        if( last_date.get(Calendar.DAY_OF_MONTH) < estimate_start_day ) {
            estimate_start_day = last_date.get(Calendar.DAY_OF_MONTH);
        }

        // check start day and current day.
        if( estimate_start_day < estimate_start_date.get(Calendar.DAY_OF_MONTH) ) {
            estimate_start_date.set(Calendar.DAY_OF_MONTH, estimate_start_day);
            estimate_start_date.add(Calendar.MONTH, 1);
        } else {
            estimate_start_date.set(Calendar.DAY_OF_MONTH, estimate_start_day);
            estimate_start_date.add(Calendar.MONTH, -1);
        }
        return (new SimpleDateFormat(DATE_FORMAT)).format(estimate_start_date.getTime());
    }

    /**
     * @brief Get Estimate Last Date.
     *
     * @param activity Activity Instance.
     * @param estimate_start_date estimate start date(yyyy/mm/dd).
     *
     * @return estimate last date.
     */
    public static String getEstimateLastDate(Activity activity, String estimate_start_date) {
        Calendar estimate_end_date = Calendar.getInstance(TimeZone.getDefault());
        estimate_end_date.setTime(Utility.convertStringDateToDate(estimate_start_date));

        // calculate estimate end date.
        estimate_end_date.add(Calendar.MONTH, 1);
        estimate_end_date.add(Calendar.DAY_OF_MONTH, -1);

        return (new SimpleDateFormat(DATE_FORMAT)).format(estimate_end_date.getTime());
    }

    /**
     * @brief Split Year And Month from TargetDate.
     * @param target_date yyyy/mm/dd.
     * @return Splited Date(yyyy/mm).
     */
    public static String splitYearAndMonth(String target_date) {
        return target_date.substring(DATE_YEAR_ST_POS, DATE_MONTH_END_SLASH_POS);
    }

    /**
     * @brief Split Target Date into Month and Day.
     * @param String Data that Target Date (format is yyyy/MM/dd).
     * @return String Date that Month and Day (format is MM/dd).
     */
    public static String splitMonthAndDay(String target_date) {
        return target_date.substring(DATE_MONTH_ST_POS);
    }

    /**
     * @brief Split the year from Specified Date.
     * @param target_date Specified Date(yyyy/mm/dd).
     * @return splited year.
     */
    public static String splitYear(String target_date) {
        return target_date.substring(DATE_YEAR_ST_POS, DATE_YEAR_SIZE);
    }

    /**
     * @brief Split the month from Specified Date.
     * @param target_date Specified Date(yyyy/mm/dd).
     * @return splited month.
     */
    public static String splitMonth(String target_date) {
        return target_date.substring(DATE_MONTH_ST_POS, DATE_MONTH_ST_POS + DATE_MONTH_SIZE);
    }

    /**
     * @brief Split Target Date into Day.
     * @param String Data that Target Date (format is yyyy/MM/dd).
     * @return String Date that Day (format is dd).
     */
    public static String splitDay(String target_date) {
        return target_date.substring(DATE_DAY_ST_POS);
    }

    /**
     * @brief Get Current Year and Month.
     * @return String Data that Current Year and Month (format is yyyy/MM).
     */
    public static String getCurrentYearAndMonth() {
        return getCurrentDate().substring(DATE_YEAR_ST_POS, DATE_MONTH_END_SLASH_POS);
     }

    /**
     * @brief Get Day of Week.
     * @param target date(yyyy/mm/dd).
     * @return int day of week value(Sunday is One).
     */
    public static int getDayOfWeek(String target_date) {
        int year = Integer.valueOf(target_date.substring(DATE_YEAR_ST_POS, DATE_YEAR_ST_POS + DATE_YEAR_SIZE));
        int month = Integer.valueOf(target_date.substring(DATE_MONTH_ST_POS, DATE_MONTH_ST_POS + DATE_MONTH_SIZE));
        int day = Integer.valueOf(target_date.substring(DATE_DAY_ST_POS, DATE_DAY_ST_POS + DATE_DAY_SIZE));

        Calendar target_cal = Calendar.getInstance(TimeZone.getDefault());

        // setting target date.
        target_cal.set(year, month - 1, day);

        return target_cal.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * @brief Get DayOfWeek String.
     * @param date target day of week.
     * @return Day Of Week String.
     */
    public static String getDayOfWeekString(int day_of_week, Activity activity) {
        String day_of_week_str = "";
        switch(day_of_week) {
            case Calendar.SUNDAY:
                day_of_week_str = activity.getText(R.string.sunday).toString();
                break;
            case Calendar.MONDAY:
                day_of_week_str = activity.getText(R.string.monday).toString();
                break;
            case Calendar.TUESDAY:
                day_of_week_str = activity.getText(R.string.tuesday).toString();
                break;
            case Calendar.WEDNESDAY:
                day_of_week_str = activity.getText(R.string.wednesday).toString();
                break;
            case Calendar.THURSDAY:
                day_of_week_str = activity.getText(R.string.thursday).toString();
                break;
            case Calendar.FRIDAY:
                day_of_week_str = activity.getText(R.string.friday).toString();
                break;
            case Calendar.SATURDAY:
                day_of_week_str = activity.getText(R.string.saturday).toString();
                break;
            default:
                break;
        }
        return day_of_week_str;
    }

    /**
     * @brief Check Include TargetDate In Current Month.
     * @param target_date checked date(yyyy/mm/dd).
     * @return true:include false:not include.
     */
    public static boolean isIncludeTargetDateInCurrentMonth(String target_date) {
        String st_date = Utility.getFirstDateOfTargetMonth(Utility.getCurrentDate());
        String end_date = Utility.getLastDateOfTargetMonth(Utility.getCurrentDate());

        if( st_date.compareTo(target_date) <= 0 && end_date.compareTo(target_date) >= 0 ) {
            return true;
        }
        return false;
    }
}

