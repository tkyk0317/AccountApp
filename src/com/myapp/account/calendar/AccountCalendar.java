package com.myapp.account.calendar;

import java.util.*;
import java.text.*;
import android.app.Activity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.view.Gravity;
import android.graphics.Color;
import com.myapp.account.R;
import com.myapp.account.utility.Utility;

/**
 * Account Calendar Class.
 */
public class AccountCalendar {

    protected Activity activity;
    protected List<TextView> calendarCells = new ArrayList<TextView>(CALENDAR_DAY_OF_WEEK_NUM*CALENDAR_ROW_NUM);
    protected static final int CALENDAR_DAY_OF_WEEK_NUM = 7;
    protected static final int CALENDAR_ROW_NUM = 6;
    protected static final int WEEK_OF_SATURDAY = 5;
    protected static final int WEEK_OF_SUNDAY = 6;

    /**
     * Constractor.
     */
    public AccountCalendar(Activity activity) {
        this.activity = activity;
        getTableItemFromXml();
        markSaturdayAndSunday();
    }

    /**
     * Get Table Item.
     */
    protected void getTableItemFromXml() {
        calendarCells.add( (TextView) activity.findViewById(R.id.calendar_cell_0) );
        calendarCells.add( (TextView) activity.findViewById(R.id.calendar_cell_1) );
        calendarCells.add( (TextView) activity.findViewById(R.id.calendar_cell_2) );
        calendarCells.add( (TextView) activity.findViewById(R.id.calendar_cell_3) );
        calendarCells.add( (TextView) activity.findViewById(R.id.calendar_cell_4) );
        calendarCells.add( (TextView) activity.findViewById(R.id.calendar_cell_5) );
        calendarCells.add( (TextView) activity.findViewById(R.id.calendar_cell_6) );
        calendarCells.add( (TextView) activity.findViewById(R.id.calendar_cell_7) );
        calendarCells.add( (TextView) activity.findViewById(R.id.calendar_cell_8) );
        calendarCells.add( (TextView) activity.findViewById(R.id.calendar_cell_9) );
        calendarCells.add( (TextView) activity.findViewById(R.id.calendar_cell_10) );
        calendarCells.add( (TextView) activity.findViewById(R.id.calendar_cell_11) );
        calendarCells.add( (TextView) activity.findViewById(R.id.calendar_cell_12) );
        calendarCells.add( (TextView) activity.findViewById(R.id.calendar_cell_13) );
        calendarCells.add( (TextView) activity.findViewById(R.id.calendar_cell_14) );
        calendarCells.add( (TextView) activity.findViewById(R.id.calendar_cell_15) );
        calendarCells.add( (TextView) activity.findViewById(R.id.calendar_cell_16) );
        calendarCells.add( (TextView) activity.findViewById(R.id.calendar_cell_17) );
        calendarCells.add( (TextView) activity.findViewById(R.id.calendar_cell_18) );
        calendarCells.add( (TextView) activity.findViewById(R.id.calendar_cell_19) );
        calendarCells.add( (TextView) activity.findViewById(R.id.calendar_cell_20) );
        calendarCells.add( (TextView) activity.findViewById(R.id.calendar_cell_21) );
        calendarCells.add( (TextView) activity.findViewById(R.id.calendar_cell_22) );
        calendarCells.add( (TextView) activity.findViewById(R.id.calendar_cell_23) );
        calendarCells.add( (TextView) activity.findViewById(R.id.calendar_cell_24) );
        calendarCells.add( (TextView) activity.findViewById(R.id.calendar_cell_25) );
        calendarCells.add( (TextView) activity.findViewById(R.id.calendar_cell_26) );
        calendarCells.add( (TextView) activity.findViewById(R.id.calendar_cell_27) );
        calendarCells.add( (TextView) activity.findViewById(R.id.calendar_cell_28) );
        calendarCells.add( (TextView) activity.findViewById(R.id.calendar_cell_29) );
        calendarCells.add( (TextView) activity.findViewById(R.id.calendar_cell_30) );
        calendarCells.add( (TextView) activity.findViewById(R.id.calendar_cell_31) );
        calendarCells.add( (TextView) activity.findViewById(R.id.calendar_cell_32) );
        calendarCells.add( (TextView) activity.findViewById(R.id.calendar_cell_33) );
        calendarCells.add( (TextView) activity.findViewById(R.id.calendar_cell_34) );
        calendarCells.add( (TextView) activity.findViewById(R.id.calendar_cell_35) );
        calendarCells.add( (TextView) activity.findViewById(R.id.calendar_cell_36) );
        calendarCells.add( (TextView) activity.findViewById(R.id.calendar_cell_37) );
        calendarCells.add( (TextView) activity.findViewById(R.id.calendar_cell_38) );
        calendarCells.add( (TextView) activity.findViewById(R.id.calendar_cell_39) );
        calendarCells.add( (TextView) activity.findViewById(R.id.calendar_cell_40) );
        calendarCells.add( (TextView) activity.findViewById(R.id.calendar_cell_41) );
    }

    /**
     * Mark Saturday and Sanday.
     */
    protected void markSaturdayAndSunday() {
        for( int i = 0 ; i < CALENDAR_DAY_OF_WEEK_NUM * CALENDAR_ROW_NUM ; ++i ) {
            // Color for Saturday and Sunday.
            if( isSaturday(i) ) calendarCells.get(i).setTextColor(Color.BLUE);
            if( isSunday(i) ) calendarCells.get(i).setTextColor(Color.RED);
        }
    }

    /**
     * Check Index is Saturday.
     * @return true if index is saturday.
     */
    protected boolean isSaturday(int index) {
        int check_index = index - WEEK_OF_SATURDAY;

        if( 0 == check_index % CALENDAR_DAY_OF_WEEK_NUM ) {
            return true;
        }
        return false;
    }

    /**
     * Check Index is Sunday.
     * @return true if index is sunday.
     */
    protected boolean isSunday(int index) {
        int check_index = index - WEEK_OF_SUNDAY;

        if( 0 == check_index % CALENDAR_DAY_OF_WEEK_NUM ) {
            return true;
        }
        return false;
    }

    /**
     * Appear the Calender.
     */
    public void appear() {
        int day_index = 1;

        // Create Calendar.
        for( int row = 0 ; row < CALENDAR_ROW_NUM ; ++row ) {
            for( int week = 0 ; week < CALENDAR_DAY_OF_WEEK_NUM ; ++week ) {

                if( row * CALENDAR_DAY_OF_WEEK_NUM + week >= getStartPosition() &&
                    row * CALENDAR_DAY_OF_WEEK_NUM + week <= getEndPosition() ) {
                    calendarCells.get(row * CALENDAR_DAY_OF_WEEK_NUM + week).setText(String.valueOf(day_index++));
                    }
            }
        }
    }

    /**
     * Get Calendar Start Position.
     * @return int Calendar Start Position.
     */
    protected int getStartPosition() {
        return Utility.getDayOfWeek(Utility.getFirstDateOfCurrentMonth()) - 1;
    }

    /**
     * Get Calendar End Position.
     * @return int Calendar End Position.
     */
    protected int getEndPosition() {
        int cal_st_pos = Utility.getDayOfWeek(Utility.getFirstDateOfCurrentMonth()) - 1;
        int end_current_month = Integer.valueOf(Utility.splitDay(Utility.getLastDateOfCurrentMonth()));

        return cal_st_pos + end_current_month;
    }
}

