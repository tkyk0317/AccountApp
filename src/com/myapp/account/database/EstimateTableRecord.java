package com.myapp.account.database;

import android.database.Cursor;

/**
 * Estimate Table Record Class.
 */
public class EstimateTableRecord {

    // Database Index Enum.
    private enum DatabaseIndex {
        ID(0), ESTIMATE_MONEY(1), TARGET_DATE(2), UPDATE_DATE(3), INSERT_DATE(4);

       private final int index;

       private DatabaseIndex(int index) { this.index = index; }
       public int getIndex() { return this.index; }
    }

    private int id;
    private int estimate_money;
    private String target_date;
    private String update_date;
    private String insert_date;

    // Setter.
    public void set(Cursor cursor) {
        id = cursor.getInt(DatabaseIndex.ID.getIndex());
        estimate_money = cursor.getInt(DatabaseIndex.ESTIMATE_MONEY.getIndex());
        target_date = cursor.getString(DatabaseIndex.TARGET_DATE.getIndex());
        update_date = cursor.getString(DatabaseIndex.UPDATE_DATE.getIndex());
        insert_date = cursor.getString(DatabaseIndex.INSERT_DATE.getIndex());
    }
    public void setEstimateMoney(int money) { estimate_money = money; }

    // Getter.
    public int getId() { return id; }
    public int getEstimateMoney() { return estimate_money; }
    public String getTargetDate() { return target_date; }
    public String getUpdateDate() { return update_date; }
    public String getInsertDate() { return insert_date; }
}

