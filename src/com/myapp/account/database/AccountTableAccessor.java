package com.myapp.account.database;

import java.util.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;

import com.myapp.account.utility.Utility;
import com.myapp.account.database.AccountTableRecord;
import com.myapp.account.config.AppConfigurationData;

/**
 * @brief AccountTableAccessor.
 */
public class AccountTableAccessor {

    protected SQLiteOpenHelper helper = null;
    protected SQLiteDatabase readDatabase = null;
    protected SQLiteDatabase writeDatabase = null;
    protected AppConfigurationData appConfig = null;
    protected static final String TABLE_NAME = "AccountTable";
    protected static final String STRING_CAMMA = ",";

    /**
     * @brief Constructor.
     */
    public AccountTableAccessor(SQLiteOpenHelper helper, AppConfigurationData app_config) {
        this.helper = helper;
        this.appConfig = app_config;
        open();
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
     * @brief Start Transaction.
     */
    public void startTransaction() {
        this.writeDatabase.beginTransaction();
    }

    /**
     * @brief Set Transaction when Successful.
     */
    public void setTransactionSuccessful() {
        this.writeDatabase.setTransactionSuccessful();
    }

    /**
     * @brief End Transaction.
     */
    public void endTransaction() {
        this.writeDatabase.endTransaction();
    }

    /**
     * @brief Get Record Specified Key.
     * @param key Table key.
     * @return AccountTableRecord Instance.
     */
    public AccountTableRecord getRecord(int key) {
        Cursor cursor = readDatabase.query(TABLE_NAME, null , "_id=? and user_id=?",
                                           new String[] {String.valueOf(key), String.valueOf(this.appConfig.getTargetUserNameId())},
                                           null, null, null);
        AccountTableRecord record = new AccountTableRecord();
        if( true == cursor.moveToFirst() ) {
            record.set(cursor);
        }
        cursor.close();
        return record;
    }

    /**
     * @brief Get Record Specified Count and Offset.
     *
     * @param count Getting Count.
     * @param offset Start Offset.
     *
     * @return AccountTableRecord List.
     */
    public List<AccountTableRecord> getRecord(int count, int offset) {
        String limit = new String();
        limit = String.valueOf(offset) + STRING_CAMMA + String.valueOf(count);
        Cursor cursor = readDatabase.query(TABLE_NAME, null, null, null, null, null, "_id", limit );

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
     * @brief Get Record Count.
     *
     * @return All Record Count.
     */
    public int getRecordCount() {
        Cursor cursor = readDatabase.query(TABLE_NAME, null , null, null, null, null, null, null);
        cursor.moveToFirst();
        int record_count = cursor.getCount();
        cursor.close();
        return record_count;
    }

    /**
     * @brief Get Record with Target Date.
     * @param target_date String Target Date.
     * @return AccountTableRecord at Current Date.
     */
    public List<AccountTableRecord> getRecordWithTargetDate(String target_date)
    {
        Cursor cursor = readDatabase.query(TABLE_NAME, null, "insert_date=? and user_id=?",
                                           new String[]{target_date, String.valueOf(this.appConfig.getTargetUserNameId())},
                                           null, null, "category_id", null);

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
     * @brief Check Exist Record at CategoryId.
     *
     * @param category_id Category Id.
     *
     * @return true:exsit false:not exist.
     */
    public boolean isExsitRecordAtCategoryId(int category_id) {
        Cursor cursor = readDatabase.query(TABLE_NAME, null , "category_id=? and user_id=?",
                                           new String[] {String.valueOf(category_id),String.valueOf(this.appConfig.getTargetUserNameId())},
                                           null, null, null);

        boolean is_exsit = false;
        if( true == cursor.moveToFirst() ) {
            if( 0 < cursor.getCount() ) {
                is_exsit = true;
            }
        }
        cursor.close();
        return is_exsit;
    }

    /**
     * @brief Check Exsit AccountRecord at Target Date.
     * @param target_date Specified target date.
     * @return true:exsit false:not exsit.
     */
    public boolean isExsitRecordAtTargetDate(String target_date)
    {
        Cursor cursor = readDatabase.query(TABLE_NAME, null, "insert_date=? and user_id=?",
                                           new String[]{target_date, String.valueOf(this.appConfig.getTargetUserNameId())},
                                           null, null, "category_id", null);

        boolean is_exsit = false;
        if( true == cursor.moveToFirst() ) {
            if( 0 < cursor.getCount() ) {
                is_exsit = true;
            }
        }
        cursor.close();
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
        Cursor cursor = readDatabase.query(TABLE_NAME, null, "insert_date>=? and insert_date<=? and user_id=?",
                                           new String[]{start_date, end_date, String.valueOf(this.appConfig.getTargetUserNameId())},
                                           null, null, "category_id", null);

        boolean is_exsit = false;
        if( true == cursor.moveToFirst() ) {
            if( 0 < cursor.getCount() ) {
                is_exsit = true;
            }
        }
        cursor.close();
        return is_exsit;
    }

    /**
     * @brief Check Exsit Payment AccountRecord at Target Date.
     * @param target_date Specified target date.
     * @return true:exsit false:not exsit.
     */
    public boolean isExsitPaymentRecordAtTargetDate(String target_date) {
        Cursor cursor = readDatabase.rawQuery("select AccountTable.* from AccountTable " +
                "join AccountMaster on AccountTable.category_id=AccountMaster._id " +
                "where AccountMaster.kind_id=1 and AccountTable.insert_date=" + "'" + target_date + "'" +
                " and user_id=" + String.valueOf(this.appConfig.getTargetUserNameId()) + ";", null);

        boolean is_exsit = false;
        if( true == cursor.moveToFirst() ) {
            if( 0 < cursor.getCount() ) {
                is_exsit = true;
            }
        }
        cursor.close();
        return is_exsit;
    }

    /**
     * @brief Check Exsit Income AccountRecord at Target Date.
     * @param target_date Specified target date.
     * @return true:exsit false:not exsit.
     */
    public boolean isExsitIncomeRecordAtTargetDate(String target_date) {
        Cursor cursor = readDatabase.rawQuery("select AccountTable.* from AccountTable " +
                "join AccountMaster on AccountTable.category_id=AccountMaster._id " +
                "where AccountMaster.kind_id=0 and AccountTable.insert_date=" + "'" + target_date + "'" +
                " and user_id=" + String.valueOf(this.appConfig.getTargetUserNameId()) + ";", null);

        boolean is_exsit = false;
        if( true == cursor.moveToFirst() ) {
            if( 0 < cursor.getCount() ) {
                is_exsit = true;
            }
        }
        cursor.close();
        return is_exsit;
    }

    /**
     * @brief Check Exist Record at Target Year.
     * @param target_date Specified Target Date.
     * @return true:exsit false:not exist.
     */
    public boolean isExsitRecordAtTargetYear(String target_date) {
        String last_date_of_year = Utility.getLastDateOfTargetYear(target_date);
        String first_date_of_year = Utility.getFirstDateOfTargetYear(target_date);

        Cursor cursor = readDatabase.query(TABLE_NAME, null, "insert_date<=? and insert_date>=? and user_id=?" ,
                                           new String[]{last_date_of_year, first_date_of_year, String.valueOf(this.appConfig.getTargetUserNameId())},
                                           "category_id, insert_date", null, "insert_date", null);

        boolean is_exsit = false;
        if( true == cursor.moveToFirst() ) {
            if( cursor.getCount() > 0 ) {
                is_exsit = true;
            }
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
                                           "insert_date=? and user_id=?", new String[]{target_date, String.valueOf(this.appConfig.getTargetUserNameId())},
                                           "category_id", null, "category_id", null);

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
    public List<AccountTableRecord> getRecordWithTargetMonthGroupByCategoryId(String start_date, String end_date) {
        Cursor cursor = readDatabase.query(TABLE_NAME, new String [] { "_id", "user_id", "category_id", "sum(money)", "memo", "update_date", "insert_date" },
                                           "insert_date<=? and insert_date>=? and user_id=?",
                                           new String[]{end_date, start_date, String.valueOf(this.appConfig.getTargetUserNameId())},
                                           "category_id", null, "insert_date", null);
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
     * @brief Get Record With Target Year Group by CategoryId.
     * @param Target Insert Date.
     * @return AccountTableRecord List at Target Year.
     */
    public List<AccountTableRecord> getRecordWithTargetYearGroupByCategoryId(String target_date) {
        String last_date_of_year = Utility.getLastDateOfTargetYear(target_date);
        String first_date_of_year = Utility.getFirstDateOfTargetYear(target_date);

        Cursor cursor = readDatabase.query(TABLE_NAME, new String [] { "_id", "user_id", "category_id", "sum(money)", "memo", "update_date", "insert_date" },
                                           "insert_date<=? and insert_date>=? and user_id=?",
                                           new String[]{last_date_of_year, first_date_of_year, String.valueOf(this.appConfig.getTargetUserNameId())}, "substr(insert_date,1,7), category_id", null, "category_id,insert_date", null);
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
        Cursor cursor = readDatabase.query(TABLE_NAME, null , "user_id=?", new String[]{String.valueOf(this.appConfig.getTargetUserNameId())},
                                           null, null, null, null);
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
     * @brief Get All Record(not specified user_id).
     * @return All AccountTableRecord in AccountMasterTable.
     */
    public List<AccountTableRecord> getAllRecordNotSpecifiedUserId() {
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
     * @brief Get All Record(specified user_id).
     * @return All AccountTableRecord in AccountMasterTable.
     */
    public List<AccountTableRecord> getAllRecordSpecifiedUserId(int user_id) {
        Cursor cursor = readDatabase.query(TABLE_NAME, null , "user_id=?", new String[]{String.valueOf(user_id)},
                                           null, null, null, null);
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
     * @brief Get Income Total Money at Target Date.
     * @param start specify start date(yyyy/mm/dd).
     * @param end_date specify end date(yyyy/mm/dd).
     * @return Total Money.
     */
    public int getTotalIncomeAtTargetDate(String start_date, String end_date) {
        Cursor cursor = readDatabase.rawQuery("select sum(AccountTable.money) from AccountTable " +
                "join AccountMaster on AccountTable.category_id=AccountMaster._id " +
                "where AccountMaster.kind_id=0 and AccountTable.insert_date>=" + "'" + start_date + "'" +
                " and AccountTable.insert_date<=" + "'" + end_date + "'" + " and user_id=" + String.valueOf(this.appConfig.getTargetUserNameId()) + " ;", null);
        cursor.moveToFirst();

        int total = cursor.getInt(0);
        cursor.close();
        return total;
    }

    /**
     * @brief Get Payment Total Money at Target Date.
     * @param start_date specify start date(yyyy/mm/dd).
     * @param end_date specify end date(yyyy/mm/dd).
     * @return Total Money.
     */
    public int getTotalPaymentAtTargetDate(String start_date, String end_date) {
        Cursor cursor = readDatabase.rawQuery("select sum(AccountTable.money) from AccountTable " +
                "join AccountMaster on AccountTable.category_id=AccountMaster._id " +
                "where AccountMaster.kind_id=1 and AccountTable.insert_date>=" + "'" + start_date + "'" +
                " and AccountTable.insert_date<=" + "'" + end_date + "'" + " and user_id=" + String.valueOf(this.appConfig.getTargetUserNameId()) + " ;", null);
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
        insert_record.put("user_id", this.appConfig.getTargetUserNameId());
        insert_record.put("category_id", record.getCategoryId());
        insert_record.put("money", record.getMoney());
        insert_record.put("memo", record.getMemo());
        insert_record.put("update_date", Utility.getCurrentDate() );
        insert_record.put("insert_date", record.getInsertDate() );

        // insert item.
        long key = writeDatabase.insert(TABLE_NAME, null, insert_record);
        insert_record = null;
        return key;
    }

    /**
     * @brief Delete Record from AccountMasterTable.
     * @param key Delete Target id of AccountTable.
     * @return  deleted key number.
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
        update_record.put("user_id", this.appConfig.getTargetUserNameId());
        update_record.put("category_id", record.getCategoryId());
        update_record.put("money", record.getMoney());
        update_record.put("memo", record.getMemo());
        update_record.put("update_date", record.getUpdateDate() );
        update_record.put("insert_date", record.getInsertDate() );

        writeDatabase.update(TABLE_NAME, update_record, "_id=" + String.valueOf(record.getId()), null);
        update_record = null;
        return true;
    }
}
