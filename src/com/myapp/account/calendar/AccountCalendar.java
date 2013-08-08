package com.myapp.account.calendar;

import java.util.*;

import android.app.Activity;
import android.content.res.Resources;
import android.widget.LinearLayout;
import android.view.MotionEvent;

import com.myapp.account.R;
import com.myapp.account.factory.Factory;
import com.myapp.account.utility.Utility;
import com.myapp.account.config.AppConfigurationData;
import com.myapp.account.observer.ClickObserverInterface;
import com.myapp.account.database.AccountTableAccessor;
import com.myapp.account.database.AccountTableRecord;

/**
 * @brief Account Calendar Class.
 */
public class AccountCalendar implements ClickObserverInterface {

    private Activity activity = null;
    private List<AccountCalendarCell> calendarCells = new ArrayList<AccountCalendarCell>(CALENDAR_DAY_OF_WEEK_NUM*CALENDAR_ROW_NUM);
    private ClickObserverInterface observer = null;
    private AccountCalendarCell currentCell = null;
    private LinearLayout layout = null;
    private AppConfigurationData appConfig = null;
    private String appearDate = null;
    private AccountTableAccessor accountTableAccessor = null;
    private List<AccountTableRecord> accountTableRecord = null;
    private String firstDateOfMonth = null;
    private String lastDateOfMonth = null;
    private AccountCalendarCell startMarkerCell = null;
    private AccountCalendarCell underLineCell = null;
    private static final int CALENDAR_DAY_OF_WEEK_NUM = 7;
    private static final int CALENDAR_ROW_NUM = 6;

    /**
     * @brief Constructor.
     */
    public AccountCalendar(Activity activity, LinearLayout layout) {
        this.activity = activity;
        this.layout = layout;
        this.appConfig = Factory.getAppConfigurationData(activity);
        this.accountTableAccessor = Factory.getAccountTableAcceessor(activity);
        getCalendarItems();
    }

    /**
     * @brief appear the Calendar.
     */
    public void appear(String target_date) {
        this.appearDate = target_date;
        this.firstDateOfMonth = Utility.getFirstDateOfTargetMonth(this.appearDate);
        this.lastDateOfMonth = Utility.getLastDateOfTargetMonth(this.appearDate);

        // clear start marker and underline.
        if( null != this.startMarkerCell ) this.startMarkerCell.clearStartMarker();
        if( null != this.underLineCell ) this.underLineCell.setUnderline(false);

        // create calnedar.
        createCalendar();
        setStartDayMarker();
        setUnderlineToCurrentDay();
        focusCurrentDate();
    }

    /**
     * @brief Get CalendarItems.
     */
    private void getCalendarItems() {
        this.calendarCells.add( new AccountCalendarCell( (LinearLayout)this.layout.findViewById(R.id.calendar_cell_layout_0), this.activity) );
        this.calendarCells.add( new AccountCalendarCell( (LinearLayout)this.layout.findViewById(R.id.calendar_cell_layout_1), this.activity) );
        this.calendarCells.add( new AccountCalendarCell( (LinearLayout)this.layout.findViewById(R.id.calendar_cell_layout_2), this.activity) );
        this.calendarCells.add( new AccountCalendarCell( (LinearLayout)this.layout.findViewById(R.id.calendar_cell_layout_3), this.activity) );
        this.calendarCells.add( new AccountCalendarCell( (LinearLayout)this.layout.findViewById(R.id.calendar_cell_layout_4), this.activity) );
        this.calendarCells.add( new AccountCalendarCell( (LinearLayout)this.layout.findViewById(R.id.calendar_cell_layout_5), this.activity) );
        this.calendarCells.add( new AccountCalendarCell( (LinearLayout)this.layout.findViewById(R.id.calendar_cell_layout_6), this.activity) );
        this.calendarCells.add( new AccountCalendarCell( (LinearLayout)this.layout.findViewById(R.id.calendar_cell_layout_7), this.activity) );
        this.calendarCells.add( new AccountCalendarCell( (LinearLayout)this.layout.findViewById(R.id.calendar_cell_layout_8), this.activity) );
        this.calendarCells.add( new AccountCalendarCell( (LinearLayout)this.layout.findViewById(R.id.calendar_cell_layout_9), this.activity) );
        this.calendarCells.add( new AccountCalendarCell( (LinearLayout)this.layout.findViewById(R.id.calendar_cell_layout_10), this.activity) );
        this.calendarCells.add( new AccountCalendarCell( (LinearLayout)this.layout.findViewById(R.id.calendar_cell_layout_11), this.activity) );
        this.calendarCells.add( new AccountCalendarCell( (LinearLayout)this.layout.findViewById(R.id.calendar_cell_layout_12), this.activity) );
        this.calendarCells.add( new AccountCalendarCell( (LinearLayout)this.layout.findViewById(R.id.calendar_cell_layout_13), this.activity) );
        this.calendarCells.add( new AccountCalendarCell( (LinearLayout)this.layout.findViewById(R.id.calendar_cell_layout_14), this.activity) );
        this.calendarCells.add( new AccountCalendarCell( (LinearLayout)this.layout.findViewById(R.id.calendar_cell_layout_15), this.activity) );
        this.calendarCells.add( new AccountCalendarCell( (LinearLayout)this.layout.findViewById(R.id.calendar_cell_layout_16), this.activity) );
        this.calendarCells.add( new AccountCalendarCell( (LinearLayout)this.layout.findViewById(R.id.calendar_cell_layout_17), this.activity) );
        this.calendarCells.add( new AccountCalendarCell( (LinearLayout)this.layout.findViewById(R.id.calendar_cell_layout_18), this.activity) );
        this.calendarCells.add( new AccountCalendarCell( (LinearLayout)this.layout.findViewById(R.id.calendar_cell_layout_19), this.activity) );
        this.calendarCells.add( new AccountCalendarCell( (LinearLayout)this.layout.findViewById(R.id.calendar_cell_layout_20), this.activity) );
        this.calendarCells.add( new AccountCalendarCell( (LinearLayout)this.layout.findViewById(R.id.calendar_cell_layout_21), this.activity) );
        this.calendarCells.add( new AccountCalendarCell( (LinearLayout)this.layout.findViewById(R.id.calendar_cell_layout_22), this.activity) );
        this.calendarCells.add( new AccountCalendarCell( (LinearLayout)this.layout.findViewById(R.id.calendar_cell_layout_23), this.activity) );
        this.calendarCells.add( new AccountCalendarCell( (LinearLayout)this.layout.findViewById(R.id.calendar_cell_layout_24), this.activity) );
        this.calendarCells.add( new AccountCalendarCell( (LinearLayout)this.layout.findViewById(R.id.calendar_cell_layout_25), this.activity) );
        this.calendarCells.add( new AccountCalendarCell( (LinearLayout)this.layout.findViewById(R.id.calendar_cell_layout_26), this.activity) );
        this.calendarCells.add( new AccountCalendarCell( (LinearLayout)this.layout.findViewById(R.id.calendar_cell_layout_27), this.activity) );
        this.calendarCells.add( new AccountCalendarCell( (LinearLayout)this.layout.findViewById(R.id.calendar_cell_layout_28), this.activity) );
        this.calendarCells.add( new AccountCalendarCell( (LinearLayout)this.layout.findViewById(R.id.calendar_cell_layout_29), this.activity) );
        this.calendarCells.add( new AccountCalendarCell( (LinearLayout)this.layout.findViewById(R.id.calendar_cell_layout_30), this.activity) );
        this.calendarCells.add( new AccountCalendarCell( (LinearLayout)this.layout.findViewById(R.id.calendar_cell_layout_31), this.activity) );
        this.calendarCells.add( new AccountCalendarCell( (LinearLayout)this.layout.findViewById(R.id.calendar_cell_layout_32), this.activity) );
        this.calendarCells.add( new AccountCalendarCell( (LinearLayout)this.layout.findViewById(R.id.calendar_cell_layout_33), this.activity) );
        this.calendarCells.add( new AccountCalendarCell( (LinearLayout)this.layout.findViewById(R.id.calendar_cell_layout_34), this.activity) );
        this.calendarCells.add( new AccountCalendarCell( (LinearLayout)this.layout.findViewById(R.id.calendar_cell_layout_35), this.activity) );
        this.calendarCells.add( new AccountCalendarCell( (LinearLayout)this.layout.findViewById(R.id.calendar_cell_layout_36), this.activity) );
        this.calendarCells.add( new AccountCalendarCell( (LinearLayout)this.layout.findViewById(R.id.calendar_cell_layout_37), this.activity) );
        this.calendarCells.add( new AccountCalendarCell( (LinearLayout)this.layout.findViewById(R.id.calendar_cell_layout_38), this.activity) );
        this.calendarCells.add( new AccountCalendarCell( (LinearLayout)this.layout.findViewById(R.id.calendar_cell_layout_39), this.activity) );
        this.calendarCells.add( new AccountCalendarCell( (LinearLayout)this.layout.findViewById(R.id.calendar_cell_layout_40), this.activity) );
        this.calendarCells.add( new AccountCalendarCell( (LinearLayout)this.layout.findViewById(R.id.calendar_cell_layout_41), this.activity) );
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
    private void createCalendar() {
        int day = 1;
        int year = Integer.valueOf(Utility.splitYear(this.appearDate));
        int month = Integer.valueOf(Utility.splitMonth(this.appearDate));
        int st_pos = getStartPosition();
        int end_pos = getEndPosition();

        // get account table record.
        this.accountTableRecord = this.accountTableAccessor.getRecord(this.firstDateOfMonth, this.lastDateOfMonth);

        // Create Calendar.
        for( int row = 0 ; row < CALENDAR_ROW_NUM ; ++row ) {
            for( int week = 0 ; week < CALENDAR_DAY_OF_WEEK_NUM ; ++week ) {
                AccountCalendarCell cell = this.calendarCells.get(row * CALENDAR_DAY_OF_WEEK_NUM + week );
                cell.attachObserver(this);

                int current_pos = row * CALENDAR_DAY_OF_WEEK_NUM + week;
                if( current_pos >= st_pos && current_pos <= end_pos ) {

                    // setting calendar cell.
                    cell.setTextHeight(AccountCalendarCell.TEXT_ONLY_HEIGHT);
                    cell.setText(String.valueOf(day));
                    cell.setDate(year, month, day++, Utility.getDayOfWeekByCalendarPos(current_pos));
                    cell.setClickable(true);

                    // check exsit record.
                    setCheckedImage(cell);
                } else {
                    cell.setTextHeight(AccountCalendarCell.TEXT_ONLY_HEIGHT);
                    cell.setClickable(false);
                    clearCalendarText(cell);
                    cell.clearImage();
                }
            }
        }
        // delete list.
        this.accountTableRecord.clear();
        this.accountTableRecord = null;
    }

    /**
     * @brief Clear Account Calendar Cell Text.
     *
     * @param cell AccountCalendarCell Instance.
     */
    private void clearCalendarText(AccountCalendarCell cell) {
        cell.setDate(0, 0, 0, 0);
        cell.setText("");
    }

    /**
     * @brief Set Marker to Start Day.
     */
    private void setStartDayMarker() {
        int start_position = getStartPosition();
        int last_day = Integer.valueOf(Utility.splitDay(Utility.getLastDateOfTargetMonth(this.appearDate)));
        int start_day = this.appConfig.getStartDay();

        if( start_day > last_day ) start_day = last_day;

        // set marker.
        this.calendarCells.get(start_position + start_day - 1).setStartDayMarker();
        this.startMarkerCell = this.calendarCells.get(start_position + start_day - 1);
    }

    /**
     * @brief Set Underline to Current Day.
     */
    private void setUnderlineToCurrentDay() {
        String current_date = Utility.getCurrentDate();
        String appear_year_month = Utility.splitYearAndMonth(this.appearDate);
        String current_year_month = Utility.splitYearAndMonth(current_date);

        if( true == appear_year_month.equals(current_year_month) ) {
            int current_day = Integer.valueOf(Utility.splitDay(current_date));
            int start_position = getStartPosition();

            // set marker to current day.
            this.calendarCells.get(current_day + start_position - 1).setUnderline(true);
            this.underLineCell = this.calendarCells.get(current_day + start_position - 1);
        }
    }

    /**
     * @brief Set Checked Image.
     *
     * @param cell AccountCalendarCell Instance.
     */
    private void setCheckedImage(AccountCalendarCell cell) {
        if( false == isExistRecord(cell.getDate()) ) {
            cell.clearImage();
        } else {
            cell.setCheckedImage();
        }
    }

    /**
     * @brief Check Exist Record.
     *
     * @param date Check Date.
     *
     * @return true:exist record false:not exist record.
     */
    private boolean isExistRecord(String date) {
        boolean is_exist = false;

        // check exsit record.
        for( AccountTableRecord record : this.accountTableRecord ) {
            if( true == date.equals(record.getInsertDate()) ) {
                is_exist = true;
                break;
            }
        }
        return is_exist;
     }

    /**
      * @brief Focus Current Date.
      */
    private void focusCurrentDate() {
        if( null != this.currentCell ) {
            Resources resources = activity.getResources();
            this.currentCell.setBackgroundColor(resources.getColor(R.color.default_background));
        }

        this.currentCell = this.calendarCells.get(getStartPosition() + Integer.valueOf(Utility.splitDay(this.appearDate)) - 1);

        // color setting.
        Resources resources = activity.getResources();
        this.currentCell.setBackgroundColor(resources.getColor(R.color.focus_background));
    }

    /**
     * @brief Get Calendar Start Position.
     * @return integer Calendar Start Position.
     */
    private int getStartPosition() {
        // calendar start if index zero(day of week start is index one).
        return Utility.getDayOfWeek(this.firstDateOfMonth) - 1;
    }

    /**
     * @brief Get Calendar End Position.
     * @return integer Calendar End Position.
     */
    private int getEndPosition() {
        int cal_st_pos = getStartPosition();
        int end_month = Integer.valueOf(Utility.splitDay(this.lastDateOfMonth));

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
     * @brief Onfling Event.
     */
    @Override
    public void notifyOnFling(Object event, MotionEvent motion_start, MotionEvent motion_end, float velocityX, float velocityY) {
        if( null != this.observer ) {
            // notify onFilng.
            this.observer.notifyOnFling(this, motion_start, motion_end, velocityX, velocityY);
        }
    }

    /**
     * @brief Focus Current Calendar Cell.
     */
    private void focusCurrentCell(AccountCalendarCell current_cell) {
        // previous cell setting.
        Resources resources = activity.getResources();
        this.currentCell.setBackgroundColor(resources.getColor(R.color.default_background));

        // current cell setting.
        this.currentCell = current_cell;
        this.currentCell.setBackgroundColor(resources.getColor(R.color.focus_background));
    }

    // not supported.
    @Override
    public void notifyLongClick(Object event) {
    }
}

