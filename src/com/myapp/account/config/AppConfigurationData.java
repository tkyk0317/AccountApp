package com.myapp.account.config;

import java.util.*;
import java.lang.RuntimeException;
import android.util.Log;
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

    private SharedPreferences appConfig;
    private boolean isEstimate;
    private String userName;
    private int estimateStartDay;
    private int estimateMoney;
    private EstimateTableAccessor estimateTable;
    private final String ESTIMATE_KEY = "estimate_configuration";
    private final String ESTIMATE_MONEY_KEY = "estimate_money_configuration";
    private final String ESTIMATE_START_DAY_KEY = "estimate_start_day_configuration";
    private final String USER_TARGET_KEY = "target_user_configuration";

    /**
     * @brief Constractor.
     */
    public AppConfigurationData(Context context) {
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
        this.estimateStartDay = Integer.valueOf(this.appConfig.getString(ESTIMATE_START_DAY_KEY, "1"));

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
            if( this.estimateTable.isEstimateRecord(Utility.getCurrentYearAndMonth()) ) {
                EstimateTableRecord record = this.estimateTable.getRecordWithCurrentMonth();
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
     * @brief Save Estimate Start Day.
     *
     * @param start_day estimate start day.
     */
    public void savaEstimateStartDay(String start_day) {
        Editor edit_config = this.appConfig.edit();
        edit_config.putString(ESTIMATE_START_DAY_KEY, start_day);
        edit_config.commit();
    }

    // Getter.
    public boolean getEstimate() { return this.isEstimate; }
    public String getTargetUserName() { return this.userName; }
    public int getEstimateMoney() { return this.estimateMoney; }
    public int getEstimateStartDay() { return this.estimateStartDay; }
    public String getEstimateKey() { return ESTIMATE_KEY; }
    public String getTargetUserKey() { return USER_TARGET_KEY; }
    public String getEstimateMoneyKey() { return ESTIMATE_MONEY_KEY; }
    public String getEstimateStartDayKey() { return ESTIMATE_START_DAY_KEY; }
}

