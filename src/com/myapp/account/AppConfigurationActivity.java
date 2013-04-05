package com.myapp.account;

import java.util.*;
import java.lang.NumberFormatException;
import java.lang.RuntimeException;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;

import com.myapp.account.R;
import com.myapp.account.utility.Utility;
import com.myapp.account.config.AppConfigurationData;

/**
 * @brief AppConfiguration Activity Class.
 */
public class AppConfigurationActivity extends PreferenceActivity {

    private AppConfigurationData appConfiguration;

    /**
     * @brief Called the Activity is First Created.
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
     * @brief Initialize.
     */
    private void init() {
        this.appConfiguration = new AppConfigurationData(this);
    }

    /**
      * @brief Display Summary.
      */
    private void displaySummary() {
        CheckBoxPreference estimate_config = (CheckBoxPreference)findPreference(this.appConfiguration.getEstimateKey());
        EditTextPreference user_config = (EditTextPreference)findPreference(this.appConfiguration.getTargetUserKey());
        ListPreference start_day_config = (ListPreference)findPreference(this.appConfiguration.getStartDayKey());

        if( this.appConfiguration.getEstimate() ) {
            estimate_config.setSummary(getText(R.string.estimate_configuration_enable));
        } else {
            estimate_config.setSummary(getText(R.string.estimate_configuration_unenable));
        }
        user_config.setSummary(this.appConfiguration.getTargetUserName());
        start_day_config.setSummary(String.valueOf(this.appConfiguration.getStartDay()) + getText(R.string.day_unit_string).toString());

        // estimate money summary
        setEstimateMoneySummary();
    }

    /**
     * @brief Set Summary for Estimate Money.
     */
    private void setEstimateMoneySummary() {
        EditTextPreference estimate_money_config = (EditTextPreference)findPreference(this.appConfiguration.getEstimateMoneyKey());

        String estimate_money_summary = getEstimateTargetDate();
        estimate_money_summary = Utility.splitMonth(estimate_money_summary) + getText(R.string.estimate_money_summary_suffix);
        estimate_money_summary = estimate_money_summary + String.format("%,d", this.appConfiguration.getEstimateMoney());
        estimate_money_config.setSummary(estimate_money_summary);
    }

    /**
      * @brief Regist Event Listner.
      */
    private void registEvent() {
        // Estimate Function Enable/UnEnable Event.
        CheckBoxPreference estimate_pref = (CheckBoxPreference)findPreference(this.appConfiguration.getEstimateKey());
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

        // UserName Changed Event.
        EditTextPreference user_pref = (EditTextPreference)findPreference(this.appConfiguration.getTargetUserKey());
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

        // Changed Estimate Money Event.
        EditTextPreference estimate_money_pref = (EditTextPreference)findPreference(this.appConfiguration.getEstimateMoneyKey());
        estimate_money_pref.setOnPreferenceChangeListener(
                new OnPreferenceChangeListener() {
                    public boolean onPreferenceChange(Preference pref, Object value) {
                        EditTextPreference estimate_money_config = (EditTextPreference)pref;
                        try {
                            int estimate_money = Integer.valueOf(value.toString());

                            // save estimate_money.
                            appConfiguration.saveEstimateMoney(estimate_money);
                            estimate_money_config.setSummary(String.format("%,d", estimate_money));
                        } catch (RuntimeException error) {
                            displayAlertEstimateMoney();
                        }
                        return true;
                    }
                });

        // Change Start Day Event.
        ListPreference start_day_pref = (ListPreference)findPreference(this.appConfiguration.getStartDayKey());
        start_day_pref.setOnPreferenceChangeListener(
            new OnPreferenceChangeListener() {
                public boolean onPreferenceChange(Preference pref, Object value) {
                    ListPreference start_day_pref = (ListPreference)pref;

                    // save start day.
                    appConfiguration.savaStartDay(value.toString());
                    start_day_pref.setSummary(value.toString() + getText(R.string.day_unit_string).toString());

                    // reflesh estimate money summary.
                    setEstimateMoneySummary();

                    return true;
                }
            });
     }

    /**
     * @brief Display Alert Estimate Money.
     */
    private void displayAlertEstimateMoney() {
        AlertDialog.Builder alert_dialog = new AlertDialog.Builder(this);
        alert_dialog.setTitle(getText(R.string.estimate_again_title));
        alert_dialog.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int estimate_money) {
                    }
                });
        alert_dialog.show();
    }

    /**
     * @brief Get Estimate TargetDate.
     *
     * @return target date.
     */
    private String getEstimateTargetDate() {
        String current_date = Utility.getCurrentDate();
        return Utility.getEstimateTargetDate(this, current_date, appConfiguration.getStartDay());
    }
}

