package com.myapp.account;

import java.util.*;
import java.lang.CharSequence;
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
import com.myapp.account.database.DatabaseHelper;
import com.myapp.account.database.UserTableAccessor;
import com.myapp.account.database.UserTableRecord;
import com.myapp.account.utility.Utility;
import com.myapp.account.config.AppConfigurationData;

/**
 * @brief AppConfiguration Activity Class.
 */
public class AppConfigurationActivity extends PreferenceActivity {

    private AppConfigurationData appConfig = null;
    private UserTableAccessor userTable = null;

    /**
     * @brief Called the Activity is First Created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
        addPreferencesFromResource(R.xml.configuration);
        createUserNameListPreference();
        displaySummary();
        registEvent();
    }

    /**
     * @brief Initialize.
     */
    private void init() {
        this.appConfig = new AppConfigurationData(this);
        this.userTable = new UserTableAccessor(new DatabaseHelper(this.getApplicationContext()));
    }

    /**
     * @brief Create Preference for UserName.
     */
    private void createUserNameListPreference() {
        ListPreference user_name_list_preference = (ListPreference)findPreference(this.appConfig.getTargetUserKey());
        List<UserTableRecord> user_records = this.userTable.getAllRecord();

        // add entry values.
        ArrayList<CharSequence> entriesList = new ArrayList<CharSequence>();
        ArrayList<CharSequence> entryValuesList = new ArrayList<CharSequence>();

        for( UserTableRecord record : user_records ) {
            String user_name = record.getName();
            String user_name_value = String.valueOf(record.getId());
            entriesList.add(user_name.subSequence(0, user_name.length()));
            entryValuesList.add(user_name_value.subSequence(0, user_name_value.length()));
        }

        // set entry.
        CharSequence[] entries = entriesList.toArray(new CharSequence[] {});
        CharSequence[] entryValues = entryValuesList.toArray(new CharSequence[] {});

        user_name_list_preference.setEntries(entries);
        user_name_list_preference.setEntryValues(entryValues);

        // set default value index.
        user_name_list_preference.setValueIndex(this.appConfig.getTargetUserNameId() - 1);
    }

    /**
      * @brief Display Summary.
      */
    private void displaySummary() {
        CheckBoxPreference estimate_config = (CheckBoxPreference)findPreference(this.appConfig.getEstimateKey());
        ListPreference user_config = (ListPreference)findPreference(this.appConfig.getTargetUserKey());
        ListPreference start_day_config = (ListPreference)findPreference(this.appConfig.getStartDayKey());

        if( this.appConfig.getEstimate() ) {
            estimate_config.setSummary(getText(R.string.estimate_configuration_enable));
        } else {
            estimate_config.setSummary(getText(R.string.estimate_configuration_unenable));
        }
        user_config.setSummary(this.appConfig.getTargetUserName());
        start_day_config.setSummary(String.valueOf(this.appConfig.getStartDay()) + getText(R.string.day_unit_string).toString());

        // estimate money summary
        setEstimateMoneySummary();
    }

    /**
     * @brief Set Summary for Estimate Money.
     */
    private void setEstimateMoneySummary() {
        EditTextPreference estimate_money_config = (EditTextPreference)findPreference(this.appConfig.getEstimateMoneyKey());

        String estimate_money_summary = getEstimateTargetDate();
        estimate_money_summary = Utility.splitMonth(estimate_money_summary) + getText(R.string.estimate_money_summary_suffix);
        estimate_money_summary = estimate_money_summary + String.format("%,d", this.appConfig.getEstimateMoney());
        estimate_money_config.setSummary(estimate_money_summary);
    }

    /**
      * @brief Regist Event Listner.
      */
    private void registEvent() {
        // Estimate Function Enable/UnEnable Event.
        CheckBoxPreference estimate_pref = (CheckBoxPreference)findPreference(this.appConfig.getEstimateKey());
        estimate_pref.setOnPreferenceChangeListener(
                new OnPreferenceChangeListener() {
                    public boolean onPreferenceChange(Preference pref, Object value) {
                        CheckBoxPreference estimate_config = (CheckBoxPreference)pref;
                        boolean is_estimate = ((Boolean)value).booleanValue();

                        // save estimate parameter.
                        appConfig.saveEstimate(is_estimate);
                        if( is_estimate ) {
                            estimate_config.setSummary(getText(R.string.estimate_configuration_enable));
                        } else {
                            estimate_config.setSummary(getText(R.string.estimate_configuration_unenable));
                        }
                       return true;
                    }
                });

        // UserName Changed Event.
        ListPreference user_pref = (ListPreference)findPreference(this.appConfig.getTargetUserKey());
        user_pref.setOnPreferenceChangeListener(
                new OnPreferenceChangeListener() {
                    public boolean onPreferenceChange(Preference pref, Object value) {
                        ListPreference user_name_pref = (ListPreference)pref;
                        String user_name_id = value.toString();

                        // save target user name.
                        appConfig.saveUserNameId(user_name_id);

                        // display summary.
                        user_name_pref.setSummary(appConfig.getTargetUserName());
                        return true;
                    }
                });

        // Changed Estimate Money Event.
        EditTextPreference estimate_money_pref = (EditTextPreference)findPreference(this.appConfig.getEstimateMoneyKey());
        estimate_money_pref.setOnPreferenceChangeListener(
                new OnPreferenceChangeListener() {
                    public boolean onPreferenceChange(Preference pref, Object value) {
                        EditTextPreference estimate_money_config = (EditTextPreference)pref;
                        try {
                            int estimate_money = Integer.valueOf(value.toString());

                            // save estimate_money.
                            appConfig.saveEstimateMoney(estimate_money);

                            // reflesh estimate money summary.
                            setEstimateMoneySummary();
                        } catch (RuntimeException error) {
                            displayAlertEstimateMoney();
                        }
                        return true;
                    }
                });

        // Change Start Day Event.
        ListPreference start_day_pref = (ListPreference)findPreference(this.appConfig.getStartDayKey());
        start_day_pref.setOnPreferenceChangeListener(
            new OnPreferenceChangeListener() {
                public boolean onPreferenceChange(Preference pref, Object value) {
                    ListPreference start_day_pref = (ListPreference)pref;

                    // save start day.
                    appConfig.savaStartDay(value.toString());
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
        return Utility.getEstimateTargetDate(current_date, appConfig.getStartDay());
    }
}

