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

    protected SQLiteDatabase readDatabase;
    protected SQLiteDatabase writeDatabase;
    protected static final String TABLE_NAME = "UserTable";

    /**
     * @brief Constractor.
     */
    public UserTableAccessor(SQLiteOpenHelper helper) {
        readDatabase = helper.getReadableDatabase();
        writeDatabase = helper.getWritableDatabase();
    }

    /**
     * @brief Get Record Specified Key.
     * @param key Table key.
     * @return UserTableRecord Instance.
     */
    public UserTableRecord getRecord(int key) {
        Cursor cursor = readDatabase.query(TABLE_NAME, null , "_id=?", new String[] {String.valueOf(key)}, null, null, null);

        UserTableRecord record = new UserTableRecord();
        if( true == cursor.moveToFirst() ) {
            record.set(cursor);
        }
        cursor.close();
        return record;
    }

    /**
     * @brief Get All Record.
     * @return All UserTableRecord in UserTable.
     */
    public List<UserTableRecord> getAllRecord() {
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
        return record_list;
    }

    /**
     * @brief Insert Record in UserTable.
     * @param record UserTableRecord Instance.
     * @return Insert Record Key(_id).
     */
    public long insert(UserTableRecord record) {
        ContentValues insert_record = new ContentValues();

        insert_record.put("name", record.getName() );
        insert_record.put("update_date", Utility.getCurrentDate() );
        insert_record.put("insert_date", Utility.getCurrentDate() );

        // insert record.
        return writeDatabase.insert(TABLE_NAME, null, insert_record);
    }

    /**
     * @brief Update Record in UserTable.
     * @param record UserTable Instance.
     */
    public int update(UserTableRecord record) {
        ContentValues update_record = new ContentValues();
        update_record.put("name", record.getName());
        update_record.put("update_date", record.getUpdateDate());
        update_record.put("insert_date", record.getInsertDate());

        return writeDatabase.update(TABLE_NAME, update_record, "_id=" + String.valueOf(record.getId()), null);
    }
}

