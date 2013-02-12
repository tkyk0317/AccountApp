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

/**
 * AppConfigurationData Class.
 */
public class AppConfigurationData {

    protected SharedPreferences appConfig;
    protected boolean isEstimate;
    protected String userName;
    protected int estimateMoney;
    protected EstimateTableAccessor estimateTable;
    protected final String ESTIMATE_KEY = "estimate_configuration";
    protected final String ESTIMATE_MONEY_KEY = "estimate_money_configuration";
    protected final String USER_TARGET_KEY = "target_user_configuration";

    /**
     * Constractor.
     */
    public AppConfigurationData(Context context) {
        appConfig = PreferenceManager.getDefaultSharedPreferences(context);
        estimateTable = new EstimateTableAccessor(new DatabaseHelper(context.getApplicationContext()));
        readConfigurationData();
    }

    /**
     * Read Configuration Data.
     */
    protected void readConfigurationData() {
        // get configuration value.
        isEstimate = appConfig.getBoolean(ESTIMATE_KEY, false);
        userName = appConfig.getString(USER_TARGET_KEY, "default");

        EstimateTableRecord record = estimateTable.getRecordWithCurrentMonth();
        estimateMoney = record.getEstimateMoney();
    }

    /**
     * Save Estimate is Enable Parameter.
     * @param is_estimate Estimate Function is enable/UnEnable.
     * @return true if success save parameter.
     */
    public boolean saveEstimate(boolean is_estimate) {
        Editor edit_config = appConfig.edit();
        edit_config.putBoolean(ESTIMATE_KEY, is_estimate);
        edit_config.commit();
        return true;
    }

    /**
     * Save Target User Name.
     * @param user_name Target User Name for AccountApp.
     * @return true if success save parameter.
     */
    public boolean saveUserName(String user_name) {
        Editor edit_config = appConfig.edit();
        edit_config.putString(USER_TARGET_KEY, user_name);
        edit_config.commit();
        return true;
    }

    /**
     * Save Estimate Money.
     * @param estimate_money Estimate Money at Current Month.
     */
    public void saveEstimateMoney(int estimate_money) throws RuntimeException {
        try {
            EstimateTableRecord record = estimateTable.getRecordWithCurrentMonth();

            record.setEstimateMoney(estimate_money);
            estimateTable.update(record);
        } catch (RuntimeException error) {
            throw new RuntimeException();
        }
    }

    // Getter.
    public boolean getEstimate() { return isEstimate; }
    public String getTargetUserName() { return userName; }
    public int getEstimateMoney() { return estimateMoney; }
    public String getEstimateKey() { return ESTIMATE_KEY; }
    public String getTargetUserKey() { return USER_TARGET_KEY; }
    public String getEstimateMoneyKey() { return ESTIMATE_MONEY_KEY; }
}

