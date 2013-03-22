package com.myapp.account.titlearea;

import java.util.*;
import android.app.Activity;
import android.widget.TextView;
import android.content.Context;
import com.myapp.account.R;
import com.myapp.account.utility.Utility;

/**
 * @brief TitleArea Class
 */
public class TitleArea
{
    protected Activity activity;
    protected static final String DAY_OF_WEEK_PREFIX = "(";
    protected static final String DAY_OF_WEEK_SUFIX = ")";

    /**
     * @brief Class Constractor.
     * @param activity Activity Instance.
     */
    public TitleArea(Activity activity) {
        this.activity = activity;
    }

    /**
     * @brief Appear the Title Area.
     * @param current_date current_date String(yyyy/MM/dd).
     */
    public void appear(String current_date) {
        TextView date_title = (TextView) activity.findViewById(R.id.date_title);
        String day_of_week_str = Utility.getDayOfWeekString(Utility.getDayOfWeek(current_date), this.activity);
        date_title.setText(current_date + DAY_OF_WEEK_PREFIX + day_of_week_str + DAY_OF_WEEK_SUFIX);
    }
}
