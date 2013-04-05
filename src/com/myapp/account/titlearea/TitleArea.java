package com.myapp.account.titlearea;

import java.util.*;
import android.app.Activity;
import android.widget.TextView;
import android.content.Context;

import com.myapp.account.R;
import com.myapp.account.utility.Utility;
import com.myapp.account.config.AppConfigurationData;

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
        this.currentDate = current_date;
        displayTitle();
    }

    /**
     * @brief Display Title.
     */
    private void displayTitle() {
        TextView date_title = (TextView)this.activity.findViewById(R.id.date_title);
        String day_of_week_str = Utility.getDayOfWeekString(Utility.getDayOfWeek(this.currentDate), this.activity);
        date_title.setText(this.currentDate + DAY_OF_WEEK_PREFIX + day_of_week_str + DAY_OF_WEEK_SUFIX);        String title = (Utility.splitYear(this.currentDate) + this.activity.getText(R.string.year_str).toString());
    }

    /**
     * @brief Get Estimate Target Date.
     *
     * @return estimate target date.
     */
    private String getEstimateTargetDate() {
        AppConfigurationData app_config = new AppConfigurationData(this.activity);
        return Utility.getEstimateTargetDate(this.currentDate, app_config.getStartDay());
    }
}
