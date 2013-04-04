package com.myapp.account.config;

import java.util.*;
import java.lang.RuntimeException;

import android.util.Log;
import android.app.Activity;
import android.content.Context;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;
import android.content.Context;
import android.content.SharedPreferences.Editor;

import com.myapp.account.database.DatabaseHelper;
import com.myapp.account.database.EstimateTableAccessor;
import com.myapp.account.database.EstimateTableRecord;
import com.myapp.account.utility.Utility;

/**
 * @brief AppConfigurationData Class.
 */
public class AppConfigurationData {

    private Activity activity;
    private SharedPreferences appConfig;
    private boolean isEstimate;
    private String userName;
    private int startDayOfMonth;
    private int estimateMoney;
    private EstimateTableAccessor estimateTable;
    private final String ESTIMATE_KEY = "estimate_configuration";
    private final String ESTIMATE_MONEY_KEY = "estimate_money_configuration";
    private final String START_DAY_KEY = "start_day_configuration";
    private final String USER_TARGET_KEY = "target_user_configuration";

    /**
     * @brief Constractor.
     */
    public AppConfigurationData(Context context) {
        this.activity = (Activity)context;
        this.appConfig = PreferenceManager.getDefaultSharedPreferences(context);
        this.estimateTable = new EstimateTableAccessor(new DatabaseHelper(context.getApplicationContext()));
        readConfigurationData();
    }

    /**
     * @brief Read Configuration Data.
     */
    private void readConfigurationData() {
        // get configuration value.
        this.isEstimate = this.appConfig.getBoolean(ESTIMATE_KEY, false);
        this.userName = this.appConfig.getString(USER_TARGET_KEY, "default");
        this.startDayOfMonth = Integer.valueOf(this.appConfig.getString(START_DAY_KEY, "1"));

        EstimateTableRecord record = this.estimateTable.getRecordWithCurrentMonth();
        this.estimateMoney = record.getEstimateMoney();
    }

    /**
     * @brief Save Estimate is Enable Parameter.
     * @param is_estimate Estimate Function is enable/UnEnable.
     */
    public void saveEstimate(boolean is_estimate) {
        Editor edit_config = this.appConfig.edit();
        edit_config.putBoolean(ESTIMATE_KEY, is_estimate);
        edit_config.commit();
    }

    /**
     * @brief Save Target User Name.
     * @param user_name Target User Name for AccountApp.
     */
    public void saveUserName(String user_name) {
        Editor edit_config = this.appConfig.edit();
        edit_config.putString(USER_TARGET_KEY, user_name);
        edit_config.commit();
    }

    /**
     * @brief Save Estimate Money.
     *
     * @param estimate_money Estimate Money at Current Month.
     */
    public void saveEstimateMoney(int estimate_money) throws RuntimeException {
        try {
            String estimate_target_date = Utility.getEstimateTargetDate(this.activity, getCurrentDate());

            // Check Exsit Record.
            if( this.estimateTable.isEstimateRecord(estimate_target_date) ) {
                EstimateTableRecord record = this.estimateTable.getRecordAtTargetDate(estimate_target_date);
                record.setEstimateMoney(estimate_money);
                this.estimateTable.update(record);
            } else {
                EstimateTableRecord estimate_record = new EstimateTableRecord();
                estimate_record.setEstimateMoney(estimate_money);
                this.estimateTable.insert(estimate_record);
            }
        } catch (RuntimeException error) {
            throw new RuntimeException();
        }
    }

    /**
     * @brief Save Start Day.
     *
     * @param start_day start day of month.
     */
    public void savaStartDay(String start_day) {
        Editor edit_config = this.appConfig.edit();
        edit_config.putString(START_DAY_KEY, start_day);
        edit_config.commit();
    }

    /**
     * @brief Get Current Date.
     */
    private String getCurrentDate() {
        return Utility.getCurrentDate();
    }

    // Getter.
    public boolean getEstimate() { return this.isEstimate; }
    public String getTargetUserName() { return this.userName; }
    public int getEstimateMoney() { return this.estimateMoney; }
    public int getStartDay() { return this.startDayOfMonth; }
    public String getEstimateKey() { return ESTIMATE_KEY; }
    public String getTargetUserKey() { return USER_TARGET_KEY; }
    public String getEstimateMoneyKey() { return ESTIMATE_MONEY_KEY; }
    public String getStartDayKey() { return START_DAY_KEY; }
}

