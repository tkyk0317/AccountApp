package com.myapp.account.titlearea;

import java.util.*;
import java.text.*;
import android.app.Activity;
import android.widget.TextView;
import android.content.Context;
import com.myapp.account.R;

/**
 * @brief TitleArea Class
 */
public class TitleArea
{
    protected Activity activity;
    protected static final String DATE_FORMAT = "yyyy/MM/dd";

    /**
     * Class Constractor.
     * @param activity Activity Instance.
     */
    public TitleArea(Activity activity) {
        this.activity = activity;
    }

    /**
     * Appear the Title Area.
     */
    public void appear() {
        appearCurrentDate();
    }

    /**
     * Appear Current Date.
     */
    protected void appearCurrentDate() {
        TextView date_title = (TextView) activity.findViewById(R.id.date_title);
        String current_date = (new SimpleDateFormat(DATE_FORMAT)).format(new Date());
        date_title.setText(current_date);
    }
}
