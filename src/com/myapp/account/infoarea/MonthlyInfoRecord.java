package com.myapp.account.infoarea;

import java.util.*;
import android.widget.TableRow;
import android.content.Context;
import android.widget.TextView;
import android.view.Gravity;
import com.myapp.account.database.DatabaseHelper;

/**
 * @brief Monthly Info Record Class.
 */
public class MonthlyInfoRecord extends TableRow {

    protected TextView accountDate;
    protected TextView categoryName;
    protected TextView accountMoney;
    protected int kindId;
    protected static final int TEXT_SIZE = 15;

    /**
     * @brief Constractor.
     */
    public MonthlyInfoRecord(Context context) {
        super(context);
        this.accountDate = new TextView(context);
        this.categoryName = new TextView(context);
        this.accountMoney = new TextView(context);
    }

    /**
     * @brief Set Account Date.
     * @param String Account Date.
     */
    public void setAccountDate(String account_date) {
        this.accountDate.setText(account_date);
        this.accountDate.setTextSize(TEXT_SIZE);
        this.accountDate.setGravity(Gravity.RIGHT);
        super.addView(accountDate);
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
     * @brief Set TextColor.
     * @param kind_id category kind id.
     */
    public void setKindId(int kind_id) {
        this.kindId = kind_id;
    }
}

