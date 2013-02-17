package com.myapp.account.infoarea;

import java.util.*;
import android.widget.TableRow;
import android.content.Context;
import android.widget.TextView;
import android.view.Gravity;
import com.myapp.account.database.AccountTableRecord;

/**
 * Daily Info Record Class.
 */
public class DailyInfoRecord extends TableRow {

    protected AccountTableRecord accountRecord;
    protected String accountDate;
    protected TextView categoryName;
    protected TextView accountMoney;
    protected TextView accountMemo;
    protected static final int TEXT_SIZE = 15;
    protected static final int DISPLAY_MAX_LINE = 1;

    // getter.
    public AccountTableRecord getAccountRecord() { return accountRecord; }
    public String getAccountDate() { return accountDate; }
    public String getCategoryName() { return String.valueOf(categoryName.getText()); }
    public String getAccountMoney() { return String.valueOf(accountMoney.getText()); }
    public String getAccountMemo() { return String.valueOf(accountMemo.getText()); }

    /**
     * Constractor.
     */
    public DailyInfoRecord(Context context) {
        super(context);
        categoryName = new TextView(context);
        accountMoney = new TextView(context);
        accountMemo = new TextView(context);
    }

    /**
     * Set Account Table Record.
     * @param record AccountTable Record Instance.
     */
    protected void setAccountTableRecord(AccountTableRecord record) {
        this.accountRecord = record;
    }

    /**
     * Set Account Date.
     * @param String Account Date.
     */
    public void setAccountDate(String account_date) {
        accountDate = account_date;
    }

    /**
     * Set Category Name.
     * @param String Category Name.
     */
    public void setCategoryName(String category_name) {
        categoryName.setText(category_name);
        categoryName.setTextSize(TEXT_SIZE);
        categoryName.setGravity(Gravity.RIGHT);
        super.addView(categoryName);
    }

    /**
     * Set Account Money.
     * @param String Account Money.
     */
    public void setAccountMoney(String account_money) {
        accountMoney.setText(account_money);
        accountMoney.setTextSize(TEXT_SIZE);
        accountMoney.setGravity(Gravity.RIGHT);
        super.addView(accountMoney);
    }

    /**
     * Set Account Memo.
     * @param String Account Memo.
     */
    public void setAccountMemo(String account_memo) {
        accountMemo.setText(account_memo);
        accountMemo.setTextSize(TEXT_SIZE);
        accountMemo.setGravity(Gravity.RIGHT);
        accountMemo.setMaxLines(DISPLAY_MAX_LINE);
        super.addView(accountMemo);
    }
}

