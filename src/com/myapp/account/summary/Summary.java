package com.myapp.account.summary;

import android.app.Activity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.view.Gravity;
import com.myapp.account.R;
import com.myapp.account.database.DatabaseHelper;
import com.myapp.account.database.AccountTableAccessor;
import com.myapp.account.summary.estimate.Estimate;

/**
 * @brief Summary Class.
 */
public class Summary {

    protected Activity activity;
    protected Estimate estimateInfo;
    protected AccountTableAccessor accountTable;
    protected String currentDate;
    protected static final int TEXT_FONT_SIZE = 15;

    /**
     * @brief Constractor.
     * @param activity Activity Instance.
     */
    public Summary(Activity activity) {
        this.activity = activity;
        this.estimateInfo = new Estimate(this.activity);
        accountTable = new AccountTableAccessor(new DatabaseHelper(activity.getApplicationContext()));
     }

    /**
      * @brief Appear the Summary.
      * @param target_date specify displaied target date(yyyy/mm/dd).
      */
    public void appear(String target_date) {
        this.currentDate = target_date;

        // appear estimate area.
        this.estimateInfo.appear(this.currentDate);

        TableRow table_row = new TableRow(activity.getApplicationContext());
        insertIncomeIntoTableRow(table_row);
        insertPaymentIntoTableRow(table_row);

        // Create Table.
        TableLayout summary_table = (TableLayout) activity.findViewById(R.id.summary_table);
        summary_table.addView(table_row);
    }

     /**
     * @brief Insert Income TableRow.
     */
    protected void insertIncomeIntoTableRow(TableRow table_row) {
        TextView income_label = new TextView(activity.getApplicationContext());
        TextView income_value = new TextView(activity.getApplicationContext());

        int total = accountTable.getTotalIncomeAtTargetMonth(this.currentDate);

        income_value.setText(String.format("%,d", total) + activity.getText(R.string.money_unit).toString());
        income_label.setText(activity.getText(R.string.income_label));
        income_label.setTextSize(TEXT_FONT_SIZE);
        income_value.setTextSize(TEXT_FONT_SIZE);
        income_label.setGravity(Gravity.RIGHT);
        income_value.setGravity(Gravity.RIGHT);

        // insert table row.
        table_row.addView(income_label);
        table_row.addView(income_value);
    }

    /**
     * @brief Insert Payment into TableRow.
     */
    protected void insertPaymentIntoTableRow(TableRow table_row) {
        TextView payment_label = new TextView(activity.getApplicationContext());
        TextView payment_value = new TextView(activity.getApplicationContext());

        int total = accountTable.getTotalPaymentAtTargetMonth(this.currentDate);

        payment_value.setText(String.format("%,d", total) + activity.getText(R.string.money_unit).toString());
        payment_label.setText(activity.getText(R.string.payment_label));
        payment_label.setTextSize(TEXT_FONT_SIZE);
        payment_value.setTextSize(TEXT_FONT_SIZE);
        payment_label.setGravity(Gravity.RIGHT);
        payment_value.setGravity(Gravity.RIGHT);

        table_row.addView(payment_label);
        table_row.addView(payment_value);
    }
}

