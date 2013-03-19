package com.myapp.account.calendar;

import java.util.*;
import android.app.Activity;
import android.widget.TextView;
import android.util.Log;
import android.view.View;
import android.view.Gravity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.GestureDetector.OnGestureListener;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import com.myapp.account.R;
import com.myapp.account.observer.ClickObserverInterface;
import com.myapp.account.utility.Utility;

/**
 * Account Calendar Cell Class.
 */
public class AccountCalendarCell implements OnGestureListener, View.OnTouchListener {

    protected Activity activity;
    protected TextView textView;
    protected int year;
    protected int month;
    protected int day;
    protected int dayOfWeek;
    protected String date;
    protected ClickObserverInterface observer;
    protected GestureDetector gestureDetector;
    protected static final int HEIGHT = 32;

    /**
     * Constractor.
     */
    public AccountCalendarCell(TextView view, Activity activity) {
        this.textView = view;
        this.activity = activity;
        this.textView.setClickable(false);
        this.textView.setHeight(HEIGHT);
        this.textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        this.textView.setOnTouchListener(this);
        gestureDetector = new GestureDetector(this.activity, this);
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
        this.date = Utility.createDateFormat(year, month, day);
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
     * @brief Set Clickable Param.
     * @param is_click Specified Clickable Param.
     */
    public void setClickable(boolean is_click) {
        this.textView.setClickable(is_click);
    }

    /**
     * @brief Set Checked Image.
     * @param is_checked Specified Check Status.
     */
    public void setCheckedImage(boolean is_checked) {
        Drawable check_image = null;
        if( is_checked ) {
            Resources resources = activity.getResources();
            check_image = resources.getDrawable(R.drawable.circle);
        }
        this.textView.setCompoundDrawablesWithIntrinsicBounds(check_image, null, null, null);
    }

    // Getter.
    public String getDate() { return this.date; }

    /**
     * @brief OnTouchEvent.
     * @return true:handle the event false:not handle the event.
     */
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if( false == this.textView.isClickable() ) return false;
        this.gestureDetector.onTouchEvent(event);
        return true;
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


