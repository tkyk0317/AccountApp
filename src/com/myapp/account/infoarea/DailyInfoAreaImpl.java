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
import com.myapp.account.database.AccountMasterTableRecord;

/**
 * Daily Info Area Class.
 */
public class DailyInfoAreaImpl extends AbstractInfoArea {

    protected static final int DISPLAY_MAX_LINE = 1;

    /**
     * Constractor.
     */
    public DailyInfoAreaImpl(Activity activity) {
        super(activity);
    }

    /**
     * Get AccountTable Record.
     * @return AccounTable record List.
     */
    @Override
    protected List<AccountTableRecord> getAccountRecord() {
        return accountTable.getRecordWithTargetDateGroupByCategoryId(displayDate);
    }

    /**
     * Get Table Rayout instansce.
     * @return TableRayout Instance.
     */
    @Override
    protected TableLayout getTableLayout() {
        return (TableLayout) activity.findViewById(R.id.daily_item_table);
    }

    /**
     * Draw Item from AccountTable.
     * @param layout TableLayout instance.
     * @param account_record AccountTable Record(Displayed Item).
     */
    @Override
    protected void drawRecord(TableLayout layout, AccountTableRecord account_record) {
        TextView account_item = new TextView(activity.getApplicationContext());
        TextView account_money= new TextView(activity.getApplicationContext());
        TextView account_memo = new TextView(activity.getApplicationContext());

        // get item name from AccountMaster.
        int master_id = account_record.getCategoryId();
        AccountMasterTableRecord account_master_record = masterTable.getRecord(master_id);

        account_item.setText( account_master_record.getName() );
        String money = String.format("%,d", account_record.getMoney() ) + activity.getText(R.string.money_unit).toString();
        account_money.setText(money);
        account_memo.setText( account_record.getMemo() );

        account_money.setTextSize(TEXT_SIZE);
        account_item.setTextSize(TEXT_SIZE);
        account_memo.setTextSize(TEXT_SIZE);
        account_memo.setMaxLines(DISPLAY_MAX_LINE);

        account_item.setGravity(Gravity.RIGHT);
        account_money.setGravity(Gravity.RIGHT);
        account_memo.setGravity(Gravity.RIGHT);

        // display AccountTable.
        TableRow row = new TableRow(activity.getApplicationContext());
        row.addView(account_item);
        row.addView(account_money);
        row.addView(account_memo);
        layout.addView(row);
    }
}
