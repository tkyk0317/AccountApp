package com.myapp.account.infoarea;

import java.util.*;
import android.widget.TableRow;
import android.content.Context;
import android.widget.TextView;
import android.view.Gravity;
import android.graphics.Color;
import com.myapp.account.database.AccountTableRecord;
import com.myapp.account.database.DatabaseHelper;

/**
 * @brief Daily Info Record Class.
 */
public class DailyInfoRecord extends TableRow {

    protected AccountTableRecord accountRecord;
    protected String accountDate;
    protected TextView categoryName;
    protected TextView accountMoney;
    protected TextView accountMemo;
    protected int kindId;
    protected static final int TEXT_SIZE = 15;
    protected static final int DISPLAY_MAX_LINE = 1;

    // getter.
    public AccountTableRecord getAccountRecord() { return accountRecord; }
    public String getAccountDate() { return accountDate; }
    public String getCategoryName() { return String.valueOf(categoryName.getText()); }
    public String getAccountMoney() { return String.valueOf(accountMoney.getText()); }
    public String getAccountMemo() { return String.valueOf(accountMemo.getText()); }

    /**
     * @brief Constractor.
     */
    public DailyInfoRecord(Context context) {
        super(context);
        this.categoryName = new TextView(context);
        this.accountMoney = new TextView(context);
        this.accountMemo = new TextView(context);
    }

    /**
     * @brief Set Account Table Record.
     * @param record AccountTable Record Instance.
     */
    public void setAccountTableRecord(AccountTableRecord record) {
        this.accountRecord = record;
    }

    /**
     * @brief Set Account Date.
     * @param String Account Date.
     */
    public void setAccountDate(String account_date) {
        this.accountDate = account_date;
    }

    /**
     * @brief Set Category Name.
     * @param String Category Name.
     */
    public void setCategoryName(String category_name) {
        this.categoryName.setText(category_name);
        this.categoryName.setTextSize(TEXT_SIZE);
        this.categoryName.setGravity(Gravity.RIGHT);
        super.addView(categoryName);
    }

    /**
     * @brief Set Kind ID.
     * @param kind_id Kind Id of category id.
     */
    public void setKindId(int kind_id) {
        this.kindId = kind_id;
        setTextColor();
    }

    /**
     * @brief Set Text Color correspond Kind Id.
     */
    protected void setTextColor() {
        if( this.kindId == DatabaseHelper.INCOME_FLAG ) {
            this.categoryName.setTextColor(Color.GREEN);
            this.accountMoney.setTextColor(Color.GREEN);
            this.accountMemo.setTextColor(Color.GREEN);
        } else {
            this.categoryName.setTextColor(Color.RED);
            this.accountMoney.setTextColor(Color.RED);
            this.accountMemo.setTextColor(Color.RED);
        }
    }

    /**
     * @brief Set Account Money.
     * @param String Account Money.
     */
    public void setAccountMoney(String account_money) {
        this.accountMoney.setText(account_money);
        this.accountMoney.setTextSize(TEXT_SIZE);
        this.accountMoney.setGravity(Gravity.RIGHT);
        super.addView(accountMoney);
    }

    /**
     * @brief Set Account Memo.
     * @param String Account Memo.
     */
    public void setAccountMemo(String account_memo) {
        this.accountMemo.setText(account_memo);
        this.accountMemo.setTextSize(TEXT_SIZE);
        this.accountMemo.setGravity(Gravity.RIGHT);
        this.accountMemo.setMaxLines(DISPLAY_MAX_LINE);
        super.addView(accountMemo);
    }
}

