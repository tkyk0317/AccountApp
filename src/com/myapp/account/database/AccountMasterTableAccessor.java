package com.myapp.account.database;

import java.util.*;
import android.util.Log;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import com.myapp.account.database.AccountMasterTableRecord;
import com.myapp.account.utility.Utility;

/**
 * @brief AccountMasterTableAccessor.
 */
public class AccountMasterTableAccessor {

    protected SQLiteDatabase readDatabase = null;
    protected SQLiteDatabase writeDatabase = null;
    protected static final String TABLE_NAME = "AccountMaster";

    /**
      * @brief Consturactor.
      */
    public AccountMasterTableAccessor(SQLiteOpenHelper helper) {
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
      * @brief Get Record Specified Key.
      * @param key Table key.
      * @return AccountMasterTableRecord Instance.
      */
    public AccountMasterTableRecord getRecord(int key) {
        Cursor cursor = readDatabase.query(TABLE_NAME, null , "_id=?", new String[] {String.valueOf(key)}, null, null, null);

        AccountMasterTableRecord record = new AccountMasterTableRecord();
        if( true == cursor.moveToFirst() ) {
            record.set(cursor);
        }
        cursor.close();
        return record;
    }

    /**
      * @brief Get Record Match the Name.
      * @param name AccountMasterTableRecord.name String.
      * @return AccountMasterTableRecord Instance.
      */
    public AccountMasterTableRecord getRecordMatchName(String name) {
        Cursor cursor = readDatabase.rawQuery("select * from " + TABLE_NAME + " where name = " + "'" + name + "'" + ";", null);

        AccountMasterTableRecord record = new AccountMasterTableRecord();
        if( true == cursor.moveToFirst() ) {
            record.set(cursor);
        }
        cursor.close();
        return record;
    }

    /**
      * @brief Check Record Match the Name.
      * @param name AccountMasterTableRecord.name String.
      * @return true:exsit false:not exsit.
      */
    public boolean isExsitRecordMatchName(String name) {
        Cursor cursor = readDatabase.rawQuery("select * from " + TABLE_NAME + " where name = " + "'" + name + "'" + ";", null);

        AccountMasterTableRecord record = new AccountMasterTableRecord();
        cursor.moveToFirst();

        boolean is_exsit = false;
        if( 0 < cursor.getCount() ) {
            is_exsit = true;
        }
        cursor.close();
        return is_exsit;
    }
    /**
     * @brief Get All Record.
     * @return All AccountMasterTableRecord in AccountMasterTable.
     */
    public List<AccountMasterTableRecord> getAll() {
        Cursor cursor = readDatabase.query(TABLE_NAME, null , null, null, null, null, "use_date desc, _id", null);

        cursor.moveToFirst();
        int record_count = cursor.getCount();
        List<AccountMasterTableRecord> record_list = new ArrayList<AccountMasterTableRecord>(record_count+1);

        for( int i = 0 ; i < record_count ; i++ ) {
            record_list.add( new AccountMasterTableRecord() );
            record_list.get(i).set(cursor);
            cursor.moveToNext();
        }
        cursor.close();
        return record_list;
    }

    /**
     * @brief Insert Record in AccountMasterTable.
     * @param record AccountMasterTableRecord Instance.
     * @return Insert Record Key(_id).
     */
    public long insert(AccountMasterTableRecord record) {
        ContentValues insert_record = new ContentValues();
        insert_record.put("kind_id", record.getKindId());
        insert_record.put("name", record.getName());
        insert_record.put("use_date", Utility.getCurrentDateAndTime());
        insert_record.put("update_date", Utility.getCurrentDate());
        insert_record.put("insert_date", Utility.getCurrentDate());

        // insert item.
        long key = writeDatabase.insert(TABLE_NAME, null, insert_record);

        return key;
    }

    /**
     * @brief Delete Record from AccountMasterTable.
     * @param key Delete Target id of AccountMasterTable.
     * @return true if delete success.
     */
    public int delete(int key) {
        writeDatabase.delete(TABLE_NAME, "_id=" + String.valueOf(key), null);
        return key;
    }

    /**
     * @brief Update Record.
     * @param record AccountMasterTableRecord Instance.
     * @return true if update success.
     */
    public boolean update(AccountMasterTableRecord record) {
        ContentValues update_record = new ContentValues();
        update_record.put("kind_id", record.getKindId());
        update_record.put("name", record.getName());
        update_record.put("use_date", Utility.getCurrentDateAndTime());
        update_record.put("update_date", record.getUpdateDate());
        update_record.put("insert_date", record.getInsertDate());

        writeDatabase.update(TABLE_NAME, update_record, "_id=" + String.valueOf(record.getId()), null);
        return true;
    }
}

