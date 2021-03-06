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
     * @brief Get All Record.
     * @return All AccountMasterTableRecord in AccountMasterTable.
     */
    public List<AccountMasterTableRecord> getAll() {
        Cursor cursor = readDatabase.query(TABLE_NAME, null , null, null, null, null, "use_date desc", null);

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
    public int insert(AccountMasterTableRecord record) {
        int key = 0;
        return key;
    }

    /**
     * @brief Delete Record from AccountMasterTable.
     * @param key Delete Target id of AccountMasterTable.
     * @return true if delete success.
     */
    public boolean delete(int key) {
        return true;
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

