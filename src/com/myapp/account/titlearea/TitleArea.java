package com.myapp.account.titlearea;

import java.util.*;
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
     * @param current_date current_date String(yyyy/MM/dd).
     */
    public void appear(String current_date) {
        TextView date_title = (TextView) activity.findViewById(R.id.date_title);
        date_title.setText(current_date);
    }
}
