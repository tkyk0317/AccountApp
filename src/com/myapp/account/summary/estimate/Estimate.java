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

    private EstimateTableAccessor estimateTable;
    private EstimateTableRecord estimateRecord;
    private AccountTableAccessor accountTable;
    private Activity activity;
    private String currentDate;
    private AppConfigurationData appConfigData;
    private static boolean isAlertFlag = false;
    private static final int ESTIMATE_MONEY_DIGITS = 9;
    private static final int TABLE_FIRST_INDEX = 0;
    private static final int TABLE_SECOND_INDEX = 1;
    private static final int TEXT_FONT_SIZE = 15;

    /**
     * @brief Constractor.
     * @param activity Activity Instance.
     */
    public Estimate(Activity activity) {
        this.activity = activity;
        this.estimateTable = new EstimateTableAccessor(new DatabaseHelper(activity.getApplicationContext()));
        this.accountTable = new AccountTableAccessor(new DatabaseHelper(activity.getApplicationContext()));
        this.appConfigData= new AppConfigurationData(this.activity);
    }

    /**
     * @brief Appear Estimate Money.
     * @param target_date Specify Estimate TargetDate(yyyy/mm/dd).
     */
    public void appear(String target_date) {
        if( isEstimate() ) {
            this.currentDate = target_date;
            if( true == isTargetDate(this.currentDate) &&
                false == isEnableEstimateMoney() &&
                true == equalsCurrentDateAndTargetDate() ) {
                displayAlertMessage();
            }
            displayEstimate();
        }
    }

    /**
     * @brief Is Estimate Enable.
     * @return true if estimate function is enable.
     */
    private boolean isEstimate() {
        if( this.appConfigData.getEstimate() ) {
            return true;
        }
        return false;
    }

    /**
     * @brief Check Target Date For Estimate.
     * @param target_date Specify Checked Date.
     * @return true:target date false:not target date.
     */
    private boolean isTargetDate(String target_date) {
        String start_date = getStartDateOfMonth(getCurrentDate());
        String end_date = getEndDateOfMonth(getCurrentDate());

        // same start_date or end_date.
        if( start_date.compareTo(target_date) == 0 || end_date.compareTo(target_date) == 0 ) {
            return true;
        }
        // include start_date ant end_date.
        if( start_date.compareTo(target_date) < 0 && end_date.compareTo(target_date) > 0 ) {
            return true;
        }
        return false;
    }

     /**
      * @brief Check Estimate Money Enable.
      * @return true if estimate money is enable.
      */
    private boolean isEnableEstimateMoney() {
        String estimate_target_date = getEstimateTargetDate(getCurrentDate());
        EstimateTableRecord record = this.estimateTable.getRecordAtTargetDate(Utility.splitYearAndMonth(estimate_target_date));
        if( 0 == record.getEstimateMoney() ) {
            return false;
        }
        return true;
    }

    /**
    * @brief Check equals current date and target date.
    *
    * @return true:equal false:not equal.
    */
    private boolean equalsCurrentDateAndTargetDate() {
        String current_year_month = Utility.splitYearAndMonth(getCurrentDate());
        String target_year_month = Utility.splitYearAndMonth(getEstimateTargetDate(this.currentDate));

        if( true == current_year_month.equals(target_year_month) ) {
            return true;
        }
        return false;
    }

    /**
     * @brief Display Estimate.
     */
    private void displayEstimate() {
        String estimate_target_date = getEstimateTargetDate(this.currentDate);
        this.estimateRecord = this.estimateTable.getRecordAtTargetDate(Utility.splitYearAndMonth(estimate_target_date));

        // clear all views.
        TableLayout summary_table = (TableLayout)activity.findViewById(R.id.summary_table);

        TableRow table_row = new TableRow(activity);
        insertEstimateMoneyIntoTableRow(table_row);
        insertRestEstimateMoneyIntoTableRow(table_row);

        // Create Table.
        summary_table.addView(table_row);
    }

    /**
     * Display Alert Estimate Message.
     */
    private void displayAlertMessage() {
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
    private void insertEstimateMoneyIntoTableRow(TableRow table_row) {
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
    private void insertRestEstimateMoneyIntoTableRow(TableRow table_row) {
        TextView estimate_rest_label = new TextView(activity);
        TextView estimate_rest_value = new TextView(activity);

        int total_payment = getPaymentTotalMoney();
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

    /**
     * @brief Get Total Payment Money.
     *
     * @return Payment Total Money.
     */
    private int getPaymentTotalMoney() {
        String start_date = getStartDateOfMonth(this.currentDate);
        String end_date = getEndDateOfMonth(this.currentDate);
        return this.accountTable.getTotalPaymentAtTargetDate(start_date, end_date);
    }

    /**
     * @brief Get Start Date Of Month.
     *
     * @param target_date target_date of start date.
     *
     * @return start date.
     */
    private String getStartDateOfMonth(String target_date) {
        return Utility.getStartDateOfMonth(target_date, this.appConfigData.getStartDay());
    }

    /**
     * @brief Get End Date Of Month.
     *
     * @param target_date target_date of end date.
     *
     * @return end date.
     */
    private String getEndDateOfMonth(String target_date) {
        return Utility.getEndDateOfMonth(target_date, this.appConfigData.getStartDay());
    }

    /**
     * @brief Get Estimate TargetDate.
     *
     * @param target_date estimate target date.
     *
     * @return target date.
     */
    private String getEstimateTargetDate(String target_date) {
        return Utility.getEstimateTargetDate(target_date, this.appConfigData.getStartDay());
    }

    /**
     * @brief Get Current Date.
     *
     * @return current date string.
     */
    private String getCurrentDate() {
        return Utility.getCurrentDate();
    }
}

