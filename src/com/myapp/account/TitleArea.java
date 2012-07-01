package com.myapp.account;

import java.util.Calendar;
import android.app.Activity;
import android.widget.TextView;
import android.content.Context;
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
        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        String date = year + "/" + (month+1) + "/" + day ;
        date_title.setText(date);
    }
}
