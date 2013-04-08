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

    private Activity activity = null;
    private Estimate estimateInfo = null;
    private AccountTableAccessor accountTable = null;
    private String currentDate = null;
    private AppConfigurationData appConfig = null;
    private static final String ACCOUNT_DATA_BEFORE_STRING = "[";
    private static final String ACCOUNT_DATA_AFTER_STRING = "]";
    private static final String PERIOD_DELIMITER = "-";
    private static final String COLON_STRING = ":";
    private static final String SPACE = "　";
    private static final int TEXT_FONT_SIZE = 15;
    private static final int TEXT_FONT_HEIGHT = 20;

    /**
     * @brief Constractor.
     * @param activity Activity Instance.
     */
    public Summary(Activity activity) {
        this.activity = activity;
        this.appConfig = new AppConfigurationData(this.activity);
        this.estimateInfo = new Estimate(this.activity);
        this.accountTable = new AccountTableAccessor(new DatabaseHelper(this.activity.getApplicationContext()), this.appConfig);
     }

    /**
      * @brief Appear the Summary.
      * @param target_date specify displaied target date(yyyy/mm/dd).
      */
    public void appear(String target_date) {
        this.currentDate = target_date;

        // create account data infomation.
        createAccountDataInfo();

        // appear estimate area.
        this.estimateInfo.appear(this.currentDate);

        // create summary info.
        createSummaryInfo();
    }

    /**
     * @brief Crate Account Data Infomation.
     */
    private void createAccountDataInfo() {
        // initialize target layout.
        LinearLayout layout = (LinearLayout)this.activity.findViewById(R.id.summary_account_data_info);
        layout.removeAllViews();

        // create user and start/end date info.
        createUserInfo(layout);
        createStartAndEndDateInfo(layout);
    }

    /**
     * @brief Create UserInfo.
     *
     * @param layout Target LinearLayout Instance.
     */
    private void createUserInfo(LinearLayout layout) {
        String user_name_text = (ACCOUNT_DATA_BEFORE_STRING + this.appConfig.getTargetUserName() + COLON_STRING);

        // set text view.
        TextView user_name_view = new TextView(this.activity);
        user_name_view.setTextSize(TEXT_FONT_SIZE);
        user_name_view.setHeight(TEXT_FONT_HEIGHT);
        user_name_view.setText(user_name_text);

        // Create Table.
        layout.addView(user_name_view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                                                     LinearLayout.LayoutParams.WRAP_CONTENT));
    }

    /**
     * @brief Create Start and End Date of Month Infomation.
     *
     * @param layout Target LinearLayout Instance.
     */
    private void createStartAndEndDateInfo(LinearLayout layout) {
        String start_date = Utility.splitMonthAndDay(getStartDateOfMonth());
        String end_date = Utility.splitMonthAndDay(getEndDateOfMonth());
        String date_text = (start_date + PERIOD_DELIMITER + end_date + ACCOUNT_DATA_AFTER_STRING);

        // set text view.
        TextView date_text_view = new TextView(this.activity);
        date_text_view.setTextSize(TEXT_FONT_SIZE);
        date_text_view.setHeight(TEXT_FONT_HEIGHT);
        date_text_view.setText(date_text);

        // Create Table.
        layout.addView(date_text_view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
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
        return Utility.getStartDateOfMonth(this.currentDate, this.appConfig.getStartDay());
    }

    /**
     * @brief Get End Date of Month.
     *
     * @return end date.
     */
    private String getEndDateOfMonth() {
        return Utility.getEndDateOfMonth(this.currentDate, this.appConfig.getStartDay());
    }
}

