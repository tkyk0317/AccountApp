package com.myapp.account.graph;

import java.util.List;

import android.app.Activity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.Gravity;

import com.myapp.account.R;
import com.myapp.account.graph.AbstractAccountGraph;
import com.myapp.account.database.DatabaseHelper;
import com.myapp.account.database.AccountMasterTableRecord;
import com.myapp.account.database.AccountTableAccessor;
import com.myapp.account.database.AccountTableRecord;

/**
 * @brief Account Payment Pie Graph Class.
 */
public class AccountPaymentPieGraphImpl extends AbstractAccountGraph {

    private static final String SUM_MONEY_TITLE_DELIMITER = ":";
    private static final String SUM_MONEY_TITLE_BEFORE_STRING = "[";
    private static final String SUM_MONEY_TITLE_AFTER_STRING = "]";

    /**
     * @brief Constractor.
     * @param activity Activity Instance.
     */
    public AccountPaymentPieGraphImpl(Activity activity, LinearLayout layout) {
        super(activity, layout);
    }

    /**
     * @brief Check Exsit Record at Target Date.
     * @return true:exsit false:not exsit.
     */
    protected boolean isExsitRecord() {
        return this.accountTable.isExsitRecordAtTargetMonth(this.targetDate);
    }

    /**
     * @brief Get Used Data In Graph.
     */
    protected List<AccountTableRecord> getUsedDataInGraph() {
        return this.accountTable.getRecordWithTargetMonthGroupByCategoryId(this.targetDate);
    }

    /**
     * @brief Check Target Record Data.
     * @param master_record master table record.
     * @return true:target false:not target.
     */
    protected boolean isTargetAccountRecord(AccountMasterTableRecord master_record) {
        if( DatabaseHelper.PAYMENT_FLAG == master_record.getKindId() ) return true;
        return false;
    }

    /**
     * @brief Display Sum money.
     */
    protected void displaySumMoney() {
        TextView sum_money_text = new TextView(this.activity);
        int sum_money = this.accountTable.getTotalPaymentAtTargetMonth(this.targetDate);

        // setting sum money.
        sum_money_text.setGravity(Gravity.CENTER);
        String display_text = SUM_MONEY_TITLE_BEFORE_STRING;
        display_text += (this.activity.getText(R.string.payment_sum_title).toString() + SUM_MONEY_TITLE_DELIMITER);
        display_text += (String.format("%,d", sum_money) + this.activity.getText(R.string.money_unit).toString());
        display_text += SUM_MONEY_TITLE_AFTER_STRING;
        sum_money_text.setText(display_text);

        this.chartArea.addView(sum_money_text);
    }
}


