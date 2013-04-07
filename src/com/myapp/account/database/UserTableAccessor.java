package com.myapp.account.database;

import java.util.*;
import java.text.*;

import android.util.Log;
import android.database.Cursor;
import android.content.ContentValues;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

import com.myapp.account.database.UserTableRecord;
import com.myapp.account.utility.Utility;

/**
 * @brief UserTableAccessor Class.
 */
public class UserTableAccessor {

    protected SQLiteDatabase readDatabase = null;
    protected SQLiteDatabase writeDatabase = null;
    protected SQLiteOpenHelper helper = null;
    protected static final String TABLE_NAME = "UserTable";

    /**
     * @brief Constractor.
     */
    public UserTableAccessor(SQLiteOpenHelper helper) {
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
        if( null == this.writeDatabase ) this.writeDatabase = this.helper.getWritableDatabase();
        if( null == this.readDatabase ) this.readDatabase = this.helper.getReadableDatabase();
    }

    /**
     * @brief Get Record Specified Key.
     * @param key Table key.
     * @return UserTableRecord Instance.
     */
    public UserTableRecord getRecord(int key) {
        open();
        Cursor cursor = readDatabase.query(TABLE_NAME, null , "_id=?", new String[] {String.valueOf(key)}, null, null, null);

        UserTableRecord record = new UserTableRecord();
        if( true == cursor.moveToFirst() ) {
            record.set(cursor);
        }
        cursor.close();
        close();
        return record;
    }

    /**
     * @brief Get All Record.
     * @return All UserTableRecord in UserTable.
     */
    public List<UserTableRecord> getAllRecord() {
        open();
        Cursor cursor = readDatabase.query(TABLE_NAME, null , null, null, null, null, null, null);
        cursor.moveToFirst();
        int record_count = cursor.getCount();
        List<UserTableRecord> record_list = new ArrayList<UserTableRecord>(record_count+1);

        for( int i = 0 ; i < record_count ; i++ ) {
            record_list.add( new UserTableRecord() );
            record_list.get(i).set(cursor);
            cursor.moveToNext();
        }
        cursor.close();
        close();
        return record_list;
    }

    /**
      * @brief Check Record Match the Name.
      * @param name UserTableRecord.name String.
      * @return true:exsit false:not exsit.
      */
    public boolean isExsitRecordMatchName(String name) {
        open();
        Cursor cursor = readDatabase.rawQuery("select * from " + TABLE_NAME + " where name = " + "'" + name + "'" + ";", null);

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
     * @brief Insert Record in UserTable.
     * @param record UserTableRecord Instance.
     * @return Insert Record Key(_id).
     */
    public long insert(UserTableRecord record) {
        open();
        ContentValues insert_record = new ContentValues();

        insert_record.put("name", record.getName());
        insert_record.put("memo", record.getMemo());
        insert_record.put("update_date", Utility.getCurrentDate());
        insert_record.put("insert_date", Utility.getCurrentDate());

        // insert record.
        long key = this.writeDatabase.insert(TABLE_NAME, null, insert_record);

        close();
        return key;
    }

    /**
     * @brief Update Record in UserTable.
     * @param record UserTable Instance.
     */
    public int update(UserTableRecord record) {
        open();
        ContentValues update_record = new ContentValues();
        update_record.put("name", record.getName());
        update_record.put("memo", record.getMemo());
        update_record.put("update_date", record.getUpdateDate());
        update_record.put("insert_date", record.getInsertDate());

        Log.d("UserTableAccessor", "UserTable Primary Id : " + record.getId());
        int key = this.writeDatabase.update(TABLE_NAME, update_record, "_id=" + String.valueOf(record.getId()), null);

        close();
        return key;
    }

    /*
     * @brief Delete Record.
     *
     * @param key Delete Target id of UserTable.
     *
     * @return  deleteted key number.
     */
    public int delete(int key) {
        open();
        writeDatabase.delete(TABLE_NAME, "_id=" + String.valueOf(key), null);
        close();
        return key;
    }

}

