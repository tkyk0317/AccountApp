package com.myapp.account.factory;

import android.app.Activity;

import com.myapp.account.config.AppConfigurationData;
import com.myapp.account.database.DatabaseHelper;
import com.myapp.account.database.AccountTableAccessor;
import com.myapp.account.database.AccountMasterTableAccessor;
import com.myapp.account.database.EstimateTableAccessor;
import com.myapp.account.database.UserTableAccessor;

/**
 * @brief Factory Class.
 */
public class Factory {

    private static AccountTableAccessor accountTable = null;
    private static AccountMasterTableAccessor accountMaster = null;
    private static EstimateTableAccessor estimateTable = null;
    private static UserTableAccessor userTable = null;
    private static AppConfigurationData appConfigurationData = null;

    /**
     * @brief Get AccountTableAccessor Instance.
     *
     * @param activity Activity Instance.
     *
     * @return AccountTableAccessor Instance.
     */
    public static AccountTableAccessor getAccountTableAcceessor(Activity activity) {
        if( Factory.accountTable == null ) {
            Factory.accountTable = new AccountTableAccessor(new DatabaseHelper(activity.getApplicationContext()),
                                                            Factory.getAppConfigurationData(activity));
        }
        return Factory.accountTable;
    }

    /**
     * @brief Get AccountTableAccessor Instance.
     *
     * @param activity Activity Instance.
     * @param app_config AppConfigurationData Instance.
     *
     * @return AccountTableAccessor Instance.
     */
    public static AccountTableAccessor getAccountTableAcceessor(Activity activity, AppConfigurationData app_config) {
        if( Factory.accountTable == null ) {
            Factory.accountTable = new AccountTableAccessor(new DatabaseHelper(activity.getApplicationContext()), app_config);
        }
        return Factory.accountTable;
    }

    /**
     * @brief Get AccountMasterTable Accessor.
     *
     * @param activity Activity Instance.
     *
     * @return AccountMasterTableAccessor Instance.
     */
    public static AccountMasterTableAccessor getAccountMasterTableAccessor(Activity activity) {
        if( Factory.accountMaster == null ) {
            Factory.accountMaster = new AccountMasterTableAccessor(new DatabaseHelper(activity.getApplicationContext()));
        }
        return Factory.accountMaster;
     }

    /**
     * @brief Estimate Table Accessor.
     *
     * @param activity Activity Instance.
     *
     * @return EstimateTableAccessor Instance.
     */
    public static EstimateTableAccessor getEstimateTableAccessor(Activity activity) {
        if( Factory.estimateTable == null ) {
            Factory.estimateTable = new EstimateTableAccessor(new DatabaseHelper(activity.getApplicationContext()),
                                                              Factory.getAppConfigurationData(activity));
        }
        return Factory.estimateTable;
    }

    /**
     * @brief Estimate Table Accessor.
     *
     * @param activity Activity Instance.
     * @param app_config AppConfigurationData Instance.
     *
     * @return EstimateTableAccessor Instance.
     */
    public static EstimateTableAccessor getEstimateTableAccessor(Activity activity, AppConfigurationData app_config) {
        if( Factory.estimateTable == null ) {
            Factory.estimateTable = new EstimateTableAccessor(new DatabaseHelper(activity.getApplicationContext()), app_config);
        }
        return Factory.estimateTable;
    }

    /**
     * @brief Get UserTable Accessor.
     *
     * @param activity Activity Instance.
     *
     * @return UserTableAccessor Instance.
     */
    public static UserTableAccessor getUserTableAccessor(Activity activity) {
        if( null == Factory.userTable ) {
            Factory.userTable = new UserTableAccessor(new DatabaseHelper(activity.getApplicationContext()));
        }
        return Factory.userTable;
    }

    /**
     * @brief Get AppConfigurationData Instance.
     *
     * @param activity Acitivty Instance.
     *
     * @return
     */
    public static AppConfigurationData getAppConfigurationData(Activity activity) {
        if( null == Factory.appConfigurationData ) {
            Factory.appConfigurationData = new AppConfigurationData(activity);
        }
        return Factory.appConfigurationData;
    }
}

