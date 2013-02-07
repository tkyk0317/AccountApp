package com.myapp.account.database;

import java.util.*;
import java.text.DateFormat;
import android.database.Cursor;

//=====================================.
// AccountTableRecordクラス.
public class AccountTableRecord {

    private int id;
    private int category_id;
    private int money;
    private String memo;
    private String date;

    // Constractor.
    public AccountTableRecord() {}

    // Setter.
    public void set(Cursor cursor) {
        id = cursor.getInt(0);
        category_id = cursor.getInt(1);
        money = cursor.getInt(2);
        memo = cursor.getString(3);
        date = cursor.getString(4);
    }
    public void setCategoryId(int id) { category_id = id; }
    public void setMoney(int money) { this.money = money; }
    public void setMemo(String memo) { this.memo = memo; }

    // getter.
    public int getid() { return id; }
    public int getCategoryId() { return category_id; }
    public int getMoney() { return money; }
    public String getMemo() { return memo; }
    public String getDate() { return date; }
}
