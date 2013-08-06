package com.myapp.account.tabcontent;

import java.util.*;
import android.app.Activity;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.myapp.account.utility.Utility;
import com.myapp.account.database.DatabaseHelper;
import com.myapp.account.database.AccountTableAccessor;
import com.myapp.account.database.AccountTableRecord;
import com.myapp.account.database.AccountMasterTableAccessor;
import com.myapp.account.observer.ClickObserverInterface;
import com.myapp.account.config.AppConfigurationData;

/**
 * @brief AbstractInfoArea Class.
 */
public abstract class AbstractInfoArea {

    protected Activity activity = null;
    protected AccountTableAccessor accountTable = null;
    protected AccountMasterTableAccessor masterTable = null;
    protected String displayDate = null;
    protected ClickObserverInterface observer = null;
    protected TableRow currentRow = null;
    protected AppConfigurationData appConfig = null;
    protected static int TEXT_SIZE = 15;
    protected static final String MONEY_SPACE = " ";

    /**
     * @brief InfoDailyArea Class Constructor.
     */
    AbstractInfoArea(Activity activity) {
        this.activity = activity;
        this.appConfig = new AppConfigurationData(this.activity);
        DatabaseHelper db_helper = new DatabaseHelper(this.activity.getApplicationContext());
        accountTable = new AccountTableAccessor(db_helper, this.appConfig);
        masterTable = new AccountMasterTableAccessor(db_helper);
    }

    /**
     * @brief Attach Observer Instance.
     * @param observer Observer Instance.
     */
    public void attachObserver(ClickObserverInterface observer) {
        this.observer = observer;
    }

    /**
     * @brief Appear the Daily Information Area.
     */
    public void appear(String display_date) {
        this.displayDate = display_date;

        // get info from database.
        List<AccountTableRecord> account_record = getAccountRecord();
        TableLayout item_table = getTableLayout();

        // remove child item.
        item_table.removeAllViews();

        // item loop.
        for( AccountTableRecord record : account_record ) {
            drawRecord(item_table, record);
        }
    }

    /**
     * @brief Get Start Date of Month.
     *
     * @return start_date.
     */
    protected String getStartDateOfMonth() {
        return Utility.getStartDateOfMonth(this.displayDate, this.appConfig.getStartDay());
    }

    /**
     * @brief Get End Date of Month.
     *
     * @return end date.
     */
    protected String getEndDateOfMonth() {
        return Utility.getEndDateOfMonth(this.displayDate, this.appConfig.getStartDay());
    }

    /**
     * @brief Get AccountTable Record.
     * @return AccounTable record List.
     */
    abstract List<AccountTableRecord> getAccountRecord();

    /**
     * @brief Get Table Layout instance.
     * @return TableRayout Instance.
     */
    abstract TableLayout getTableLayout();

    /**
     * @brief Draw Item from AccountTable.
     *
     * @param layout TableLayout instance.
     * @param account_record AccountTable Record(Displayed Item).
     */
    abstract void drawRecord(TableLayout layout, AccountTableRecord accunt_record);
}

