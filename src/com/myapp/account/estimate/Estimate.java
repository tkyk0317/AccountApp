package com.myapp.account.estimate;

import java.lang.RuntimeException;
import android.util.Log;
import android.text.InputType;
import android.text.InputFilter;
import android.app.AlertDialog;
import android.widget.EditText;
import android.app.Activity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.view.Gravity;
import android.graphics.Color;
import android.content.DialogInterface;
import com.myapp.account.R;
import com.myapp.account.database.DatabaseHelper;
import com.myapp.account.database.EstimateTableAccessor;
import com.myapp.account.database.EstimateTableRecord;
import com.myapp.account.database.AccountTableAccessor;
import com.myapp.account.config.AppConfigurationData;

/**
 * Estimate Class.
 */
public class Estimate {

    protected EstimateTableAccessor estimateTable;
    protected AccountTableAccessor accountTable;
    protected Activity activity;
    protected static final int ESTIMATE_MONEY_DIGITS = 9;
    protected static final int TABLE_FIRST_INDEX = 0;
    protected static final int TABLE_SECOND_INDEX = 1;
    protected static final int TEXT_FONT_SIZE = 15;

    /**
     * Constractor.
     * @param activity Activity Instance.
     */
    public Estimate(Activity activity) {
        this.activity = activity;
        estimateTable = new EstimateTableAccessor(new DatabaseHelper(activity.getApplicationContext()));
        accountTable = new AccountTableAccessor(new DatabaseHelper(activity.getApplicationContext()));
    }

    /**
     * Appear Estimate Money.
     */
    public void appear() {
        if( isEstimate() ) {
            if( isEnableEstimateMoney() ) {
                displayEstimateMessage();
            } else {
                displayAlertMessage();
            }
        }
    }

    /**
     * Is Estimate Enable.
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
     * Is Estimate Money Enable.
     * @return true if estimate money is enable.
     */
    protected boolean isEnableEstimateMoney() {
        EstimateTableRecord record = estimateTable.getRecordWithCurrentMonth();

        if( 0 == record.getEstimateMoney() ) {
            return false;
        }
        return true;
    }

    /**
     * Display Estimate Message.
     */
    protected void displayEstimateMessage() {
        TableRow table_row = new TableRow(activity);
        insertEstimateMoneyIntoTableRow(table_row);
        insertRestEstimateMoneyIntoTableRow(table_row);

        // Create Table.
        TableLayout summary_table = (TableLayout) activity.findViewById(R.id.summary_table);
        summary_table.addView(table_row);
    }

    /**
     * Insert Estimate Money.
     */
    protected void insertEstimateMoneyIntoTableRow(TableRow table_row) {
        TextView estimate_label = new TextView(activity);
        TextView estimate_value = new TextView(activity);

        EstimateTableRecord record = estimateTable.getRecordWithCurrentMonth();

        estimate_value.setText(String.format("%,d", record.getEstimateMoney()) + activity.getText(R.string.money_unit).toString());
        estimate_label.setText(activity.getText(R.string.estimate_label));
        estimate_label.setTextSize(TEXT_FONT_SIZE);
        estimate_value.setTextSize(TEXT_FONT_SIZE);
        estimate_label.setGravity(Gravity.RIGHT);
        estimate_value.setGravity(Gravity.RIGHT);

        table_row.addView(estimate_label);
        table_row.addView(estimate_value);
    }

    /**
     * Insert Rest Estimate Money.
     */
    protected void insertRestEstimateMoneyIntoTableRow(TableRow table_row) {
        TextView estimate_rest_label = new TextView(activity);
        TextView estimate_rest_value = new TextView(activity);

        EstimateTableRecord record = estimateTable.getRecordWithCurrentMonth();
        int total_payment = accountTable.getTotalPaymentAtCurrentMonth();
        int rest_money = record.getEstimateMoney() - total_payment;

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
     * Display Alert Estimate Message.
     */
    protected void displayAlertMessage() {
        final EditText edit_text = new EditText(activity);
        edit_text.setInputType(InputType.TYPE_CLASS_NUMBER);
        edit_text.setFilters(
                new InputFilter[] { new InputFilter.LengthFilter(ESTIMATE_MONEY_DIGITS) }
                );

        // setting dialog.
        AlertDialog.Builder message_dialog = new AlertDialog.Builder(activity);
        message_dialog.setTitle(activity.getText(R.string.estimate_title));
        message_dialog.setView(edit_text);
        message_dialog.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            int money = Integer.valueOf(edit_text.getText().toString());
                            insertEstimateMoney(money);
                            displayEstimateMessage();
                        } catch (RuntimeException error) {
                            displayAlertAgainEstimateInputMessage();
                        }
                    }
                }
                );
        message_dialog.show();
    }

    /**
     * Display Alert Message Again Estimate Input Display.
     */
    protected void displayAlertAgainEstimateInputMessage()
    {
        AlertDialog.Builder message_dialog = new AlertDialog.Builder(activity);
        message_dialog.setTitle(activity.getText(R.string.estimate_again_title));
        message_dialog.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int estimate_money) {
                        displayAlertMessage();
                    }
                }
                );
        message_dialog.show();
    }

    /**
     * Insert Estimate Money into Database.
     * @param estimate_money User inputed Money of Estimate.
     */
    protected void insertEstimateMoney(int estimate_money) throws RuntimeException {
        if( 0 >= estimate_money ) throw new RuntimeException();

        EstimateTableRecord record = new EstimateTableRecord();

        record.setEstimateMoney(estimate_money);
        estimateTable.insert(record);
    }
}

