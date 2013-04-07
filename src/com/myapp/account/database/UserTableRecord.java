package com.myapp.account.database;

import android.database.Cursor;

/**
 * User Table Record Class.
 */
public class UserTableRecord {

    // Database Index Enum.
    private enum DatabaseIndex {
        ID(0), NAME(1), UPDATE_DATE(2), INSERT_DATE(3), MEMO(4);

       private final int index;

       private DatabaseIndex(int index) { this.index = index; }
       public int getIndex() { return this.index; }
    }

    private int id;
    private String name;
    private String memo;
    private String update_date;
    private String insert_date;

    // Setter.
    public void set(Cursor cursor) {
        id = cursor.getInt(DatabaseIndex.ID.getIndex());
        name = cursor.getString(DatabaseIndex.NAME.getIndex());
        memo = cursor.getString(DatabaseIndex.MEMO.getIndex());
        update_date = cursor.getString(DatabaseIndex.UPDATE_DATE.getIndex());
        insert_date = cursor.getString(DatabaseIndex.INSERT_DATE.getIndex());
    }

    // Setter.
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setMemo(String memo) { this.memo = memo; }
    public void setUpdateDate(String date) { this.update_date = date; }
    public void setInsertDate(String date) { this.insert_date = date; }

    // Getter.
    public int getId() { return this.id; }
    public String getName() { return this.name; }
    public String getMemo() { return this.memo; }
    public String getUpdateDate() { return this.update_date; }
    public String getInsertDate() { return this.insert_date; }
}

