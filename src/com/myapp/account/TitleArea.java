package com.myapp.account;

import android.app.Activity;
import android.widget.TextView;
import android.content.Context;
import android.text.format.Time;
import com.myapp.account.DatabaseHelper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @brief TitleArea Class
 */
public class TitleArea
{
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDatabase;
    /**
     * @brief constractor
     */
    TitleArea(Context context) {
        mDbHelper = new DatabaseHelper(context.getApplicationContext());
        mDatabase = mDbHelper.getReadableDatabase();
    }
    /**
     * @brief appear the title area
     * @param none
     * @return void
     */
    public void appear(Activity act) {
        // display current date
        TextView date_title = (TextView) act.findViewById(R.id.date_title);
        Time time = new Time("Asia/Tokyo");
        time.setToNow();
        String date = time.year + "/" + (time.month+1) + "/" + time.monthDay ;
        date_title.setText(date);
    }
}
