package com.myapp.account.database;

import java.util.*;
import java.text.DateFormat;
import android.database.Cursor;

//========================================.
// AccountMasterTableRecordクラス.
public class AccountMasterTableRecord {

    private int id;
    private int kind_id;
    private String name;
    private DateFormat date;

    // constructor.
    public AccountMasterTableRecord() {}

    // Setter.
    public void set(Cursor cursor) {
        id = cursor.getInt(0);
        kind_id = cursor.getInt(1);
        name = cursor.getString(2);
    }

    // Getter.
    public int getId() { return id; }
    public int getKindId() { return kind_id; }
    public String getName() { return name; }
}
