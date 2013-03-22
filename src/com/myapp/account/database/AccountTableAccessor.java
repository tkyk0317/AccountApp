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
 * @brief AccountTableAccessor.
 */
public class AccountTableAccessor {

    protected SQLiteDatabase readDatabase;
    protected SQLiteDatabase writeDatabase;
    protected static final String TABLE_NAME = "AccountTable";

    /**
     * @brief Consturactor.
     */
    public AccountTableAccessor(SQLiteOpenHelper helper) {
        readDatabase = helper.getReadableDatabase();
        writeDatabase = helper.getWritableDatabase();
    }

    /**
     * @brief Get Record Specified Key.
     * @param key Table key.
     * @return AccountTableRecord Instance.
     */
    public AccountTableRecord getRecord(int key) {
        Cursor cursor = readDatabase.query(TABLE_NAME, null , "_id=?", new String[] {String.valueOf(key)}, null, null, null);

        AccountTableRecord record = new AccountTableRecord();
        if( true == cursor.moveToFirst() ) {
            record.set(cursor);
        }
        cursor.close();
        return record;
    }

    /**
     * @brief Get Record with Target Date.
     * @param target_date String Target Date.
     * @return AccountTableRecord at Current Date.
     */
    public List<AccountTableRecord> getRecordWithTargetDate(String target_date)
    {
        Cursor cursor = readDatabase.query(TABLE_NAME, null, "insert_date=?" , new String[]{target_date}, null, null, "category_id", null);

        cursor.moveToFirst();
        int record_count = cursor.getCount();
        List<AccountTableRecord> record_list = new ArrayList<AccountTableRecord>(record_count+1);

        for( int i = 0 ; i < record_count ; i++ ) {
            record_list.add( new AccountTableRecord() );
            record_list.get(i).set(cursor);
            cursor.moveToNext();
        }
        cursor.close();
        return record_list;
    }

    /**
     * @brief Check Exsit AccountRecord at Target Date.
     * @param target_date Specified target date.
     * @return true:exsit false:not exsit.
     */
    public boolean isExsitRecordAtTargetDate(String target_date) {
        Cursor cursor = readDatabase.query(TABLE_NAME, null, "insert_date=?" , new String[]{target_date}, null, null, "category_id", null);
        cursor.moveToFirst();

        boolean is_exsit = false;
        if( 0 < cursor.getCount() ) {
            is_exsit = true;
        }
        cursor.close();
        return is_exsit;
    }

    /**
     * @brief Get Record With Target Date Group by CategoryId.
     * @param Target Insert Date.
     * @return AccountTableRecord List at Target Date.
     */
    public List<AccountTableRecord> getRecordWithTargetDateGroupByCategoryId(String target_date)
    {
        Cursor cursor = readDatabase.query(TABLE_NAME, new String [] { "_id", "user_id", "category_id", "sum(money)", "memo", "update_date", "insert_date" },
                                           "insert_date=?" , new String[]{target_date}, "category_id", null, "category_id", null);

        cursor.moveToFirst();
        int record_count = cursor.getCount();
        List<AccountTableRecord> record_list = new ArrayList<AccountTableRecord>(record_count+1);

        for( int i = 0 ; i < record_count ; i++ ) {
            record_list.add( new AccountTableRecord() );
            record_list.get(i).set(cursor);
            cursor.moveToNext();
        }
        cursor.close();
        return record_list;
    }

    /**
     * @brief Get Record With Target Month Group by CategoryId.
     * @param Target Insert Date.
     * @return AccountTableRecord List at Target Month.
     */
    public List<AccountTableRecord> getRecordWithTargetMonthGroupByCategoryId(String target_date) {
        String last_date_of_month = Utility.getLastDateOfTargetMonth(target_date);
        String first_date_of_month = Utility.getFirstDateOfTargetMonth(target_date);

        Cursor cursor = readDatabase.query(TABLE_NAME, new String [] { "_id", "user_id", "category_id", "sum(money)", "memo", "update_date", "insert_date" },
                                           "insert_date<=? and insert_date>=?" , new String[]{last_date_of_month, first_date_of_month}, "category_id, insert_date", null, "insert_date", null);
        cursor.moveToFirst();

        int record_count = cursor.getCount();
        List<AccountTableRecord> record_list = new ArrayList<AccountTableRecord>(record_count+1);

        for( int i = 0 ; i < record_count ; i++ ) {
            record_list.add( new AccountTableRecord() );
            record_list.get(i).set(cursor);
            cursor.moveToNext();
        }
        cursor.close();
        return record_list;
    }

    /**
     * @brief Get All Record.
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
        cursor.close();
        return record_list;
    }

    /**
     * @brief Get Income Total Money at Target Month.
     * @param target_date specify target date(yyyy/mm/dd).
     * @return Total Money.
     */
    public int getTotalIncomeAtTargetMonth(String target_date) {
        String last_date_of_month = Utility.getLastDateOfTargetMonth(target_date);
        String first_date_of_month = Utility.getFirstDateOfTargetMonth(target_date);

        Cursor cursor = readDatabase.rawQuery("select sum(AccountTable.money) from AccountTable " +
                "join AccountMaster on AccountTable.category_id=AccountMaster._id " +
                "where AccountMaster.kind_id=0 and AccountTable.insert_date>=" + "'" + first_date_of_month + "'" +
                " and AccountTable.insert_date<=" + "'" + last_date_of_month + "'" + " ;", null);
        cursor.moveToFirst();

        int total = cursor.getInt(0);
        cursor.close();

        return total;
    }

    /**
     * @brief Get Payment Total Money at Target Month.
     * @param target_date specify target date(yyyy/mm/dd).
     * @return Total Money.
     */
    public int getTotalPaymentAtTargetMonth(String target_date) {
        String last_date_of_month = Utility.getLastDateOfTargetMonth(target_date);
        String first_date_of_month = Utility.getFirstDateOfTargetMonth(target_date);

        Cursor cursor = readDatabase.rawQuery("select sum(AccountTable.money) from AccountTable " +
                "join AccountMaster on AccountTable.category_id=AccountMaster._id " +
                "where AccountMaster.kind_id=1 and AccountTable.insert_date>=" + "'" + first_date_of_month + "'" +
                " and AccountTable.insert_date<=" + "'" + last_date_of_month + "'" + " ;", null);
        cursor.moveToFirst();

        int total = cursor.getInt(0);
        cursor.close();
        return total;
    }

    /**
     * @brief Insert Record in AccountMasterTable.
     * @param record AccountTableRecord Instance.
     * @return Insert Record Key(_id).
     */
    public long insert(AccountTableRecord record) {
        ContentValues insert_record = new ContentValues();
        insert_record.put("user_id", record.getUserId());
        insert_record.put("category_id", record.getCategoryId());
        insert_record.put("money", record.getMoney());
        insert_record.put("memo", record.getMemo());
        insert_record.put("update_date", Utility.getCurrentDate() );
        insert_record.put("insert_date", record.getInsertDate() );

        // insert item.
        long key = writeDatabase.insert(TABLE_NAME, null, insert_record);

        return key;
    }

    /**
     * @brief Delete Record from AccountMasterTable.
     * @param key Delete Target id of AccountTable.
     * @return  deleteted key number.
     */
    public int delete(int key) {
        writeDatabase.delete(TABLE_NAME, "_id=" + String.valueOf(key), null);
        return key;
    }

    /**
     * @brief Update Record.
     * @param record AccountTableRecord Instance.
     * @return true if update success.
     */
    public boolean update(AccountTableRecord record) {
        ContentValues update_record = new ContentValues();
        update_record.put("user_id", record.getUserId());
        update_record.put("category_id", record.getCategoryId());
        update_record.put("money", record.getMoney());
        update_record.put("memo", record.getMemo());
        update_record.put("update_date", record.getUpdateDate() );
        update_record.put("insert_date", record.getInsertDate() );

        writeDatabase.update(TABLE_NAME, update_record, "_id=" + String.valueOf(record.getId()), null);
        return true;
    }
}
