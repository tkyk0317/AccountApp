package com.myapp.account.calendar;

import java.util.*;
import java.text.*;
import android.util.Log;
import android.app.Activity;
import android.content.res.Resources;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.graphics.Color;
import android.view.View;
import com.myapp.account.R;
import com.myapp.account.utility.Utility;
import com.myapp.account.calendar.AccountCalendarCell;
import com.myapp.account.ObserverInterface;

/**
 * Account Calendar Class.
 */
public class AccountCalendar implements ObserverInterface {

    protected Activity activity;
    protected List<AccountCalendarCell> calendarCells = new ArrayList<AccountCalendarCell>(CALENDAR_DAY_OF_WEEK_NUM*CALENDAR_ROW_NUM);
    protected ObserverInterface observer;
    protected AccountCalendarCell currentCell;
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
    }

    /**
     * Get Table Item.
     */
    protected void getTableItemFromXml() {
        calendarCells.add( new AccountCalendarCell( (TextView)activity.findViewById(R.id.calendar_cell_0)) );
        calendarCells.add( new AccountCalendarCell( (TextView)activity.findViewById(R.id.calendar_cell_1)) );
        calendarCells.add( new AccountCalendarCell( (TextView)activity.findViewById(R.id.calendar_cell_2)) );
        calendarCells.add( new AccountCalendarCell( (TextView)activity.findViewById(R.id.calendar_cell_3)) );
        calendarCells.add( new AccountCalendarCell( (TextView)activity.findViewById(R.id.calendar_cell_4)) );
        calendarCells.add( new AccountCalendarCell( (TextView)activity.findViewById(R.id.calendar_cell_5)) );
        calendarCells.add( new AccountCalendarCell( (TextView)activity.findViewById(R.id.calendar_cell_6)) );
        calendarCells.add( new AccountCalendarCell( (TextView)activity.findViewById(R.id.calendar_cell_7)) );
        calendarCells.add( new AccountCalendarCell( (TextView)activity.findViewById(R.id.calendar_cell_8)) );
        calendarCells.add( new AccountCalendarCell( (TextView)activity.findViewById(R.id.calendar_cell_9)) );
        calendarCells.add( new AccountCalendarCell( (TextView)activity.findViewById(R.id.calendar_cell_10)) );
        calendarCells.add( new AccountCalendarCell( (TextView)activity.findViewById(R.id.calendar_cell_11)) );
        calendarCells.add( new AccountCalendarCell( (TextView)activity.findViewById(R.id.calendar_cell_12)) );
        calendarCells.add( new AccountCalendarCell( (TextView)activity.findViewById(R.id.calendar_cell_13)) );
        calendarCells.add( new AccountCalendarCell( (TextView)activity.findViewById(R.id.calendar_cell_14)) );
        calendarCells.add( new AccountCalendarCell( (TextView)activity.findViewById(R.id.calendar_cell_15)) );
        calendarCells.add( new AccountCalendarCell( (TextView)activity.findViewById(R.id.calendar_cell_16)) );
        calendarCells.add( new AccountCalendarCell( (TextView)activity.findViewById(R.id.calendar_cell_17)) );
        calendarCells.add( new AccountCalendarCell( (TextView)activity.findViewById(R.id.calendar_cell_18)) );
        calendarCells.add( new AccountCalendarCell( (TextView)activity.findViewById(R.id.calendar_cell_19)) );
        calendarCells.add( new AccountCalendarCell( (TextView)activity.findViewById(R.id.calendar_cell_20)) );
        calendarCells.add( new AccountCalendarCell( (TextView)activity.findViewById(R.id.calendar_cell_21)) );
        calendarCells.add( new AccountCalendarCell( (TextView)activity.findViewById(R.id.calendar_cell_22)) );
        calendarCells.add( new AccountCalendarCell( (TextView)activity.findViewById(R.id.calendar_cell_23)) );
        calendarCells.add( new AccountCalendarCell( (TextView)activity.findViewById(R.id.calendar_cell_24)) );
        calendarCells.add( new AccountCalendarCell( (TextView)activity.findViewById(R.id.calendar_cell_25)) );
        calendarCells.add( new AccountCalendarCell( (TextView)activity.findViewById(R.id.calendar_cell_26)) );
        calendarCells.add( new AccountCalendarCell( (TextView)activity.findViewById(R.id.calendar_cell_27)) );
        calendarCells.add( new AccountCalendarCell( (TextView)activity.findViewById(R.id.calendar_cell_28)) );
        calendarCells.add( new AccountCalendarCell( (TextView)activity.findViewById(R.id.calendar_cell_29)) );
        calendarCells.add( new AccountCalendarCell( (TextView)activity.findViewById(R.id.calendar_cell_30)) );
        calendarCells.add( new AccountCalendarCell( (TextView)activity.findViewById(R.id.calendar_cell_31)) );
        calendarCells.add( new AccountCalendarCell( (TextView)activity.findViewById(R.id.calendar_cell_32)) );
        calendarCells.add( new AccountCalendarCell( (TextView)activity.findViewById(R.id.calendar_cell_33)) );
        calendarCells.add( new AccountCalendarCell( (TextView)activity.findViewById(R.id.calendar_cell_34)) );
        calendarCells.add( new AccountCalendarCell( (TextView)activity.findViewById(R.id.calendar_cell_35)) );
        calendarCells.add( new AccountCalendarCell( (TextView)activity.findViewById(R.id.calendar_cell_36)) );
        calendarCells.add( new AccountCalendarCell( (TextView)activity.findViewById(R.id.calendar_cell_37)) );
        calendarCells.add( new AccountCalendarCell( (TextView)activity.findViewById(R.id.calendar_cell_38)) );
        calendarCells.add( new AccountCalendarCell( (TextView)activity.findViewById(R.id.calendar_cell_39)) );
        calendarCells.add( new AccountCalendarCell( (TextView)activity.findViewById(R.id.calendar_cell_40)) );
        calendarCells.add( new AccountCalendarCell( (TextView)activity.findViewById(R.id.calendar_cell_41)) );
    }

    /**
      * Attach Observer.
      * @param observer ObserverInterface Instance.
      */
    public void attachObserver(ObserverInterface observer) {
        this.observer = observer;
    }

    /**
     * Appear the Calender.
     */
    public void appear() {
        createCalendar();
        focusCurrentDate();
    }

    /**
      * Create Calendar.
      */
    protected void createCalendar() {
        int day = 1;
        int current_year = Integer.valueOf(Utility.getCurrentYear());
        int current_month = Integer.valueOf(Utility.getCurrentMonth());

        // Create Calendar.
        for( int row = 0 ; row < CALENDAR_ROW_NUM ; ++row ) {
            for( int week = 0 ; week < CALENDAR_DAY_OF_WEEK_NUM ; ++week ) {
                AccountCalendarCell cell = calendarCells.get(row * CALENDAR_DAY_OF_WEEK_NUM + week );
                cell.attachObserver(this);

                if( row * CALENDAR_DAY_OF_WEEK_NUM + week >= getStartPosition() &&
                    row * CALENDAR_DAY_OF_WEEK_NUM + week <= getEndPosition() ) {
                    String date = Utility.CreateDateFormat(current_year, current_month, day);
                    int day_of_week = Utility.getDayOfWeek(date);

                    // setting calendar cell.
                    cell.setText(String.valueOf(day));
                    cell.setDate(current_year, current_month, day++, day_of_week);
                }
            }
        }
    }

    /**
      * Focus Current Date.
      */
    protected void focusCurrentDate() {
        currentCell = calendarCells.get(getStartPosition() + Integer.valueOf(Utility.getCurrentDay()) - 1);

        // color setting.
        Resources resources = activity.getResources();
        currentCell.setBackgroundColor(resources.getColor(R.color.cal_focus_background));
    }

    /**
     * Get Calendar Start Position.
     * @return int Calendar Start Position.
     */
    protected int getStartPosition() {
        // calednar start if index zero(day of week start is index one).
        return Utility.getDayOfWeek(Utility.getFirstDateOfCurrentMonth()) - 1;
    }

    /**
     * Get Calendar End Position.
     * @return int Calendar End Position.
     */
    protected int getEndPosition() {
        int cal_st_pos = getStartPosition();
        int end_current_month = Integer.valueOf(Utility.splitDay(Utility.getLastDateOfCurrentMonth()));

        return cal_st_pos + end_current_month - 1;
    }

    /**
     * Click Event from AccountCalendarCell Instance.
     * @param event Account Calendar Cell Instance.
     */
    public void notify(Object event) {
        // previous cell setting.
        Resources resources = activity.getResources();
        currentCell.setBackgroundColor(resources.getColor(R.color.cal_default_background));

        // current cell setteing.
        currentCell = (AccountCalendarCell)event;
        currentCell.setBackgroundColor(resources.getColor(R.color.cal_focus_background));

        // notify observer.
        if( null != this.observer ) this.observer.notify(event);
    }
}

