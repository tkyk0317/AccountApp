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

    private SQLiteDatabase read_db;
    private SQLiteDatabase write_db;
    private SQLiteOpenHelper db_helper;
    private static final String TABLE_NAME = "AccountTable";

    /**
     * Consturactor.
     */
    public AccountTableAccessImpl(SQLiteOpenHelper helper) {
        db_helper = helper;
        read_db = db_helper.getReadableDatabase();
        write_db = db_helper.getWritableDatabase();
    }

    /**
     * Get Record Specified Key.
     * @param key Table key.
     * @return AccountTableRecord Instance.
     */
    public AccountTableRecord getRecord(int key) {
        Cursor cursor = read_db.rawQuery("select * from " + TABLE_NAME + " where _id = " + key + ";", null);
        cursor.moveToFirst();

        AccountTableRecord record = new AccountTableRecord();
        record.set(cursor);

        return record;
    }

    /**
     * Get Record with Current Date.
     * @return AccountTableRecord at Current Date.
     */
    public List<AccountTableRecord> getRecordWithCurrentDate()
    {
        String current_date = Utility.getCurrentDate();
        Cursor cursor = read_db.query(TABLE_NAME, null, "update_time=?" , new String[]{current_date}, null, null, null, null);

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
        Cursor cursor = read_db.query(TABLE_NAME, null , null, null, null, null, null, null);
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
    public long getTotalIncodemAtCurrentMonth() {
        String last_date_of_month = Utility.getLastDateOfCurrentMonth();
        String first_date_of_month = Utility.getFirstDateOfCurrentMonth();

        Cursor cursor = read_db.rawQuery("select sum(AccountTable.money) from AccountTable " +
                "join AccountMaster on AccountTable.category_id=AccountMaster._id " +
                "where AccountMaster.kind_id=0 and AccountTable.update_time>=" + "'" + first_date_of_month + "'" +
                " and AccountTable.update_time<=" + "'" + last_date_of_month + "'" + " ;", null);
        cursor.moveToFirst();

        return cursor.getLong(0);
    }

    /**
      * Get Payment Total Money at Current Month.
      * @return Total Money.
      */
    public long getTotalPaymentAtCurrentMonth() {
        String last_date_of_month = Utility.getLastDateOfCurrentMonth();
        String first_date_of_month = Utility.getFirstDateOfCurrentMonth();

        Cursor cursor = read_db.rawQuery("select sum(AccountTable.money) from AccountTable " +
                "join AccountMaster on AccountTable.category_id=AccountMaster._id " +
                "where AccountMaster.kind_id=1 and AccountTable.update_time>=" + "'" + first_date_of_month + "'" +
                " and AccountTable.update_time<=" + "'" + last_date_of_month + "'" + " ;", null);
        cursor.moveToFirst();

        return cursor.getLong(0);
    }

    /**
     * Insert Record in AccountMasterTable.
     * @param record AccountTableRecord Instance.
     * @return Insert Record Key(_id).
     */
    public long insert(AccountTableRecord record) {
        ContentValues content = new ContentValues();
        content.put("category_id", record.getCategoryId());
        content.put("money", record.getMoney());
        content.put("memo", record.getMemo());
        //content.put("update_time", (new SimpleDateFormat(DATE_FORMAT)).format(new Date()) );
        content.put("update_time", Utility.getCurrentDate() );

        // insert item.
        Log.d("AccountTableAccessImple", "START");
        long key = write_db.insert(TABLE_NAME, null, content);

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
