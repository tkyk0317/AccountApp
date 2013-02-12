package com.myapp.account.config;

import java.util.*;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import com.myapp.account.R;
import com.myapp.account.config.AppConfigurationData;

/**
 * AppConfiguration Activity Class.
 */
public class AppConfigurationActivity extends PreferenceActivity {

    protected AppConfigurationData appConfiguration;
    protected final String ESTIMATE_KEY = "estimate_configuration";
    protected final String USER_TARGET_KEY = "target_user_configuration";

    /**
     * Called the Activity is First Created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
        addPreferencesFromResource(R.xml.configuration);
        displaySummary();
        registEvent();
    }

    /**
     * Initialize.
     */
    protected void init() {
        appConfiguration = new AppConfigurationData(this);
    }

    /**
      * Display Summary.
      */
    protected void displaySummary() {
        CheckBoxPreference estimate_config = (CheckBoxPreference)findPreference(ESTIMATE_KEY);
        EditTextPreference user_config = (EditTextPreference)findPreference(USER_TARGET_KEY);

        if( appConfiguration.getEstimate() ) {
            estimate_config.setSummary(getText(R.string.estimate_configuration_enable));
        } else {
            estimate_config.setSummary(getText(R.string.estimate_configuration_unenable));
        }
        user_config.setSummary(appConfiguration.getTargetUserName());
    }

    /**
      * Regist Event Listner.
      */
    protected void registEvent() {
        CheckBoxPreference estimate_pref = (CheckBoxPreference)findPreference(ESTIMATE_KEY);
        estimate_pref.setOnPreferenceChangeListener(
                new OnPreferenceChangeListener() {
                    public boolean onPreferenceChange(Preference pref, Object value) {
                        CheckBoxPreference estimate_config = (CheckBoxPreference)pref;
                        boolean is_estimate = ((Boolean)value).booleanValue();

                        // save estimate parameter.
                        appConfiguration.saveEstimate(is_estimate);
                        if( is_estimate ) {
                            estimate_config.setSummary(getText(R.string.estimate_configuration_enable));
                        } else {
                            estimate_config.setSummary(getText(R.string.estimate_configuration_unenable));
                        }

                       return true;
                    }
                });
        EditTextPreference user_pref = (EditTextPreference)findPreference(USER_TARGET_KEY);
        user_pref.setOnPreferenceChangeListener(
                new OnPreferenceChangeListener() {
                    public boolean onPreferenceChange(Preference pref, Object value) {
                        EditTextPreference user_config = (EditTextPreference)pref;
                        String user_name = value.toString();

                        // save target user name.
                        appConfiguration.saveUserName(user_name);

                        // display summary.
                        user_config.setSummary(user_name);
                        return true;
                    }
                });
    }
}

