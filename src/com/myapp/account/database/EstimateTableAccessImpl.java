package com.myapp.account.database;

import java.util.*;
import java.text.*;
import android.util.Log;
import android.database.Cursor;
import android.content.ContentValues;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import com.myapp.account.database.EstimateTableRecord;
import com.myapp.account.utility.Utility;

/**
 * EstimateTableImpl Class.
 */
public class EstimateTableAccessImpl {

    protected SQLiteDatabase readDatabase;
    protected SQLiteDatabase writeDatabase;
    protected static final String TABLE_NAME = "EstimateTable";

    /**
     * Constractor.
     */
    public EstimateTableAccessImpl(SQLiteOpenHelper helper) {
        readDatabase = helper.getReadableDatabase();
        writeDatabase = helper.getWritableDatabase();
    }

    /**
     * Get Record With Current Month.
     * @return EstimateRecord Instance.
     */
    public EstimateTableRecord getRecordWithCurrentMonth() {
        String current_year_month = Utility.getCurrentYearAndMonth();
        Cursor cursor =
            readDatabase.query(TABLE_NAME, null, "target_date=?", new String[] {current_year_month}, null, null, null, null);

        EstimateTableRecord record = new EstimateTableRecord();
        if( true == cursor.moveToFirst() ) {
            record.set(cursor);
        }
        return record;
    }

    /**
     * Insert Record in EstimateTable.
     * @param record EstimateTableRecord Instance.
     * @return Insert Record Key(_id).
     */
    public long insert(EstimateTableRecord record) {
        ContentValues insert_record = new ContentValues();

        insert_record.put("money", record.getEstimateMoney() );
        insert_record.put("target_date", Utility.getCurrentYearAndMonth() );
        insert_record.put("update_date", Utility.getCurrentDate() );
        insert_record.put("insert_date", Utility.getCurrentDate() );

        // insert record.
        return writeDatabase.insert(TABLE_NAME, null, insert_record);
    }
}

