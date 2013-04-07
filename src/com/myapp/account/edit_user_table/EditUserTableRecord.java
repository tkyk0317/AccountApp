package com.myapp.account.edit_user_table;

import java.util.*;
import android.app.Activity;
import android.widget.TableRow;
import android.content.Context;
import android.widget.TextView;
import android.content.res.Resources;
import android.view.Gravity;

import com.myapp.account.R;
import com.myapp.account.database.DatabaseHelper;
import com.myapp.account.database.UserTableRecord;

/**
 * @brief EditUserTableRecord Class.
 */
public class EditUserTableRecord extends TableRow {

    protected Activity activity = null;
    protected UserTableRecord userTableRecord = null;
    protected TextView userName = null;
    protected static final int TEXT_SIZE = 18;

    /**
     * @brief Constractor.
     * @param context Context Instance.
     */
    public EditUserTableRecord(Activity activity) {
        super(activity);
        this.activity = activity;
        this.userName = new TextView(this.activity);
        this.userName.setTextSize(TEXT_SIZE);
        this.setClickable(true);
    }

    /**
     * @brief Set UserTable Record Infomation.
     * @param record AccountMasterTable Record.
     */
    public void setUserTableRecord(UserTableRecord record) {
        this.userTableRecord = record;
        this.userName.setText(record.getName());

        // add view.
        super.addView(this.userName);
    }

    // getter.
    public int getPrimaryId() { return this.userTableRecord.getId(); }
    public String getName() { return this.userTableRecord.getName(); }
    public String getUpdateDate() { return this.userTableRecord.getUpdateDate(); }
    public String getInsertDate() { return this.userTableRecord.getInsertDate(); }
}

