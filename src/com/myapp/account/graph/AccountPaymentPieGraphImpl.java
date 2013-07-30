package com.myapp.account.graph;

import java.util.List;

import android.app.Activity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.Gravity;

import com.myapp.account.R;
import com.myapp.account.utility.Utility;
import com.myapp.account.graph.AbstractAccountGraph;
import com.myapp.account.database.DatabaseHelper;
import com.myapp.account.database.AccountMasterTableRecord;
import com.myapp.account.database.AccountTableRecord;

/**
 * @brief Account Payment Pie Graph Class.
 */
public class AccountPaymentPieGraphImpl extends AbstractAccountGraph {

    private static final String SPACE = "　";
    private static final String PERIOD_DELIMITER = "-";
    private static final String SUMMARY_TITLE_DELIMITER = ":";
    private static final String SUMMARY_TITLE_BEFORE_STRING = "[";
    private static final String SUMMARY_TITLE_AFTER_STRING = "]";

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
        String start_date = getStartDateOfMonth();
        String end_date = getEndDateOfMonth();
        return this.accountTable.isExsitRecordBetweenStartDateAndEndDate(start_date, end_date);
    }

    /**
     * @brief Get Used Data In Graph.
     */
    protected List<AccountTableRecord> getUsedDataInGraph() {
        String start_date = getStartDateOfMonth();
        String end_date = getEndDateOfMonth();
        return this.accountTable.getRecordWithTargetMonthGroupByCategoryId(start_date, end_date);
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
        TextView summary_text = new TextView(this.activity);
        int sum_money = getTotalPaymentMoney();

        // setting period.
        String display_text = SUMMARY_TITLE_BEFORE_STRING;
        display_text += (this.activity.getText(R.string.payment_period).toString() + SUMMARY_TITLE_DELIMITER);
        display_text += Utility.splitMonthAndDay(getStartDateOfMonth());
        display_text += PERIOD_DELIMITER;
        display_text += Utility.splitMonthAndDay(getEndDateOfMonth());
        display_text += SPACE;

        // setting sum money.
        summary_text.setGravity(Gravity.CENTER);
        display_text += (this.activity.getText(R.string.payment_sum_title).toString() + SUMMARY_TITLE_DELIMITER);
        display_text += (String.format("%,d", sum_money) + this.activity.getText(R.string.money_unit).toString());
        display_text += SUMMARY_TITLE_AFTER_STRING;
        summary_text.setText(display_text);

        this.chartArea.addView(summary_text);
    }

    /**
     * @brief Get Total Payment Money.
     *
     * @return Payment Total Money.
     */
    private int getTotalPaymentMoney() {
        String start_date = getStartDateOfMonth();
        String end_date = getEndDateOfMonth();
        return this.accountTable.getTotalPaymentAtTargetDate(start_date, end_date);
    }
}


