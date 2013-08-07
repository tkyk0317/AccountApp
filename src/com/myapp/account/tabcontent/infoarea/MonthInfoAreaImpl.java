package com.myapp.account.tabcontent.infoarea;

import java.util.*;
import android.app.Activity;
import android.widget.TableLayout;

import com.myapp.account.R;
import com.myapp.account.database.AccountTableRecord;
import com.myapp.account.database.AccountMasterTableRecord;

/**
 * @brief Month Info Area Class.
 */
public class MonthInfoAreaImpl extends AbstractInfoArea {

    /**
     * @brief Constructor.
     */
    public MonthInfoAreaImpl(Activity activity) {
        super(activity);
    }

    /**
     * @brief Get AccountTable Record.
     * @return AccounTable record List.
     */
    @Override
    protected List<AccountTableRecord> getAccountRecord() {
        String start_date = getStartDateOfMonth();
        String end_date = getEndDateOfMonth();
        return this.accountTable.getRecordWithTargetMonthGroupByCategoryId(start_date, end_date);
    }

    /**
     * @brief Get Table Layout instance.
     * @return TableRayout Instance.
     */
    @Override
    protected TableLayout getTableLayout() {
        return (TableLayout)this.activity.findViewById(R.id.month_item_table);
    }

    /**
     * @brief Draw Item from AccountTable.
     *
     * @param layout TableLayout instance.
     * @param account_record AccountTable Record(Displayed Item).
     */
    @Override
    protected void drawRecord(TableLayout layout, AccountTableRecord account_record) {
        // display.
        MonthlyInfoRecord row = new MonthlyInfoRecord(activity);
        row.removeAllViews();

        // get item name from AccountMaster.
        int master_id = account_record.getCategoryId();
        AccountMasterTableRecord account_master_record = this.masterTable.getRecord(master_id);

        row.setCategoryName(account_master_record.getName() );
        row.setKindId(account_master_record.getKindId());
        String money = String.format("%,d", account_record.getMoney() ) + activity.getText(R.string.money_unit).toString();
        row.setAccountMoney( MONEY_SPACE + money + MONEY_SPACE );

        layout.addView(row);
    }
}

