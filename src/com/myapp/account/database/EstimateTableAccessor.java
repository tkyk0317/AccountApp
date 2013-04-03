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
 * @brief EstimateTableImpl Class.
 */
public class EstimateTableAccessor {

    protected SQLiteDatabase readDatabase = null;
    protected SQLiteDatabase writeDatabase = null;
    protected static final String TABLE_NAME = "EstimateTable";

    /**
     * @brief Constractor.
     */
    public EstimateTableAccessor(SQLiteOpenHelper helper) {
        if( null == this.readDatabase ) this.readDatabase = helper.getReadableDatabase();
        if( null == this.writeDatabase ) this.writeDatabase = helper.getWritableDatabase();
    }

    /**
     * @brief Finalize Process.
     */
    @Override
    protected void finalize() throws Throwable {
        try {
            super.finalize();
        } finally {
            terminate();
        }
    }

    /**
     * @brief Terminate process.
     */
    public void terminate() {
        this.readDatabase.close();
        this.writeDatabase.close();
    }

    /**
     * @brief Get Record With Current Month.
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
        cursor.close();
        return record;
    }

    /**
     * @brief Get Record at TargetDate
     * @param target_date TargetDate(yyyy/mm).
     * @return EstimateRecord Instance.
     *
     */
    public EstimateTableRecord getRecordAtTargetDate(String target_date) {
        Cursor cursor = readDatabase.query(TABLE_NAME, null, "target_date=?", new String[] {target_date}, null, null, null, null);

        EstimateTableRecord record = new EstimateTableRecord();
        if( true == cursor.moveToFirst() ) {
            record.set(cursor);
        }
        cursor.close();
        return record;
    }

    /**
     * @brief Check Estimate Record at TargetDate.
     * @param target_date Specify Checked Date(yyyy/dd).
     * @return true:exsit false:not exsit.
     */
    public boolean isEstimateRecord(String target_date) {
        Cursor cursor = readDatabase.query(TABLE_NAME, null, "target_date=?", new String[] {target_date}, null, null, null, null);

        boolean ret = false;
        if( true == cursor.moveToFirst() ) {
            if( cursor.getCount() > 0 ) {
                ret = true;
            }
        }
        cursor.close();
        return ret;
    }

    /**
     * @brief Insert Record in EstimateTable.
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

    /**
     * @brief Update Record in EstimateTAble.
     * @param record EstimateTableRecord Instance.
     */
    public int update(EstimateTableRecord record) {
        ContentValues update_record = new ContentValues();
        update_record.put("money", record.getEstimateMoney());
        update_record.put("target_date", record.getTargetDate());
        update_record.put("update_date", record.getUpdateDate());
        update_record.put("insert_date", record.getInsertDate());

        return writeDatabase.update(TABLE_NAME, update_record, "_id=" + String.valueOf(record.getId()), null);
    }
}

