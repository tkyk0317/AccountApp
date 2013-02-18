package com.myapp.account.infoarea;

import java.util.*;
import android.util.Log;
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
import com.myapp.account.infoarea.MonthlyInfoRecord;

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
        // display.
        MonthlyInfoRecord row = new MonthlyInfoRecord(activity.getApplicationContext());
        row.removeAllViews();

        // get item name from AccountMaster.
        int master_id = account_record.getCategoryId();
        AccountMasterTableRecord account_master_record = masterTable.getRecord(master_id);

        row.setAccountDate(Utility.splitMonthAndDay(account_record.getInsertDate()));
        String money = String.format("%,d", account_record.getMoney() ) + activity.getText(R.string.money_unit).toString();
        row.setAccountMoney( MONEY_SPACE + money + MONEY_SPACE );
        row.setCategoryName(account_master_record.getName() );

        layout.addView(row);
    }
}

