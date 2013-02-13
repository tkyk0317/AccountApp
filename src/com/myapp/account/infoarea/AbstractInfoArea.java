package com.myapp.account.infoarea;

import java.util.*;
import android.util.Log;
import android.app.Activity;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.myapp.account.R;
import com.myapp.account.utility.Utility;
import com.myapp.account.database.DatabaseHelper;
import com.myapp.account.database.AccountTableAccessor;
import com.myapp.account.database.AccountTableRecord;
import com.myapp.account.database.AccountMasterTableRecord;
import com.myapp.account.database.AccountMasterTableAccessor;

/**
 * AbstractInfoArea Class.
 */
public abstract class AbstractInfoArea {

    protected Activity activity;
    protected AccountTableAccessor accountTable;
    protected AccountMasterTableAccessor masterTable;
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
    public void appear() {
        Log.d("AbstractInfoArea", "[START]");
        // get info from database.
        List<AccountTableRecord> account_record = getAccountRecord();
        TableLayout item_table = getTableLayout();

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
    protected void drawRecord(TableLayout layout, AccountTableRecord account_record) {
        TextView account_date = new TextView(activity.getApplicationContext());
        TextView account_item = new TextView(activity.getApplicationContext());
        TextView account_money= new TextView(activity.getApplicationContext());

        // get item name from AccountMaster.
        int master_id = account_record.getCategoryId();
        AccountMasterTableRecord account_master_record = masterTable.getRecord(master_id);

        account_date.setText( Utility.splitMonthAndDay(account_record.getInsertDate()) );
        account_item.setText( account_master_record.getName() );
        String money = String.format("%,d", account_record.getMoney() ) + activity.getText(R.string.money_unit).toString();
        account_money.setText( "(" + money +")" );

        account_date.setTextSize(TEXT_SIZE);
        account_money.setTextSize(TEXT_SIZE);
        account_item.setTextSize(TEXT_SIZE);

        account_date.setGravity(Gravity.RIGHT);
        account_item.setGravity(Gravity.RIGHT);
        account_money.setGravity(Gravity.RIGHT);

        // display AccountTable.
        TableRow row = new TableRow(activity.getApplicationContext());
        row.addView(account_date);
        row.addView(account_item);
        row.addView(account_money);
        layout.addView(row);
    }
}


