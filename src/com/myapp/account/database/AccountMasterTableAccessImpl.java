package com.myapp.account.database;

import java.util.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import com.myapp.account.database.AccountMasterTableRecord;
import android.util.Log;

/**
 * AccountMasterTableAccessImpl.
 */
public class AccountMasterTableAccessImpl {

    private SQLiteDatabase read_db;
    private SQLiteDatabase write_db;
    private SQLiteOpenHelper db_helper;
    private static final String TABLE_NAME = "AccountMaster";

    /**
      * Consturactor.
      */
    public AccountMasterTableAccessImpl(SQLiteOpenHelper helper) {
        db_helper = helper;
        read_db = db_helper.getReadableDatabase();
        write_db = db_helper.getWritableDatabase();
    }

    /**
      * Get Record Specified Key.
      * @param key Table key.
      * @return AccountMasterTableRecord Instance.
      */
    public AccountMasterTableRecord get(int key) {
        Cursor cursor = read_db.rawQuery("select * from " + TABLE_NAME + " where _id = " + key + ";", null);
        cursor.moveToFirst();

        AccountMasterTableRecord record = new AccountMasterTableRecord();
        record.set(cursor);

        return record;
    }

    /**
      * Get Record Match the Name.
      * @param name AccountMasterTableRecord.name String.
      * @return AccountMasterTableRecord Instance.
      */
    public AccountMasterTableRecord getRecordMatchName(String name) {
        Cursor cursor = read_db.rawQuery("select * from " + TABLE_NAME + " where name = " + "'" + name + "'" + ";", null);
        cursor.moveToFirst();

        AccountMasterTableRecord record = new AccountMasterTableRecord();
        record.set(cursor);

        return record;
    }

    /**
     * Get All Record.
     * @return All AccountMasterTableRecord in AccountMasterTable.
     */
    public List<AccountMasterTableRecord> getAll() {
        Cursor cursor = read_db.query(TABLE_NAME, null , null,
                null, null, null, null);
        cursor.moveToFirst();
        int record_count = cursor.getCount();
        List<AccountMasterTableRecord> record_list = new ArrayList<AccountMasterTableRecord>(record_count+1);

        for( int i = 0 ; i < record_count ; i++ ) {
            record_list.add( new AccountMasterTableRecord() );
            record_list.get(i).set(cursor);
            cursor.moveToNext();
        }
        return record_list;
    }

    /**
     * Insert Record in AccountMasterTable.
     * @param record AccountMasterTableRecord Instance.
     * @return Insert Record Key(_id).
     */
    public int insert(AccountMasterTableRecord record) {
        int key = 0;
        return key;
    }

    /**
     * Delete Record from AccountMasterTable.
     * @param key Delete Target id of AccountMasterTable.
     * @return true if delete success.
     */
    public boolean delete(int key) {
        return true;
    }

    /**
     * Update Record.
     * @param record AccountMasterTableRecord Instance.
     * @return true if update success.
     */
    public boolean update(AccountMasterTableRecord record) {
        return true;
    }
}
