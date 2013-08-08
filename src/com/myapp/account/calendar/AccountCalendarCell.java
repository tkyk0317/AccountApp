package com.myapp.account.calendar;

import java.util.*;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView;
import android.os.Build;
import android.text.TextPaint;
import android.view.View;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.widget.RelativeLayout;

import com.myapp.account.R;
import com.myapp.account.utility.Utility;
import com.myapp.account.observer.ClickObserverInterface;

/**
 * @brief Account Calendar Cell Class.
 */
public class AccountCalendarCell extends SimpleOnGestureListener implements View.OnTouchListener {

    private Activity activity = null;
    private LinearLayout layout = null;
    private RelativeLayout image_layout = null;
    private ImageView startMarkerImage = null;
    private ImageView exsitRecordImage = null;
    private TextView textView = null;
    private int year = 0;
    private int month = 0;
    private int day = 0;
    private int dayOfWeek = 0;
    private GestureDetector gestureDetector = null;
    private ClickObserverInterface observer = null;
    private static final int IMAGE_WIDTH = 12;
    private static final int IMAGE_HEIGHT = 18;
    private static final int TEXT_ONLY_HEIGHT = IMAGE_HEIGHT * 2;

    /**
     * @brief Constructor.
     */
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public AccountCalendarCell(LinearLayout layout, Activity activity) {
        this.layout = layout;
        this.layout.setClickable(false);
        this.activity = activity;
        this.textView = new TextView(this.activity.getApplicationContext());
        this.layout.addView(this.textView);
        this.textView.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        this.textView.setHeight(TEXT_ONLY_HEIGHT);
        this.textView.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
        this.textView.setOnTouchListener(this);
        this.gestureDetector = new GestureDetector(this.activity.getApplicationContext(), this);
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
     */
    public void setDate(int year, int month, int day, int day_of_week) {
        this.dayOfWeek = day_of_week;
        this.year = year;
        this.month = month;
        this.day = day;

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

        this.startMarkerImage = new ImageView(this.activity.getApplicationContext());
        this.startMarkerImage.setId(ImageId.START_DAY_IMAGE.getId());
        this.startMarkerImage.setImageDrawable(mark_image);
        this.startMarkerImage.setScaleType(ImageView.ScaleType.CENTER_CROP);

        // add view.
        RelativeLayout.LayoutParams layout_params = new RelativeLayout.LayoutParams(IMAGE_WIDTH, IMAGE_HEIGHT);

        // check layout item num.
        if( 0 < this.image_layout.getChildCount() ) {
            // add again exist image.
            RelativeLayout.LayoutParams exsit_image_param = (RelativeLayout.LayoutParams)this.exsitRecordImage.getLayoutParams();
            exsit_image_param.addRule(RelativeLayout.LEFT_OF, ImageId.RELATIVE_LAYOUT.getId());
            layout_params.addRule(RelativeLayout.RIGHT_OF, ImageId.EXSIT_ACCOUNT_DATA_IMAGE.getId());
        } else {
            layout_params.addRule(RelativeLayout.CENTER_HORIZONTAL, ImageId.RELATIVE_LAYOUT.getId());
        }
        this.image_layout.addView(this.startMarkerImage, layout_params);

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
     * @brief Clear Image.
     */
    public void clearImage() {
        if( null != this.image_layout ) this.image_layout.removeAllViews();
        this.exsitRecordImage = null;
    }

    /**
     * @brief Clear Start Marker.
     */
    public void clearStartMarker() {
        this.startMarkerImage = null;
    }

    /**
     * @brief Set Checked Image.
     * @param is_checked Specified Check Status.
     */
    public void setCheckedImage(boolean is_checked) {
        // set text only size.
        this.textView.setHeight(TEXT_ONLY_HEIGHT);

        if( is_checked ) {
            // create linear layout for image view.
            createLinearLayoutForImageView();

            Resources resources = this.activity.getResources();
            Drawable check_image = resources.getDrawable(R.drawable.circle_red);

            // create view image.
            this.exsitRecordImage = new ImageView(this.activity.getApplicationContext());
            this.exsitRecordImage.setId(ImageId.EXSIT_ACCOUNT_DATA_IMAGE.getId());
            this.exsitRecordImage.setImageDrawable(check_image);
            this.exsitRecordImage.setScaleType(ImageView.ScaleType.FIT_CENTER);

            // add view.
            RelativeLayout.LayoutParams layout_params = new RelativeLayout.LayoutParams(IMAGE_WIDTH, IMAGE_HEIGHT);
            layout_params.addRule(RelativeLayout.CENTER_HORIZONTAL, ImageId.RELATIVE_LAYOUT.getId());
            this.image_layout.addView(this.exsitRecordImage, layout_params);

            // change text height.
            this.textView.setHeight(IMAGE_HEIGHT);
        }
    }

    /**
     * @brief Create LinearLayout for ImageView.
     */
    private void createLinearLayoutForImageView() {
        if( null != this.image_layout ) return;

        this.image_layout = new RelativeLayout(this.activity.getApplicationContext());
        this.image_layout.setId(ImageId.RELATIVE_LAYOUT.getId());
        this.layout.addView(this.image_layout);
        this.image_layout.setGravity(Gravity.CENTER_HORIZONTAL);
        this.image_layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                                                        LinearLayout.LayoutParams.WRAP_CONTENT));
    }

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

    // Getter.
    public String getDate() {
        return Utility.createDateFormat(this.year, this.month, this.day);
    }

    /**
     * @brief Single Tap Event.
     * @return true:handle the event false:not handle the event.
     */
    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        if( null != this.observer ) this.observer.notifyClick(this);
        return true;
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

    /**
     * @brief Image ID Class.
     */
    private enum ImageId {
        START_DAY_IMAGE(0), EXSIT_ACCOUNT_DATA_IMAGE(1), RELATIVE_LAYOUT(2);

        private final int id;

        private ImageId(int id) { this.id = id; }
        public int getId() { return this.id; }
    }
}


