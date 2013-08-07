package com.myapp.account.tabcontent.infoarea;

import java.util.*;

import android.app.Activity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;

import com.myapp.account.R;
import com.myapp.account.database.AccountTableRecord;
import com.myapp.account.database.AccountMasterTableRecord;

/**
 * @brief Daily Info Area Class.
 */
public class DailyInfoAreaImpl extends AbstractInfoArea {

    protected static final int DISPLAY_MAX_LINE = 1;
    protected List<AccountMasterTableRecord> masterRecord = null;
    protected List<DailyInfoRecord> dailyRecord = null;

    /**
     * @brief Constructor.
     */
    public DailyInfoAreaImpl(Activity activity) {
        super(activity);
    }

    /**
     * @brief Get Table Layout instance.
     *
     * @return TableRayout Instance.
     */
    @Override
    protected TableLayout getTableLayout() {
        return (TableLayout) this.activity.findViewById(R.id.daily_item_table);
    }

    /**
     * @brief Get AccountTable Record.
     * @return AccounTable record List.
     */
    @Override
    protected List<AccountTableRecord> getAccountRecord() {
        return this.accountTable.getRecordWithTargetDate(displayDate);
    }

    /**
     * @brief Appear the Daily Information Area.
     */
    public void appear(String display_date) {
        this.displayDate = display_date;

        // get info from database.
        List<AccountTableRecord> account_record = getAccountRecord();

        // remove child item.
        this.tableLayout.removeAllViews();

        // get master record.
        this.masterRecord = this.masterTable.getAllRecord();

        // item loop.
        for( AccountTableRecord record : account_record ) {
            drawRecord(this.tableLayout, record);
        }
        // delete list.
        this.masterRecord = null;
    }

    /**
     * @brief Draw Item from AccountTable.
     *
     * @param layout TableLayout instance.
     * @param account_record AccountTable Record(Displayed Item).
     */
    @Override
    protected void drawRecord(TableLayout layout, AccountTableRecord account_record) {
        // set long click event listener.
        DailyInfoRecord row = new DailyInfoRecord(this.activity);
        row.removeAllViews();

        // get item name from AccountMaster.
        int master_id = account_record.getCategoryId();
        AccountMasterTableRecord account_master_record = getMasterRecord(master_id);

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
     * @brief Get Master Table Record.
     *
     * @param id key id.
     *
     * @return AccountMasterTableRecord.
     */
    protected AccountMasterTableRecord getMasterRecord(int id) {
        for( AccountMasterTableRecord record : this.masterRecord ) {
            if( id == record.getId() ) {
                return record;
            }
        }
        // bad case.
        return new AccountMasterTableRecord();
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
