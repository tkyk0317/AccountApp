package com.myapp.account.database;

import java.util.*;
import java.text.DateFormat;
import android.database.Cursor;

/**
 * Account Table Record Class.
 */
public class AccountTableRecord {

    // Database Index Enum.
    private enum DatabaseIndex {
        ID(0), USER_ID(1), CATEGORY_ID(2), MONEY(3), MEMO(4), UPDATE_DATE(5), INSERT_DATE(6);

       private final int index;

       private DatabaseIndex(int index) { this.index = index; }
       public int getIndex() { return this.index; }
    }

    private int id;
    private int user_id;
    private int category_id;
    private int money;
    private String memo;
    private String update_date;
    private String insert_date;

    // Setter.
    public void set(Cursor cursor) {
        id = cursor.getInt(DatabaseIndex.ID.getIndex());
        user_id = cursor.getInt(DatabaseIndex.USER_ID.getIndex());
        category_id = cursor.getInt(DatabaseIndex.CATEGORY_ID.getIndex());
        money = cursor.getInt(DatabaseIndex.MONEY.getIndex());
        memo = cursor.getString(DatabaseIndex.MEMO.getIndex());
        update_date = cursor.getString(DatabaseIndex.UPDATE_DATE.getIndex());
        insert_date = cursor.getString(DatabaseIndex.INSERT_DATE.getIndex());
    }
    public void setId(int id) { this.id = id; }
    public void setCategoryId(int id) { this.category_id = id; }
    public void setMoney(int money) { this.money = money; }
    public void setMemo(String memo) { this.memo = memo; }
    public void setUpdateDate(String date) { this.update_date = date; }
    public void setInsertDate(String date) { this.insert_date = date; }

    // getter.
    public int getId() { return id; }
    public int getUserId() { return user_id; }
    public int getCategoryId() { return category_id; }
    public int getMoney() { return money; }
    public String getMemo() { return memo; }
    public String getUpdateDate() { return update_date; }
    public String getInsertDate() { return insert_date; }
}
