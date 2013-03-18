package com.myapp.account.calendar;

import java.util.*;
import android.app.Activity;
import android.widget.TextView;
import android.util.Log;
import android.view.View;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.GestureDetector.OnGestureListener;
import com.myapp.account.observer.ClickObserverInterface;
import com.myapp.account.utility.Utility;

/**
 * Account Calendar Cell Class.
 */
public class AccountCalendarCell implements OnGestureListener, View.OnTouchListener {
    protected TextView textView;
    protected int year;
    protected int month;
    protected int day;
    protected int dayOfWeek;
    protected String date;
    protected ClickObserverInterface observer;
    protected GestureDetector gestureDetector;

    /**
     * Constractor.
     */
    public AccountCalendarCell(TextView view, Activity activity) {
        this.textView = view;
        this.textView.setOnTouchListener(this);
        gestureDetector = new GestureDetector(activity, this);
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

    // Getter.
    public String getDate() { return this.date; }

    /**
     * @brief OnTouchEvent.
     * @return true:handle the event false:not handle the event.
     */
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        this.gestureDetector.onTouchEvent(event);
        return false;
    }

    /**
     * @brief Down Click Event.
     * @return true:handle the event false:not handle the event.
     */
    @Override
    public boolean onDown(MotionEvent e) {
        if( null != this.observer ) this.observer.notifyClick(this);
        return true;
    }

    /**
     * @brief Long Click Event.
     * @return true:handle the event false:not handle the event.
     */
    @Override
    public void onLongPress(MotionEvent e) {
        if( null != this.observer ) this.observer.notifyLongClick(this);
    }

    /**
     * @brief Flick Event.
     * @return true:handle the event false:not handle the event.
     */
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if( null != this.observer ) this.observer.notifyOnFling(this, e1, e2, velocityX, velocityY);
        return true;
    }

    // not support module.
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float x, float y) { return true; }
    @Override
    public void onShowPress(MotionEvent e) {}
    @Override
    public boolean onSingleTapUp(MotionEvent e) { return false; }
}


