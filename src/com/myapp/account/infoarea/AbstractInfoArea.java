package com.myapp.account.infoarea;

import java.util.*;
import android.util.Log;
import android.app.Activity;
import android.widget.TableLayout;
import com.myapp.account.R;
import com.myapp.account.database.DatabaseHelper;
import com.myapp.account.database.AccountTableAccessor;
import com.myapp.account.database.AccountTableRecord;
import com.myapp.account.database.AccountMasterTableAccessor;

/**
 * AbstractInfoArea Class.
 */
public abstract class AbstractInfoArea {

    protected Activity activity;
    protected AccountTableAccessor accountTable;
    protected AccountMasterTableAccessor masterTable;
    protected String displayDate;
    protected static int TEXT_SIZE = 15;

    /**
     * InfoDailyArea Class Constractor.
     */
    AbstractInfoArea(Activity activity) {
        this.activity = activity;
        DatabaseHelper db_helper = new DatabaseHelper(this.activity.getApplicationContext());
        accountTable = new AccountTableAccessor(db_helper);
        masterTable = new AccountMasterTableAccessor(db_helper);
    }

    /**
     * Appear the Daily Infomation Area.
     */
    public void appear(String display_date) {
        Log.d("AbstractInfoArea", "[START]");
        this.displayDate = display_date;

        // get info from database.
        List<AccountTableRecord> account_record = getAccountRecord();
        TableLayout item_table = getTableLayout();

        // remove child item.
        item_table.removeAllViews();

        // item loop.
        for( int i = 0 ; i < account_record.size() ; i++ ) {
            // draw.
            drawRecord(item_table, account_record.get(i) );
        }
        Log.d("AbstractInfoArea", "[END]");
    }

    /**
     * Get AccountTable Record.
     * @return AccounTable record List.
     */
    abstract List<AccountTableRecord> getAccountRecord();

    /**
     * Get Table Rayout instansce.
     * @return TableRayout Instance.
     */
    abstract TableLayout getTableLayout();

    /**
     * Draw Item from AccountTable.
     * @param layout TableLayout instance.
     * @param account_record AccountTable Record(Displayed Item).
     */
    abstract void drawRecord(TableLayout layout, AccountTableRecord accunt_record);
}


