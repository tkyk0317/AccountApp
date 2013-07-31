package com.myapp.account.titlearea;

import android.app.Activity;
import android.widget.TextView;

import com.myapp.account.R;
import com.myapp.account.utility.Utility;

/**
 * @brief TitleArea Class
 */
public class TitleArea
{
    private Activity activity;
    private String currentDate;
    private static final String DAY_OF_WEEK_PREFIX = "(";
    private static final String DAY_OF_WEEK_SUFIX = ")";

    /**
     * @brief Class Constructor.
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
        this.currentDate = current_date;
        displayTitle();
    }

    /**
     * @brief Display Title.
     */
    private void displayTitle() {
        TextView date_title = (TextView)this.activity.findViewById(R.id.date_title);
        String day_of_week_str = Utility.getDayOfWeekString(Utility.getDayOfWeek(this.currentDate), this.activity);
        date_title.setText(this.currentDate + DAY_OF_WEEK_PREFIX + day_of_week_str + DAY_OF_WEEK_SUFIX);
    }
}
