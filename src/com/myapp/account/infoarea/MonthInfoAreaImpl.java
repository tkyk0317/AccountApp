package com.myapp.account.infoarea;

import java.util.*;
import android.app.Activity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.view.Gravity;
import com.myapp.account.R;
import com.myapp.account.infoarea.AbstractInfoArea;
import com.myapp.account.database.AccountTableAccessor;
import com.myapp.account.database.AccountTableRecord;
import com.myapp.account.utility.Utility;
import com.myapp.account.database.AccountMasterTableRecord;

/**
 * Month Info Area Class.
 */
public class MonthInfoAreaImpl extends AbstractInfoArea {

    /**
     * Constractor.
     */
    public MonthInfoAreaImpl(Activity activity) {
        super(activity);
    }

    /**
     * Get AccountTable Record.
     * @return AccounTable record List.
     */
    @Override
    protected List<AccountTableRecord> getAccountRecord() {
        return accountTable.getRecordWithTargetMonthGroupByCategoryId(displayDate);
    }

    /**
     * Get Table Rayout instansce.
     * @return TableRayout Instance.
     */
    @Override
    protected TableLayout getTableLayout() {
        return (TableLayout) activity.findViewById(R.id.month_item_table);
    }

    /**
     * Draw Item from AccountTable.
     * @param layout TableLayout instance.
     * @param account_record AccountTable Record(Displayed Item).
     */
    @Override
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
        account_money.setText(money);

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
    }}
