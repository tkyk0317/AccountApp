package com.myapp.account.calendar;

import java.util.*;

import android.app.Activity;
import android.util.Log;
import android.graphics.Color;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView;
import android.text.TextPaint;
import android.view.View;
import android.view.Gravity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.GestureDetector.OnGestureListener;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.widget.LinearLayout;

import com.myapp.account.R;
import com.myapp.account.utility.Utility;
import com.myapp.account.observer.ClickObserverInterface;

/**
 * @brief Account Calendar Cell Class.
 */
public class AccountCalendarCell implements OnGestureListener, View.OnTouchListener {

    private Activity activity = null;
    private LinearLayout layout = null;
    private LinearLayout image_layout = null;
    private TextView textView = null;
    private int year = 0;
    private int month = 0;
    private int day = 0;
    private int dayOfWeek = 0;
    private String date = null;
    private ClickObserverInterface observer = null;
    private GestureDetector gestureDetector = null;
    private static final int IMAGE_WIDTH = 16;
    private static final int IMAGE_HEIGHT = 16;
    private static final int TEXT_ONLY_HEIGHT = 32;

    /**
     * @brief Constractor.
     */
    public AccountCalendarCell(LinearLayout layout, Activity activity) {
        this.layout = layout;
        this.layout.setClickable(false);
        this.activity = activity;

        this.textView = new TextView(this.activity);
        this.layout.addView(this.textView);
        this.textView.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        this.textView.setHeight(TEXT_ONLY_HEIGHT);
        this.textView.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
        this.textView.setOnTouchListener(this);

        this.gestureDetector = new GestureDetector(this.activity, this);
    }

    /**
     * @brief Set Observer Instance.
     * @param observer Observer Instance.
     */
    public void attachObserver(ClickObserverInterface observer) {
        this.observer = observer;
    }

    /**
     * @brief Set Date of Cell.
     * @param int year.
     * @param int month.
     * @param int day.
     * @param int day of week.
     * @param String date.
     */
    public void setDate(int year, int month, int day, int day_of_week, String date) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.dayOfWeek = day_of_week;
        this.date = date;

        // set holiday color.
        setHolidayColor();
    }

    /**
     * @brief Set Holiday Color(SunDay=Red SaturDay=Blue).
     */
    private void setHolidayColor() {
        switch(this.dayOfWeek) {
            case Calendar.SUNDAY:
                this.textView.setTextColor(Color.RED);
                break;
            case Calendar.SATURDAY:
                this.textView.setTextColor(Color.BLUE);
                break;
            default:
                break;
        }
    }

    /**
     * @brief set underline.
     */
    public void setUnderline(boolean is_underline) {
        TextPaint text_paint = this.textView.getPaint();
        text_paint.setUnderlineText(is_underline);
    }

    /**
     * @brief Set Marker to start day.
     */
    public void setStartDayMarker() {
        // create image layout.
        createLinearLayoutForImageView();

        Resources resources = this.activity.getResources();
        Drawable mark_image = resources.getDrawable(R.drawable.start_sign);

        ImageView mark_image_view = new ImageView(this.activity);
        mark_image_view.setImageDrawable(mark_image);
        this.image_layout.addView(mark_image_view);

        mark_image_view.setLayoutParams(new LinearLayout.LayoutParams(IMAGE_WIDTH, IMAGE_HEIGHT));
        mark_image_view.setScaleType(ImageView.ScaleType.CENTER_CROP);

        // change text height.
        this.textView.setHeight(IMAGE_HEIGHT);
    }

    /**
     * @brief Set BackgroundColor.
     * @param int Background Color.
     */
    public void setBackgroundColor(int color) {
        this.layout.setBackgroundColor(color);
    }

    /**
     * @brief Set Cell Text.
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

        if( is_checked ) {
            // create linear layout for image view.
            createLinearLayoutForImageView();

            Resources resources = this.activity.getResources();
            Drawable check_image = resources.getDrawable(R.drawable.circle_red);

            // create view image.
            ImageView check_image_view = new ImageView(this.activity);
            check_image_view.setImageDrawable(check_image);
            this.image_layout.addView(check_image_view);

            check_image_view.setLayoutParams(new LinearLayout.LayoutParams(IMAGE_WIDTH, IMAGE_HEIGHT));
            check_image_view.setScaleType(ImageView.ScaleType.FIT_CENTER);

            // change text height.
            this.textView.setHeight(IMAGE_HEIGHT);
        } else {
            if( null != this.image_layout ) this.image_layout.removeAllViews();
        }
    }

    /**
     * @brief Create LinearLayout for ImageView.
     */
    private void createLinearLayoutForImageView() {
        if( null != this.image_layout ) return;

        this.image_layout = new LinearLayout(this.activity);
        this.layout.addView(this.image_layout);
        this.image_layout.setOrientation(0);
        this.image_layout.setGravity(Gravity.CENTER_HORIZONTAL);
        this.image_layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                                                        LinearLayout.LayoutParams.WRAP_CONTENT));
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


