package com.myapp.account.infoarea;

import java.util.*;
import android.util.Log;
import android.app.Activity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import com.myapp.account.R;
import com.myapp.account.infoarea.AbstractInfoArea;
import com.myapp.account.database.AccountTableAccessor;
import com.myapp.account.database.AccountTableRecord;
import com.myapp.account.database.AccountMasterTableRecord;
import com.myapp.account.infoarea.DailyInfoRecord;

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
        // set long click event listener.
        DailyInfoRecord row = new DailyInfoRecord(activity.getApplicationContext());
        row.removeAllViews();

        // get item name from AccountMaster.
        int master_id = account_record.getCategoryId();
        AccountMasterTableRecord account_master_record = masterTable.getRecord(master_id);

        row.setAccountDate(account_record.getInsertDate());
        row.setCategoryName(account_master_record.getName() );
        String money = String.format("%,d", account_record.getMoney() ) + activity.getText(R.string.money_unit).toString();
        row.setAccountMoney( money );
        row.setAccountMemo( account_record.getMemo() );

        row.setClickable(true);
        row.setOnLongClickListener( new OnLongClickListener() {
            @Override
            public boolean onLongClick(View event) {
                focusCurrentRow((TableRow)event);

                if( null != observer ) {
                    observer.notifyLongClickForDailyInfo(event);
                }
                return true;
            }
        });

        // set click event litener.
        row.setOnClickListener( new OnClickListener() {
            @Override
            public void onClick(View event) {
                focusCurrentRow((TableRow)event);
            }
        });

        // display AccountTable.
        layout.addView(row);
    }

    /**
     * Focus Current Row.
     * @param current_row Current TableRow Instance.
     */
    protected void focusCurrentRow(TableRow current_row) {
        if( null != currentRow )
        {
            currentRow.setBackgroundColor(activity.getResources().getColor(R.color.default_background));
        }
        currentRow = current_row;
        currentRow.setBackgroundColor(activity.getResources().getColor(R.color.focus_background));
    }
}
