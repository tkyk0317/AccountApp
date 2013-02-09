package com.myapp.account.database;

import java.util.*;
import java.text.*;
import android.util.Log;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import com.myapp.account.database.AccountTableRecord;
import com.myapp.account.utility.Utility;

/**
 * AccountTableAccessImpl.
 */
public class AccountTableAccessImpl {

    protected SQLiteDatabase readDatabase;
    protected SQLiteDatabase writeDatabase;
    protected static final String TABLE_NAME = "AccountTable";

    /**
     * Consturactor.
     */
    public AccountTableAccessImpl(SQLiteOpenHelper helper) {
        readDatabase = helper.getReadableDatabase();
        writeDatabase = helper.getWritableDatabase();
    }

    /**
     * Get Record Specified Key.
     * @param key Table key.
     * @return AccountTableRecord Instance.
     */
    public AccountTableRecord getRecord(int key) {
        Cursor cursor = readDatabase.rawQuery("select * from " + TABLE_NAME + " where _id = " + key + ";", null);

        AccountTableRecord record = new AccountTableRecord();
        if( true == cursor.moveToFirst() ) {
            record.set(cursor);
        }
        return record;
    }

    /**
     * Get Record with Current Date.
     * @return AccountTableRecord at Current Date.
     */
    public List<AccountTableRecord> getRecordWithCurrentDate()
    {
        String current_date = Utility.getCurrentDate();
        Cursor cursor = readDatabase.query(TABLE_NAME, null, "insert_date=?" , new String[]{current_date}, null, null, null, null);

        cursor.moveToFirst();
        int record_count = cursor.getCount();
        List<AccountTableRecord> record_list = new ArrayList<AccountTableRecord>(record_count+1);

        for( int i = 0 ; i < record_count ; i++ ) {
            record_list.add( new AccountTableRecord() );
            record_list.get(i).set(cursor);
            cursor.moveToNext();
        }
        return record_list;
    }

    /**
     * Get Record With Current Date Group by CategoryId.
     * @return AccountTableRecord List at Current Date.
     */
    public List<AccountTableRecord> getRecordWithCurrentDateGroupByCategoryId()
    {
        String current_date = Utility.getCurrentDate();
        Cursor cursor = readDatabase.query(TABLE_NAME, new String [] { "_id", "category_id", "sum(money)", "memo", "update_date", "insert_date" },
                                           "insert_date=?" , new String[]{current_date}, "category_id", null, null, null);

        cursor.moveToFirst();
        int record_count = cursor.getCount();
        List<AccountTableRecord> record_list = new ArrayList<AccountTableRecord>(record_count+1);

        for( int i = 0 ; i < record_count ; i++ ) {
            record_list.add( new AccountTableRecord() );
            record_list.get(i).set(cursor);
            cursor.moveToNext();
        }
        return record_list;
    }

    /**
     * Get All Record.
     * @return All AccountTableRecord in AccountMasterTable.
     */
    public List<AccountTableRecord> getAllRecord() {
        Cursor cursor = readDatabase.query(TABLE_NAME, null , null, null, null, null, null, null);
        cursor.moveToFirst();
        int record_count = cursor.getCount();
        List<AccountTableRecord> record_list = new ArrayList<AccountTableRecord>(record_count+1);

        for( int i = 0 ; i < record_count ; i++ ) {
            record_list.add( new AccountTableRecord() );
            record_list.get(i).set(cursor);
            cursor.moveToNext();
        }
        return record_list;
    }

    /**
     * Get Income Total Money at Current Month.
     * @return Total Money.
     */
    public int getTotalIncomeAtCurrentMonth() {
        String last_date_of_month = Utility.getLastDateOfCurrentMonth();
        String first_date_of_month = Utility.getFirstDateOfCurrentMonth();

        Cursor cursor = readDatabase.rawQuery("select sum(AccountTable.money) from AccountTable " +
                "join AccountMaster on AccountTable.category_id=AccountMaster._id " +
                "where AccountMaster.kind_id=0 and AccountTable.insert_date>=" + "'" + first_date_of_month + "'" +
                " and AccountTable.insert_date<=" + "'" + last_date_of_month + "'" + " ;", null);
        cursor.moveToFirst();

        return cursor.getInt(0);
    }

    /**
     * Get Payment Total Money at Current Month.
     * @return Total Money.
     */
    public int getTotalPaymentAtCurrentMonth() {
        String last_date_of_month = Utility.getLastDateOfCurrentMonth();
        String first_date_of_month = Utility.getFirstDateOfCurrentMonth();

        Cursor cursor = readDatabase.rawQuery("select sum(AccountTable.money) from AccountTable " +
                "join AccountMaster on AccountTable.category_id=AccountMaster._id " +
                "where AccountMaster.kind_id=1 and AccountTable.insert_date>=" + "'" + first_date_of_month + "'" +
                " and AccountTable.insert_date<=" + "'" + last_date_of_month + "'" + " ;", null);
        cursor.moveToFirst();

        return cursor.getInt(0);
    }

    /**
     * Insert Record in AccountMasterTable.
     * @param record AccountTableRecord Instance.
     * @return Insert Record Key(_id).
     */
    public long insert(AccountTableRecord record) {
        ContentValues insert_record = new ContentValues();
        insert_record.put("category_id", record.getCategoryId());
        insert_record.put("money", record.getMoney());
        insert_record.put("memo", record.getMemo());
        insert_record.put("update_date", Utility.getCurrentDate() );
        insert_record.put("insert_date", Utility.getCurrentDate() );

        // insert item.
        long key = writeDatabase.insert(TABLE_NAME, null, insert_record);

        return key;
    }

    /**
     * Delete Record from AccountMasterTable.
     * @param key Delete Target id of AccountTable.
     * @return true if delete success.
     */
    public boolean delete(int key) {
        return true;
    }

    /**
     * Update Record.
     * @param record AccountTableRecord Instance.
     * @return true if update success.
     */
    public boolean update(AccountTableRecord record) {
        return true;
    }
}
