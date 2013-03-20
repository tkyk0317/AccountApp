package com.myapp.account.edit_account_master;

import java.util.*;
import android.app.Activity;
import android.widget.TableRow;
import android.content.Context;
import android.widget.TextView;
import android.content.res.Resources;
import android.view.Gravity;
import com.myapp.account.R;
import com.myapp.account.database.DatabaseHelper;
import com.myapp.account.database.AccountMasterTableRecord;

/**
 * @brief EditAccountMasterRecord Class.
 */
public class EditAccountMasterRecord extends TableRow {

    protected AccountMasterTableRecord accountMasterRecord;
    protected Activity activity;
    protected TextView categoryName;
    protected TextView accountKind;
    protected static final int TEXT_SIZE = 18;

    /**
     * @brief Constractor.
     * @param context Context Instance.
     */
    public EditAccountMasterRecord(Activity activity) {
        super(activity);
        this.activity = activity;
        this.categoryName = new TextView(activity);
        this.accountKind = new TextView(activity);
        this.categoryName.setTextSize(TEXT_SIZE);
        this.accountKind.setTextSize(TEXT_SIZE);
        this.setClickable(true);
    }

    /**
     * @brief Set AccountMaster Record Infomation.
     * @param record AccountMasterTable Record.
     */
    public void setAccountMasterInfo(AccountMasterTableRecord record) {

        this.accountMasterRecord = record;
        this.categoryName.setText(record.getName());
        this.accountKind.setText(getAccountKind(record.getKindId()));
        this.categoryName.setGravity(Gravity.RIGHT);
        this.accountKind.setGravity(Gravity.RIGHT);

        // add view.
        super.addView(this.categoryName);
        super.addView(this.accountKind);
    }

    /**
     * @brief Get AccountKind String.
     * @param kind_id account kind id.
     * @return Account Kind String.
     */
    protected String getAccountKind(int kind_id) {
        Resources resources = this.activity.getResources();
        if( DatabaseHelper.INCOME_FLAG == kind_id) {
            return resources.getString(R.string.income_label);
        }
        return resources.getString(R.string.payment_label);
    }
}

