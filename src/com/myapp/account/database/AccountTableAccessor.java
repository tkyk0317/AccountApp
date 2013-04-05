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

    protected SQLiteOpenHelper helper = null;
    protected SQLiteDatabase readDatabase = null;
    protected SQLiteDatabase writeDatabase = null;
    protected static final String TABLE_NAME = "AccountTable";

    /**
     * @brief Consturactor.
     */
    public AccountTableAccessor(SQLiteOpenHelper helper) {
        this.helper = helper;
    }

    /**
     * @brief Finalize Process.
     */
    @Override
    protected void finalize() throws Throwable {
        try {
            super.finalize();
        } finally {
            close();
        }
    }

    /**
     * @brief close process.
     */
    public void close() {
        if( null != this.readDatabase ) this.readDatabase.close(); this.readDatabase = null;
        if( null != this.writeDatabase ) this.writeDatabase.close(); this.writeDatabase = null;
    }

    /**
     * @brief open Database.
     */
    private void open() {
        if( null == this.readDatabase ) this.readDatabase = this.helper.getReadableDatabase();
        if( null == this.writeDatabase ) this.writeDatabase = this.helper.getWritableDatabase();
    }

    /**
     * @brief Get Record Specified Key.
     * @param key Table key.
     * @return AccountTableRecord Instance.
     */
    public AccountTableRecord getRecord(int key) {
        open();

        Cursor cursor = readDatabase.query(TABLE_NAME, null , "_id=?", new String[] {String.valueOf(key)}, null, null, null);

        AccountTableRecord record = new AccountTableRecord();
        if( true == cursor.moveToFirst() ) {
            record.set(cursor);
        }
        cursor.close();
        close();
        return record;
    }

    /**
     * @brief Get Record with Target Date.
     * @param target_date String Target Date.
     * @return AccountTableRecord at Current Date.
     */
    public List<AccountTableRecord> getRecordWithTargetDate(String target_date)
    {
        open();
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
        close();
        return record_list;
    }

    /**
     * @brief Check Exsit Record at CategoryId.
     *
     * @param category_id Category Id.
     *
     * @return true:exsit false:not exsit.
     */
    public boolean isExsitRecordAtCategoryId(int category_id) {
        open();
        Cursor cursor = readDatabase.query(TABLE_NAME, null , "category_id=?", new String[] {String.valueOf(category_id)}, null, null, null);

        boolean is_exsit = false;
        if( true == cursor.moveToFirst() ) {
            if( 0 < cursor.getCount() ) {
                is_exsit = true;
            }
        }
        cursor.close();
        close();
        return is_exsit;
    }

    /**
     * @brief Check Exsit AccountRecord at Target Date.
     * @param target_date Specified target date.
     * @return true:exsit false:not exsit.
     */
    public boolean isExsitRecordAtTargetDate(String target_date)
    {
        open();
        Cursor cursor = readDatabase.query(TABLE_NAME, null, "insert_date=?" , new String[]{target_date}, null, null, "category_id", null);

        boolean is_exsit = false;
        if( true == cursor.moveToFirst() ) {
            if( 0 < cursor.getCount() ) {
                is_exsit = true;
            }
        }
        cursor.close();
        close();
        return is_exsit;
    }

    /**
     * @brief Check Exsit AccountRecord at Target Date.
     *
     * @param start_date start date.
     * @param end_date end date.
     *
     * @return true:exsit false:not exsit.
     */
    public boolean isExsitRecordBetweenStartDateAndEndDate(String start_date, String end_date)
    {
        open();
        Cursor cursor = readDatabase.query(TABLE_NAME, null, "insert_date>=? and insert_date<=?" , new String[]{start_date, end_date}, null, null, "category_id", null);

        boolean is_exsit = false;
        if( true == cursor.moveToFirst() ) {
            if( 0 < cursor.getCount() ) {
                is_exsit = true;
            }
        }
        cursor.close();
        close();
        return is_exsit;
    }

    /**
     * @brief Check Exsit Payment AccountRecord at Target Date.
     * @param target_date Specified target date.
     * @return true:exsit false:not exsit.
     */
    public boolean isExsitPaymentRecordAtTargetDate(String target_date) {
        open();
        Cursor cursor = readDatabase.rawQuery("select AccountTable.* from AccountTable " +
                "join AccountMaster on AccountTable.category_id=AccountMaster._id " +
                "where AccountMaster.kind_id=1 and AccountTable.insert_date=" + "'" + target_date + "';", null);

        boolean is_exsit = false;
        if( true == cursor.moveToFirst() ) {
            if( 0 < cursor.getCount() ) {
                is_exsit = true;
            }
        }
        cursor.close();
        close();
        return is_exsit;
    }

    /**
     * @brief Check Exsit Income AccountRecord at Target Date.
     * @param target_date Specified target date.
     * @return true:exsit false:not exsit.
     */
    public boolean isExsitIncomeRecordAtTargetDate(String target_date) {
        open();
        Cursor cursor = readDatabase.rawQuery("select AccountTable.* from AccountTable " +
                "join AccountMaster on AccountTable.category_id=AccountMaster._id " +
                "where AccountMaster.kind_id=0 and AccountTable.insert_date=" + "'" + target_date + "';", null);

        boolean is_exsit = false;
        if( true == cursor.moveToFirst() ) {
            if( 0 < cursor.getCount() ) {
                is_exsit = true;
            }
        }
        cursor.close();
        close();
        return is_exsit;
    }

    /**
     * @brief Check Exsit Record at Target Year.
     * @param target_date Specified Target Date.
     * @return true:exsit false:not exsit.
     */
    public boolean isExsitRecordAtTargetYear(String target_date) {
        open();
        String last_date_of_year = Utility.getLastDateOfTargetYear(target_date);
        String first_date_of_year = Utility.getFirstDateOfTargetYear(target_date);

        Cursor cursor = readDatabase.query(TABLE_NAME, null, "insert_date<=? and insert_date>=?" , new String[]{last_date_of_year, first_date_of_year}, "category_id, insert_date", null, "insert_date", null);

        boolean is_exsit = false;
        if( true == cursor.moveToFirst() ) {
            if( cursor.getCount() > 0 ) {
                is_exsit = true;
            }
        }
        cursor.close();
        close();
        return is_exsit;
    }

    /**
     * @brief Get Record With Target Date Group by CategoryId.
     * @param Target Insert Date.
     * @return AccountTableRecord List at Target Date.
     */
    public List<AccountTableRecord> getRecordWithTargetDateGroupByCategoryId(String target_date)
    {
        open();
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
        close();
        return record_list;
    }

    /**
     * @brief Get Record With Target Month Group by CategoryId.
     * @param Target Insert Date.
     * @return AccountTableRecord List at Target Month.
     */
    public List<AccountTableRecord> getRecordWithTargetMonthGroupByCategoryId(String start_date, String end_date) {
        open();
        Cursor cursor = readDatabase.query(TABLE_NAME, new String [] { "_id", "user_id", "category_id", "sum(money)", "memo", "update_date", "insert_date" },
                                           "insert_date<=? and insert_date>=?" , new String[]{end_date, start_date}, "category_id", null, "insert_date", null);
        cursor.moveToFirst();

        int record_count = cursor.getCount();
        List<AccountTableRecord> record_list = new ArrayList<AccountTableRecord>(record_count+1);

        for( int i = 0 ; i < record_count ; i++ ) {
            record_list.add( new AccountTableRecord() );
            record_list.get(i).set(cursor);
            cursor.moveToNext();
        }
        cursor.close();
        close();
        return record_list;
    }

    /**
     * @brief Get Record With Target Year Group by CategoryId.
     * @param Target Insert Date.
     * @return AccountTableRecord List at Target Year.
     */
    public List<AccountTableRecord> getRecordWithTargetYearGroupByCategoryId(String target_date) {
        open();
        String last_date_of_year = Utility.getLastDateOfTargetYear(target_date);
        String first_date_of_year = Utility.getFirstDateOfTargetYear(target_date);

        Cursor cursor = readDatabase.query(TABLE_NAME, new String [] { "_id", "user_id", "category_id", "sum(money)", "memo", "update_date", "insert_date" },
                                           "insert_date<=? and insert_date>=?" , new String[]{last_date_of_year, first_date_of_year}, "substr(insert_date,1,7), category_id", null, "category_id,insert_date", null);
        cursor.moveToFirst();

        int record_count = cursor.getCount();
        List<AccountTableRecord> record_list = new ArrayList<AccountTableRecord>(record_count+1);

        for( int i = 0 ; i < record_count ; i++ ) {
            record_list.add( new AccountTableRecord() );
            record_list.get(i).set(cursor);
            cursor.moveToNext();
        }
        cursor.close();
        close();
        return record_list;
    }

    /**
     * @brief Get All Record.
     * @return All AccountTableRecord in AccountMasterTable.
     */
    public List<AccountTableRecord> getAllRecord() {
        open();
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
        close();
        return record_list;
    }

    /**
     * @brief Get Income Total Money at Target Date.
     * @param start specify start date(yyyy/mm/dd).
     * @param end_date specify end date(yyyy/mm/dd).
     * @return Total Money.
     */
    public int getTotalIncomeAtTargetDate(String start_date, String end_date) {
        open();
        Cursor cursor = readDatabase.rawQuery("select sum(AccountTable.money) from AccountTable " +
                "join AccountMaster on AccountTable.category_id=AccountMaster._id " +
                "where AccountMaster.kind_id=0 and AccountTable.insert_date>=" + "'" + start_date + "'" +
                " and AccountTable.insert_date<=" + "'" + end_date + "'" + " ;", null);
        cursor.moveToFirst();

        int total = cursor.getInt(0);
        cursor.close();
        close();
        return total;
    }

    /**
     * @brief Get Payment Total Money at Target Date.
     * @param start_date specify start date(yyyy/mm/dd).
     * @param end_date specify end date(yyyy/mm/dd).
     * @return Total Money.
     */
    public int getTotalPaymentAtTargetDate(String start_date, String end_date) {
        open();
        Cursor cursor = readDatabase.rawQuery("select sum(AccountTable.money) from AccountTable " +
                "join AccountMaster on AccountTable.category_id=AccountMaster._id " +
                "where AccountMaster.kind_id=1 and AccountTable.insert_date>=" + "'" + start_date + "'" +
                " and AccountTable.insert_date<=" + "'" + end_date + "'" + " ;", null);
        cursor.moveToFirst();

        int total = cursor.getInt(0);
        cursor.close();
        close();
        return total;
    }

    /**
     * @brief Insert Record in AccountMasterTable.
     * @param record AccountTableRecord Instance.
     * @return Insert Record Key(_id).
     */
    public long insert(AccountTableRecord record) {
        open();
        ContentValues insert_record = new ContentValues();
        insert_record.put("user_id", record.getUserId());
        insert_record.put("category_id", record.getCategoryId());
        insert_record.put("money", record.getMoney());
        insert_record.put("memo", record.getMemo());
        insert_record.put("update_date", Utility.getCurrentDate() );
        insert_record.put("insert_date", record.getInsertDate() );

        // insert item.
        long key = writeDatabase.insert(TABLE_NAME, null, insert_record);
        close();
        return key;
    }

    /**
     * @brief Delete Record from AccountMasterTable.
     * @param key Delete Target id of AccountTable.
     * @return  deleteted key number.
     */
    public int delete(int key) {
        open();
        writeDatabase.delete(TABLE_NAME, "_id=" + String.valueOf(key), null);
        close();
        return key;
    }

    /**
     * @brief Update Record.
     * @param record AccountTableRecord Instance.
     * @return true if update success.
     */
    public boolean update(AccountTableRecord record) {
        open();
        ContentValues update_record = new ContentValues();
        update_record.put("user_id", record.getUserId());
        update_record.put("category_id", record.getCategoryId());
        update_record.put("money", record.getMoney());
        update_record.put("memo", record.getMemo());
        update_record.put("update_date", record.getUpdateDate() );
        update_record.put("insert_date", record.getInsertDate() );

        writeDatabase.update(TABLE_NAME, update_record, "_id=" + String.valueOf(record.getId()), null);
        close();
        return true;
    }
}
