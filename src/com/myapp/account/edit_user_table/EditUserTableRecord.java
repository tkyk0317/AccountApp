package com.myapp.account.edit_user_table;

import android.widget.TableRow;
import android.widget.TextView;
import android.content.Context;

import com.myapp.account.database.UserTableRecord;

/**
 * @brief EditUserTableRecord Class.
 */
public class EditUserTableRecord extends TableRow {

    protected UserTableRecord userTableRecord = null;
    protected TextView userName = null;
    protected TextView userMemo = null;
    protected static final int TEXT_SIZE = 18;

    /**
     * @brief Constructor.
     * @param context Context Instance.
     */
    public EditUserTableRecord(Context context) {
        super(context);
        this.userName = new TextView(context.getApplicationContext());
        this.userMemo = new TextView(context.getApplicationContext());
        this.userName.setTextSize(TEXT_SIZE);
        this.userMemo.setTextSize(TEXT_SIZE);
        this.setClickable(true);
    }

    /**
     * @brief Set UserTable Record Information.
     * @param record AccountMasterTable Record.
     */
    public void setUserTableRecord(UserTableRecord record) {
        this.userTableRecord = record;
        this.userName.setText(record.getName());
        this.userMemo.setText(record.getMemo());

        // add view.
        super.addView(this.userName);
        super.addView(this.userMemo);
    }

    // getter.
    public int getPrimaryId() { return this.userTableRecord.getId(); }
    public String getName() { return this.userTableRecord.getName(); }
    public String getMemo() { return this.userTableRecord.getMemo(); }
    public String getUpdateDate() { return this.userTableRecord.getUpdateDate(); }
    public String getInsertDate() { return this.userTableRecord.getInsertDate(); }
}

