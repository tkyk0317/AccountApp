package com.myapp.account.summary;

import android.util.Log;
import android.app.Activity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.myapp.account.R;
import com.myapp.account.utility.Utility;
import com.myapp.account.database.DatabaseHelper;
import com.myapp.account.database.AccountTableAccessor;
import com.myapp.account.summary.estimate.Estimate;
import com.myapp.account.config.AppConfigurationData;

/**
 * @brief Summary Class.
 */
public class Summary {

    private Activity activity;
    private Estimate estimateInfo;
    private AccountTableAccessor accountTable;
    private String currentDate;
    private static final String PERIOD_BEFORE_STRING = "[";
    private static final String PERIOD_AFTER_STRING = "]";
    private static final String PERIOD_DELIMITER = "-";
    private static final String PERIOD_COLON = ":";
    private static final int TEXT_FONT_SIZE = 15;

    /**
     * @brief Constractor.
     * @param activity Activity Instance.
     */
    public Summary(Activity activity) {
        this.activity = activity;
        this.estimateInfo = new Estimate(this.activity);
        this.accountTable = new AccountTableAccessor(new DatabaseHelper(this.activity.getApplicationContext()));
     }

    /**
      * @brief Appear the Summary.
      * @param target_date specify displaied target date(yyyy/mm/dd).
      */
    public void appear(String target_date) {
        this.currentDate = target_date;

        // create start and date info.
        createStartAndEndDateInfo();

        // appear estimate area.
        this.estimateInfo.appear(this.currentDate);

        // create summary info.
        createSummaryInfo();
    }

    /**
     * @brief Create Start and End Date of Month Infomation.
     */
    private void createStartAndEndDateInfo() {
        String start_date = Utility.splitMonthAndDay(getStartDateOfMonth());
        String end_date = Utility.splitMonthAndDay(getEndDateOfMonth());
        String info_text = PERIOD_BEFORE_STRING;
        info_text += (this.activity.getText(R.string.payment_period).toString() + PERIOD_COLON);
        info_text += (start_date + PERIOD_DELIMITER + end_date);
        info_text += PERIOD_AFTER_STRING;

        // set text view.
        TextView info_text_view = new TextView(this.activity);
        info_text_view.setTextSize(TEXT_FONT_SIZE);
        info_text_view.setGravity(Gravity.CENTER);
        info_text_view.setText(info_text);

        // Create Table.
        LinearLayout layout = (LinearLayout)this.activity.findViewById(R.id.summary_period_info_area);
        layout.removeAllViews();
        layout.addView(info_text_view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                                                     LinearLayout.LayoutParams.WRAP_CONTENT));
    }

    /**
     * @brief Create Summary Infomation.
     */
    private void createSummaryInfo() {
        TableRow table_row = new TableRow(this.activity.getApplicationContext());
        insertIncomeIntoTableRow(table_row);
        insertPaymentIntoTableRow(table_row);

        // Create Table.
        TableLayout summary_table = (TableLayout)this.activity.findViewById(R.id.summary_table);
        summary_table.addView(table_row);
    }

     /**
     * @brief Insert Income TableRow.
     */
    private void insertIncomeIntoTableRow(TableRow table_row) {
        TextView income_label = new TextView(this.activity.getApplicationContext());
        TextView income_value = new TextView(this.activity.getApplicationContext());

        int total = getIncomeTotalMoney();

        income_value.setText(String.format("%,d", total) + this.activity.getText(R.string.money_unit).toString());
        income_label.setText(this.activity.getText(R.string.income_label));
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
    private void insertPaymentIntoTableRow(TableRow table_row) {
        TextView payment_label = new TextView(this.activity.getApplicationContext());
        TextView payment_value = new TextView(this.activity.getApplicationContext());

        int total = getPaymentTotalMoney();

        payment_value.setText(String.format("%,d", total) + this.activity.getText(R.string.money_unit).toString());
        payment_label.setText(this.activity.getText(R.string.payment_label));
        payment_label.setTextSize(TEXT_FONT_SIZE);
        payment_value.setTextSize(TEXT_FONT_SIZE);
        payment_label.setGravity(Gravity.RIGHT);
        payment_value.setGravity(Gravity.RIGHT);

        table_row.addView(payment_label);
        table_row.addView(payment_value);
    }

    /**
     * @brief Get Total Income Money.
     *
     * @return Income Total Money.
     */
    private int getIncomeTotalMoney() {
        String start_date = getStartDateOfMonth();
        String end_date = getEndDateOfMonth();
        return this.accountTable.getTotalIncomeAtTargetDate(start_date, end_date);
    }
    /**
     * @brief Get Total Payment Money.
     *
     * @return Payment Total Money.
     */
    private int getPaymentTotalMoney() {
        String start_date = getStartDateOfMonth();
        String end_date = getEndDateOfMonth();
        return this.accountTable.getTotalPaymentAtTargetDate(start_date, end_date);
    }

    /**
     * @brief Get Start Date of Month.
     *
     * @return start_date.
     */
    private String getStartDateOfMonth() {
        AppConfigurationData app_config = new AppConfigurationData(this.activity);
        return Utility.getStartDateOfMonth(this.currentDate, app_config.getStartDay());
    }

    /**
     * @brief Get End Date of Month.
     *
     * @return end date.
     */
    private String getEndDateOfMonth() {
        AppConfigurationData app_config = new AppConfigurationData(this.activity);
        return Utility.getEndDateOfMonth(this.currentDate, app_config.getStartDay());
    }
}

