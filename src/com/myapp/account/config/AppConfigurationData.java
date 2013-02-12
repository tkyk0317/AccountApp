package com.myapp.account.config;

import java.util.*;
import android.content.Context;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;
import android.content.Context;
import android.content.SharedPreferences.Editor;

/**
 * AppConfigurationData Class.
 */
public class AppConfigurationData {

    protected SharedPreferences appConfig;
    protected boolean isEstimate;
    protected String userName;
    protected final String ESTIMATE_KEY = "estimate_configuration";
    protected final String USER_TARGET_KEY = "target_user_configuration";

    /**
     * Constractor.
     */
    public AppConfigurationData(Context context) {
        appConfig = PreferenceManager.getDefaultSharedPreferences(context);
        readConfigurationData();
    }

    /**
     * Read Configuration Data.
     */
    protected void readConfigurationData() {
        // get configuration value.
        isEstimate = appConfig.getBoolean(ESTIMATE_KEY, false);
        userName = appConfig.getString(USER_TARGET_KEY, "default");
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

    // Getter.
    public boolean getEstimate() { return isEstimate; }
    public String getTargetUserName() { return userName; }
}

