package com.myapp.account.edit_account_master;

import android.widget.TableRow;
import android.widget.TextView;
import android.content.res.Resources;
import android.view.Gravity;
import android.content.Context;

import com.myapp.account.R;
import com.myapp.account.database.DatabaseHelper;
import com.myapp.account.database.AccountMasterTableRecord;

/**
 * @brief EditAccountMasterRecord Class.
 */
public class EditAccountMasterRecord extends TableRow {

    protected AccountMasterTableRecord accountMasterRecord;
    protected Context context;
    protected TextView categoryName;
    protected TextView accountKind;
    protected static final int TEXT_SIZE = 18;

    /**
     * @brief Constructor.
     * @param context Context Instance.
     */
    public EditAccountMasterRecord(Context context) {
        super(context);
        this.context = context;
        this.categoryName = new TextView(context);
        this.accountKind = new TextView(context);
        this.categoryName.setTextSize(TEXT_SIZE);
        this.accountKind.setTextSize(TEXT_SIZE);
        this.setClickable(true);
    }

    /**
     * @brief Set AccountMaster Record Information.
     * @param record AccountMasterTable Record.
     */
    public void setAccountMasterInfo(AccountMasterTableRecord record) {

        this.accountMasterRecord = record;
        this.categoryName.setText(record.getName());
        this.accountKind.setText(convertAccountKind(record.getKindId()));
        this.categoryName.setGravity(Gravity.RIGHT);
        this.accountKind.setGravity(Gravity.RIGHT);

        // add view.
        super.addView(this.categoryName);
        super.addView(this.accountKind);
    }

    /**
     * @brief Convert AccountKind String.
     * @param kind_id account kind id.
     * @return Account Kind String.
     */
    private String convertAccountKind(int kind_id) {
        Resources resources = this.context.getResources();
        if( DatabaseHelper.INCOME_FLAG == kind_id) {
            return resources.getString(R.string.income_label);
        }
        return resources.getString(R.string.payment_label);
    }

    // getter.
    public int getPrimaryId() { return this.accountMasterRecord.getId(); }
    public String getName() { return this.accountMasterRecord.getName(); }
    public int getKindId() { return this.accountMasterRecord.getKindId(); }
    public String getUseDate() { return this.accountMasterRecord.getUseDate(); }
    public String getUpdateDate() { return this.accountMasterRecord.getUpdateDate(); }
    public String getInsertDate() { return this.accountMasterRecord.getInsertDate(); }
}

