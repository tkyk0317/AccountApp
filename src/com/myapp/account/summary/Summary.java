package com.myapp.account.summary;

import android.app.Activity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.view.Gravity;
import com.myapp.account.R;
import com.myapp.account.database.DatabaseHelper;
import com.myapp.account.database.AccountTableAccessor;

/**
 * Summary Class.
 */
public class Summary {

    protected Activity activity;
    protected AccountTableAccessor accountTable;
    protected static final int TEXT_FONT_SIZE = 15;

    /**
     * Constractor.
     * @param activity Activity Instance.
     */
    public Summary(Activity activity) {
        this.activity = activity;
        accountTable = new AccountTableAccessor(new DatabaseHelper(activity.getApplicationContext()));
     }

    /**
      * Appear the Summary.
      */
    public void appear() {
        TableRow table_row = new TableRow(activity.getApplicationContext());
        insertIncomeIntoTableRow(table_row);
        insertPaymentIntoTableRow(table_row);

        // Create Table.
        TableLayout summry_table = (TableLayout) activity.findViewById(R.id.summry_table);
        summry_table.addView(table_row);
    }

     /**
     * Insert Income TableRow.
     */
    protected void insertIncomeIntoTableRow(TableRow table_row) {
        TextView income_label = new TextView(activity.getApplicationContext());
        TextView income_value = new TextView(activity.getApplicationContext());

        int total = accountTable.getTotalIncomeAtCurrentMonth();

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
     * Insert Payment into TableRow.
     */
    protected void insertPaymentIntoTableRow(TableRow table_row) {
        TextView payment_label = new TextView(activity.getApplicationContext());
        TextView payment_value = new TextView(activity.getApplicationContext());

        int total = accountTable.getTotalPaymentAtCurrentMonth();

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

