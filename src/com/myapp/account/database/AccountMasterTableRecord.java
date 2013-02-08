package com.myapp.account.database;

import java.util.*;
import java.text.DateFormat;
import android.database.Cursor;

/**
 * Account Master Table Record Class.
 */
public class AccountMasterTableRecord {

    // Database Index Enum.
    private enum DatabaseIndex {
        ID(0), KIND_ID(1), NAME(2), UPDATE_DATE(3), INSERT_DATE(4);

        private final int index;

        private DatabaseIndex(int index) { this.index = index; }
        public int getIndex() { return this.index; }
    }

    private int id;
    private int kind_id;
    private String name;
    private String update_date;
    private String insert_date;

    // Setter.
    public void set(Cursor cursor) {
        id = cursor.getInt(DatabaseIndex.ID.getIndex());
        kind_id = cursor.getInt(DatabaseIndex.KIND_ID.getIndex());
        name = cursor.getString(DatabaseIndex.NAME.getIndex());
        update_date = cursor.getString(DatabaseIndex.UPDATE_DATE.getIndex());
        insert_date = cursor.getString(DatabaseIndex.INSERT_DATE.getIndex());
    }

    // Getter.
    public int getId() { return id; }
    public int getKindId() { return kind_id; }
    public String getName() { return name; }
    public String getUpdateDate() { return update_date; }
    public String getInsertDate() { return insert_date; }
}
