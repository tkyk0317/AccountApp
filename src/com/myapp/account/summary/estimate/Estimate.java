package com.myapp.account.summary.estimate;

import java.lang.RuntimeException;
import android.util.Log;
import android.widget.EditText;
import android.app.Activity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.view.Gravity;
import android.graphics.Color;
import android.text.InputType;
import android.text.InputFilter;
import android.app.AlertDialog;
import android.content.DialogInterface;
import com.myapp.account.R;
import com.myapp.account.utility.Utility;
import com.myapp.account.database.DatabaseHelper;
import com.myapp.account.database.EstimateTableAccessor;
import com.myapp.account.database.EstimateTableRecord;
import com.myapp.account.database.AccountTableAccessor;
import com.myapp.account.config.AppConfigurationData;

/**
 * @brief Estimate Class.
 */
public class Estimate {

    protected EstimateTableAccessor estimateTable;
    protected EstimateTableRecord estimateRecord;
    protected AccountTableAccessor accountTable;
    protected Activity activity;
    protected String currentDate;
    protected static boolean isAlertFlag = false;
    protected static final int ESTIMATE_MONEY_DIGITS = 9;
    protected static final int TABLE_FIRST_INDEX = 0;
    protected static final int TABLE_SECOND_INDEX = 1;
    protected static final int TEXT_FONT_SIZE = 15;

    /**
     * @brief Constractor.
     * @param activity Activity Instance.
     */
    public Estimate(Activity activity) {
        this.activity = activity;
        estimateTable = new EstimateTableAccessor(new DatabaseHelper(activity.getApplicationContext()));
        accountTable = new AccountTableAccessor(new DatabaseHelper(activity.getApplicationContext()));
    }

    /**
     * @brief Appear Estimate Money.
     * @param target_date Specify Estimate TargetDate(yyyy/mm/dd).
     */
    public void appear(String target_date) {
        if( isEstimate() ) {
            this.currentDate = target_date;
            if( true == isTargetDate(this.currentDate) &&
                false == isEnableEstimateMoney() ) {
                displayAlertMessage();
            }
            displayEstimate();
        }
    }

    /**
     * @brief Is Estimate Enable.
     * @return true if estimate function is enable.
     */
    protected boolean isEstimate() {
        AppConfigurationData app_config = new AppConfigurationData(activity);
        if( app_config.getEstimate() ) {
            return true;
        }
        return false;
    }

    /**
     * @brief Check Target Date For Estimate.
     * @param target_date Specify Checked Date.
     * @return true:target date false:not target date.
     */
    protected boolean isTargetDate(String target_date) {
        String target = Utility.splitYearAndMonth(target_date);
        if( 0 != target.compareTo(Utility.getCurrentYearAndMonth()) ) {
            return false;
        }
        return true;
    }

     /**
      * @brief Check Estimate Money Enable.
      * @return true if estimate money is enable.
      */
    protected boolean isEnableEstimateMoney() {
        EstimateTableRecord record = this.estimateTable.getRecordAtTargetDate(Utility.splitYearAndMonth(this.currentDate));
        if( 0 == record.getEstimateMoney() ) {
            return false;
        }
        return true;
    }

    /**
     * @brief Display Estimate.
     */
    protected void displayEstimate() {
        this.estimateRecord = this.estimateTable.getRecordAtTargetDate(Utility.splitYearAndMonth(this.currentDate));

        // clear all views.
        TableLayout summary_table = (TableLayout) activity.findViewById(R.id.summary_table);
        summary_table.removeAllViews();

        TableRow table_row = new TableRow(activity);
        insertEstimateMoneyIntoTableRow(table_row);
        insertRestEstimateMoneyIntoTableRow(table_row);

        // Create Table.
        summary_table.addView(table_row);
    }

     /**
     * Display Alert Estimate Message.
     */
    protected void displayAlertMessage() {
        if( true == Estimate.isAlertFlag ) return;

        // setting dialog.
        AlertDialog.Builder message_dialog = new AlertDialog.Builder(activity);
        message_dialog.setTitle(activity.getText(R.string.estimate_title));
        message_dialog.setPositiveButton("OK", null);
        message_dialog.show();

        // update flag.
        Estimate.isAlertFlag = true;
    }

    /**
     * @brief Insert Estimate Money.
     */
    protected void insertEstimateMoneyIntoTableRow(TableRow table_row) {
        TextView estimate_label = new TextView(activity);
        TextView estimate_value = new TextView(activity);

        estimate_value.setText(String.format("%,d", this.estimateRecord.getEstimateMoney()) + activity.getText(R.string.money_unit).toString());
        estimate_label.setText(activity.getText(R.string.estimate_label));
        estimate_label.setTextSize(TEXT_FONT_SIZE);
        estimate_value.setTextSize(TEXT_FONT_SIZE);
        estimate_label.setGravity(Gravity.RIGHT);
        estimate_value.setGravity(Gravity.RIGHT);

        table_row.addView(estimate_label);
        table_row.addView(estimate_value);
    }

    /**
     * @brief Insert Rest Estimate Money.
     */
    protected void insertRestEstimateMoneyIntoTableRow(TableRow table_row) {
        TextView estimate_rest_label = new TextView(activity);
        TextView estimate_rest_value = new TextView(activity);

        int total_payment = accountTable.getTotalPaymentAtTargetMonth(this.currentDate);
        int rest_money = this.estimateRecord.getEstimateMoney() - total_payment;

        if( 0 > rest_money ) {
            estimate_rest_label.setTextColor(Color.RED);
            estimate_rest_value.setTextColor(Color.RED);
        } else {
            estimate_rest_label.setTextColor(Color.GREEN);
            estimate_rest_value.setTextColor(Color.GREEN);
        }

        estimate_rest_value.setText(String.format("%,d", rest_money) + activity.getText(R.string.money_unit).toString());
        estimate_rest_label.setText(activity.getText(R.string.estimate_rest_money_label));
        estimate_rest_label.setTextSize(TEXT_FONT_SIZE);
        estimate_rest_value.setTextSize(TEXT_FONT_SIZE);
        estimate_rest_label.setGravity(Gravity.RIGHT);
        estimate_rest_value.setGravity(Gravity.RIGHT);

        table_row.addView(estimate_rest_label);
        table_row.addView(estimate_rest_value);
    }
}

