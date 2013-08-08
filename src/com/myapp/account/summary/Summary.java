package com.myapp.account.summary;

import android.app.Activity;
import android.widget.TextView;
import android.widget.LinearLayout;

import com.myapp.account.R;
import com.myapp.account.factory.Factory;
import com.myapp.account.utility.Utility;
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
    private static final String COLON_STRING = " : ";
    private static final int TEXT_FONT_SIZE = 15;
    private static final int TEXT_FONT_HEIGHT = 20;

    /**
     * @brief Constractor.
     * @param activity Activity Instance.
     */
    public Summary(Activity activity) {
        this.activity = activity;
        this.appConfig = Factory.getAppConfigurationData(activity);
        this.estimateInfo = new Estimate(activity);
        this.accountTable = Factory.getAccountTableAcceessor(activity);
     }

    /**
     * @brief Clear View.
     */
    public void clearView() {
        this.estimateInfo.clearView();
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
        TextView user_name_view = new TextView(this.activity.getApplicationContext());
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
        TextView date_text_view = new TextView(this.activity.getApplicationContext());
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
        insertIncomeInfo();
        insertPaymentInfo();
    }

     /**
     * @brief Insert Income Info.
     */
    private void insertIncomeInfo() {
        TextView income_label = (TextView)this.activity.findViewById(R.id.total_income_label);
        TextView income_value = (TextView)this.activity.findViewById(R.id.total_income_value);

        int total = getIncomeTotalMoney();

        income_value.setText(String.format("%,d", total) + this.activity.getText(R.string.money_unit).toString());
        income_label.setText(this.activity.getText(R.string.income_label));
    }

    /**
     * @brief Insert Payment Info.
     */
    private void insertPaymentInfo() {
        TextView payment_label = (TextView)this.activity.findViewById(R.id.total_payment_label);
        TextView payment_value = (TextView)this.activity.findViewById(R.id.total_payment_value);

        int total = getPaymentTotalMoney();

        payment_value.setText(String.format("%,d", total) + this.activity.getText(R.string.money_unit).toString());
        payment_label.setText(this.activity.getText(R.string.payment_label));
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

