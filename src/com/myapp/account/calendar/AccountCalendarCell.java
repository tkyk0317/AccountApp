package com.myapp.account.calendar;

import java.util.*;
import android.widget.TextView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import com.myapp.account.observer.ClickObserverInterface;
import com.myapp.account.utility.Utility;

/**
 * Account Calendar Cell Class.
 */
public class AccountCalendarCell implements OnClickListener, OnLongClickListener {

    protected TextView textView;
    protected int year;
    protected int month;
    protected int day;
    protected int dayOfWeek;
    protected String date;
    protected ClickObserverInterface observer;

    /**
     * Constractor.
     */
    public AccountCalendarCell(TextView view) {
        this.textView = view;
        this.textView.setOnClickListener(this);
        this.textView.setOnLongClickListener(this);
    }

    /**
     * Set Observer Instance.
     * @param observer Observer Instance.
     */
    public void attachObserver(ClickObserverInterface observer) {
        this.observer = observer;
    }

    /**
     * Set Date of Cell.
     * @param int year.
     * @param int month.
     * @param int day.
     * @param int day of week.
     */
    public void setDate(int year, int month, int day, int day_of_week) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.dayOfWeek = day_of_week;
        this.date = Utility.CreateDateFormat(year, month, day);
    }

    /**
     * Set BackgroundColor.
     * @param int Background Color.
     */
    public void setBackgroundColor(int color) {
        this.textView.setBackgroundColor(color);
    }

    /**
     * Set Cell Text.
     * @param String setting string data.
     */
    public void setText(String text) {
        this.textView.setText(text);
    }

    /**
     * Click Event Listener.
     * @param event View instance.
     */
    @Override
    public void onClick(View event) {
        if( null != this.observer ) this.observer.notifyClick(this);
    }

    /**
      * Long Click Listener.
      * @param event View instance.
      */
    @Override
    public boolean onLongClick(View event) {
        if( null != this.observer ) this.observer.notifyLongClick(this);
        return true;
    }

    // Getter.
    public String getDate() { return this.date; }
}


