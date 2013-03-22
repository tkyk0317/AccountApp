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
import android.widget.LinearLayout;
import android.view.MotionEvent;
import com.myapp.account.R;
import com.myapp.account.utility.Utility;
import com.myapp.account.calendar.AccountCalendarCell;
import com.myapp.account.observer.ClickObserverInterface;
import com.myapp.account.database.DatabaseHelper;
import com.myapp.account.database.AccountTableAccessor;

/**
 * @brief Account Calendar Class.
 */
public class AccountCalendar implements ClickObserverInterface {

    protected Activity activity;
    protected List<AccountCalendarCell> calendarCells = new ArrayList<AccountCalendarCell>(CALENDAR_DAY_OF_WEEK_NUM*CALENDAR_ROW_NUM);
    protected ClickObserverInterface observer;
    protected AccountCalendarCell currentCell;
    protected LinearLayout layout;
    protected String appearDate;
    protected AccountTableAccessor accountTableAccessor;
    protected static final int CALENDAR_DAY_OF_WEEK_NUM = 7;
    protected static final int CALENDAR_ROW_NUM = 6;
    protected static final int WEEK_OF_SATURDAY = 5;
    protected static final int WEEK_OF_SUNDAY = 6;

    /**
     * @brief Constractor.
     */
    public AccountCalendar(Activity activity, LinearLayout layout) {
        this.activity = activity;
        this.layout = layout;
        this.accountTableAccessor = new AccountTableAccessor(new DatabaseHelper(activity.getApplicationContext()));
        getCalendarItems();
    }

    /**
     * @brief Appear the Calender.
     */
    public void appear(String target_date) {
        this.appearDate = target_date;
        clearCalendarItems();
        createCalendar();
        focusCurrentDate();
    }

    /**
     * @brief Clear Calendar Items.
     */
    protected void clearCalendarItems() {
        for( AccountCalendarCell cell : this.calendarCells ) {
            cell.setDate(0, 0, 0, 0);
            cell.setText("");
        }
    }

    /**
     * @brief Get CalendarItems.
     */
    protected void getCalendarItems() {
        calendarCells.add( new AccountCalendarCell( (TextView)this.layout.findViewById(R.id.calendar_cell_0), this.activity) );
        calendarCells.add( new AccountCalendarCell( (TextView)this.layout.findViewById(R.id.calendar_cell_1), this.activity) );
        calendarCells.add( new AccountCalendarCell( (TextView)this.layout.findViewById(R.id.calendar_cell_2), this.activity) );
        calendarCells.add( new AccountCalendarCell( (TextView)this.layout.findViewById(R.id.calendar_cell_3), this.activity) );
        calendarCells.add( new AccountCalendarCell( (TextView)this.layout.findViewById(R.id.calendar_cell_4), this.activity) );
        calendarCells.add( new AccountCalendarCell( (TextView)this.layout.findViewById(R.id.calendar_cell_5), this.activity) );
        calendarCells.add( new AccountCalendarCell( (TextView)this.layout.findViewById(R.id.calendar_cell_6), this.activity) );
        calendarCells.add( new AccountCalendarCell( (TextView)this.layout.findViewById(R.id.calendar_cell_7), this.activity) );
        calendarCells.add( new AccountCalendarCell( (TextView)this.layout.findViewById(R.id.calendar_cell_8), this.activity) );
        calendarCells.add( new AccountCalendarCell( (TextView)this.layout.findViewById(R.id.calendar_cell_9), this.activity) );
        calendarCells.add( new AccountCalendarCell( (TextView)this.layout.findViewById(R.id.calendar_cell_10), this.activity) );
        calendarCells.add( new AccountCalendarCell( (TextView)this.layout.findViewById(R.id.calendar_cell_11), this.activity) );
        calendarCells.add( new AccountCalendarCell( (TextView)this.layout.findViewById(R.id.calendar_cell_12), this.activity) );
        calendarCells.add( new AccountCalendarCell( (TextView)this.layout.findViewById(R.id.calendar_cell_13), this.activity) );
        calendarCells.add( new AccountCalendarCell( (TextView)this.layout.findViewById(R.id.calendar_cell_14), this.activity) );
        calendarCells.add( new AccountCalendarCell( (TextView)this.layout.findViewById(R.id.calendar_cell_15), this.activity) );
        calendarCells.add( new AccountCalendarCell( (TextView)this.layout.findViewById(R.id.calendar_cell_16), this.activity) );
        calendarCells.add( new AccountCalendarCell( (TextView)this.layout.findViewById(R.id.calendar_cell_17), this.activity) );
        calendarCells.add( new AccountCalendarCell( (TextView)this.layout.findViewById(R.id.calendar_cell_18), this.activity) );
        calendarCells.add( new AccountCalendarCell( (TextView)this.layout.findViewById(R.id.calendar_cell_19), this.activity) );
        calendarCells.add( new AccountCalendarCell( (TextView)this.layout.findViewById(R.id.calendar_cell_20), this.activity) );
        calendarCells.add( new AccountCalendarCell( (TextView)this.layout.findViewById(R.id.calendar_cell_21), this.activity) );
        calendarCells.add( new AccountCalendarCell( (TextView)this.layout.findViewById(R.id.calendar_cell_22), this.activity) );
        calendarCells.add( new AccountCalendarCell( (TextView)this.layout.findViewById(R.id.calendar_cell_23), this.activity) );
        calendarCells.add( new AccountCalendarCell( (TextView)this.layout.findViewById(R.id.calendar_cell_24), this.activity) );
        calendarCells.add( new AccountCalendarCell( (TextView)this.layout.findViewById(R.id.calendar_cell_25), this.activity) );
        calendarCells.add( new AccountCalendarCell( (TextView)this.layout.findViewById(R.id.calendar_cell_26), this.activity) );
        calendarCells.add( new AccountCalendarCell( (TextView)this.layout.findViewById(R.id.calendar_cell_27), this.activity) );
        calendarCells.add( new AccountCalendarCell( (TextView)this.layout.findViewById(R.id.calendar_cell_28), this.activity) );
        calendarCells.add( new AccountCalendarCell( (TextView)this.layout.findViewById(R.id.calendar_cell_29), this.activity) );
        calendarCells.add( new AccountCalendarCell( (TextView)this.layout.findViewById(R.id.calendar_cell_30), this.activity) );
        calendarCells.add( new AccountCalendarCell( (TextView)this.layout.findViewById(R.id.calendar_cell_31), this.activity) );
        calendarCells.add( new AccountCalendarCell( (TextView)this.layout.findViewById(R.id.calendar_cell_32), this.activity) );
        calendarCells.add( new AccountCalendarCell( (TextView)this.layout.findViewById(R.id.calendar_cell_33), this.activity) );
        calendarCells.add( new AccountCalendarCell( (TextView)this.layout.findViewById(R.id.calendar_cell_34), this.activity) );
        calendarCells.add( new AccountCalendarCell( (TextView)this.layout.findViewById(R.id.calendar_cell_35), this.activity) );
        calendarCells.add( new AccountCalendarCell( (TextView)this.layout.findViewById(R.id.calendar_cell_36), this.activity) );
        calendarCells.add( new AccountCalendarCell( (TextView)this.layout.findViewById(R.id.calendar_cell_37), this.activity) );
        calendarCells.add( new AccountCalendarCell( (TextView)this.layout.findViewById(R.id.calendar_cell_38), this.activity) );
        calendarCells.add( new AccountCalendarCell( (TextView)this.layout.findViewById(R.id.calendar_cell_39), this.activity) );
        calendarCells.add( new AccountCalendarCell( (TextView)this.layout.findViewById(R.id.calendar_cell_40), this.activity) );
        calendarCells.add( new AccountCalendarCell( (TextView)this.layout.findViewById(R.id.calendar_cell_41), this.activity) );
    }

    /**
      * @brief Attach Observer.
      * @param observer ObserverInterface Instance.
      */
    public void attachObserver(ClickObserverInterface observer) {
        this.observer = observer;
    }

    /**
      * @brief Create Calendar.
      */
    protected void createCalendar() {
        int day = 1;
        int year = Integer.valueOf(Utility.splitYear(this.appearDate));
        int month = Integer.valueOf(Utility.splitMonth(this.appearDate));

        // Create Calendar.
        for( int row = 0 ; row < CALENDAR_ROW_NUM ; ++row ) {
            for( int week = 0 ; week < CALENDAR_DAY_OF_WEEK_NUM ; ++week ) {
                AccountCalendarCell cell = calendarCells.get(row * CALENDAR_DAY_OF_WEEK_NUM + week );
                cell.attachObserver(this);
                cell.setCheckedImage(false);
                cell.setClickable(false);

                if( row * CALENDAR_DAY_OF_WEEK_NUM + week >= getStartPosition() &&
                    row * CALENDAR_DAY_OF_WEEK_NUM + week <= getEndPosition() ) {
                    String date = Utility.createDateFormat(year, month, day);
                    int day_of_week = Utility.getDayOfWeek(date);

                    // setting calendar cell.
                    cell.setText(String.valueOf(day));
                    cell.setDate(year, month, day++, day_of_week);
                    cell.setClickable(true);

                    // check exsit data.
                    if( this.accountTableAccessor.isExsitRecordAtTargetDate(date) ) {
                        cell.setCheckedImage(true);
                    }
                }
            }
        }
    }

    /**
      * @brief Focus Current Date.
      */
    protected void focusCurrentDate() {
        if( null != currentCell ) {
            Resources resources = activity.getResources();
            currentCell.setBackgroundColor(resources.getColor(R.color.default_background));
        }

        currentCell = calendarCells.get(getStartPosition() + Integer.valueOf(Utility.splitDay(this.appearDate)) - 1);

        // color setting.
        Resources resources = activity.getResources();
        currentCell.setBackgroundColor(resources.getColor(R.color.focus_background));
    }

    /**
     * @brief Get Calendar Start Position.
     * @return int Calendar Start Position.
     */
    protected int getStartPosition() {
        // calednar start if index zero(day of week start is index one).
        return Utility.getDayOfWeek(Utility.getFirstDateOfTargetMonth(this.appearDate)) - 1;
    }

    /**
     * @brief Get Calendar End Position.
     * @return int Calendar End Position.
     */
    protected int getEndPosition() {
        int cal_st_pos = getStartPosition();
        int end_month = Integer.valueOf(Utility.splitDay(Utility.getLastDateOfTargetMonth(this.appearDate)));

        return cal_st_pos + end_month - 1;
    }

    /**
     * @brief Click Event from AccountCalendarCell Instance.
     * @param event Account Calendar Cell Instance.
     */
    @Override
    public void notifyClick(Object event) {
        if( true == Utility.isStringNULL(((AccountCalendarCell)event).getDate()) ) return;

        // focus current cell.
        focusCurrentCell((AccountCalendarCell)event);

       // notify observer.
        if( null != this.observer ) this.observer.notifyClick(event);
    }

    /**
     * @brief Long Click Event from AccountCalendarCell Instance.
     * @param event Account Calendar Cell Instance.
     */
    @Override
    public void notifyLongClick(Object event) {
        if( true == Utility.isStringNULL(((AccountCalendarCell)event).getDate()) ) return;

        // focus current cell.
        focusCurrentCell((AccountCalendarCell)event);

        if( null != this.observer ) this.observer.notifyLongClick(event);
    }

    /**
     * @brief Onfiling Event.
     */
    @Override
    public void notifyOnFling(Object event, MotionEvent motion_start, MotionEvent motion_end, float velocityX, float velocityY) {
        if( null != this.observer ) {
            this.observer.notifyOnFling(this, motion_start, motion_end, velocityX, velocityY);
            focusCurrentDate();
        }
    }

    /**
     * @brief Focus Current Calendar Cell.
     */
    protected void focusCurrentCell(AccountCalendarCell current_cell) {
        // previous cell setting.
        Resources resources = activity.getResources();
        currentCell.setBackgroundColor(resources.getColor(R.color.default_background));

        // current cell setteing.
        currentCell = current_cell;
        currentCell.setBackgroundColor(resources.getColor(R.color.focus_background));
    }

    // not implement.
    @Override
    public void notifyLongClickForDailyInfo(Object event) {}
}


