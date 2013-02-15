package com.myapp.account.infoarea;

import java.util.*;
import android.widget.TableRow;
import android.content.Context;
import android.widget.TextView;
import android.view.Gravity;

/**
 * Monthly Info Record Class.
 */
public class MonthlyInfoRecord extends TableRow {

    protected TextView accountDate;
    protected TextView categoryName;
    protected TextView accountMoney;
    protected static final int TEXT_SIZE = 15;

    /**
     * Constractor.
     */
    public MonthlyInfoRecord(Context context) {
        super(context);
        accountDate = new TextView(context);
        categoryName = new TextView(context);
        accountMoney = new TextView(context);
    }

    /**
     * Set Account Date.
     * @param String Account Date.
     */
    public void setAccountDate(String account_date) {
        accountDate.setText(account_date);
        accountDate.setTextSize(TEXT_SIZE);
        accountDate.setGravity(Gravity.RIGHT);
        super.addView(accountDate);
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
}

