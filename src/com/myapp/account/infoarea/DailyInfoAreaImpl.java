package com.myapp.account.infoarea;

import java.util.*;
import android.app.Activity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import com.myapp.account.R;
import com.myapp.account.infoarea.AbstractInfoArea;
import com.myapp.account.database.AccountTableRecord;
import com.myapp.account.database.AccountMasterTableRecord;
import com.myapp.account.infoarea.DailyInfoRecord;

/**
 * @brief Daily Info Area Class.
 */
public class DailyInfoAreaImpl extends AbstractInfoArea {

    protected static final int DISPLAY_MAX_LINE = 1;

    /**
     * @brief Constructor.
     */
    public DailyInfoAreaImpl(Activity activity) {
        super(activity);
    }

    /**
     * @brief Get AccountTable Record.
     * @return AccounTable record List.
     */
    @Override
    protected List<AccountTableRecord> getAccountRecord() {
        return accountTable.getRecordWithTargetDate(displayDate);
    }

    /**
     * @brief Get Table Layout instance.
     * @return TableRayout Instance.
     */
    @Override
    protected TableLayout getTableLayout() {
        return (TableLayout) activity.findViewById(R.id.daily_item_table);
    }

    /**
     * @brief Draw Item from AccountTable.
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
        row.setCategoryName(account_master_record.getName());
        row.setKindId(account_master_record.getKindId());
        String money = String.format("%,d", account_record.getMoney() ) + activity.getText(R.string.money_unit).toString();
        row.setAccountMoney( MONEY_SPACE + money + MONEY_SPACE );
        row.setAccountMemo( account_record.getMemo() );

        row.setClickable(true);
        row.setOnLongClickListener( new OnLongClickListener() {
            @Override
            public boolean onLongClick(View event) {
                focusCurrentRow((TableRow)event);

                if( null != observer ) {
                    observer.notifyLongClick(event);
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

        // setting record info.
        row.setAccountTableRecord(account_record);

        // display AccountTable.
        layout.addView(row);
    }

    /**
     * @brief Focus Current Row.
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
