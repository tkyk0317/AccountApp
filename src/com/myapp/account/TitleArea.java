package com.myapp.account;

import java.util.*;
import java.text.*;
import android.app.Activity;
import android.widget.TextView;
import android.content.Context;

/**
 * @brief TitleArea Class
 */
public class TitleArea
{
    protected static final String DATE_FORMAT = "yyyy/MM/dd";

    /**
     * Class Constractor.
     */
    TitleArea() {}

    /**
     * Appear the Title Area.
     * @param activity Activity Instance.
     */
    public void appear(Activity activity) {
        appearCurrentDate(activity);
    }

    /**
     * Appear Current Date.
     * @param activity Activity Instance.
     */
    protected void appearCurrentDate(Activity activity) {
        TextView date_title = (TextView) activity.findViewById(R.id.date_title);
        String current_date = (new SimpleDateFormat(DATE_FORMAT)).format(new Date());
        date_title.setText(current_date);
    }
}
