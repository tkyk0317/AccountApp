package com.myapp.account.graph;

import java.util.List;

import android.app.Activity;
import android.widget.LinearLayout;

import com.myapp.account.graph.AbstractAccountGraph;
import com.myapp.account.database.DatabaseHelper;
import com.myapp.account.database.AccountMasterTableRecord;
import com.myapp.account.database.AccountTableRecord;

/**
 * @brief Account Income Pie Graph Class.
 */
public class AccountIncomePieGraphImpl extends AbstractAccountGraph {

    /**
     * @brief Constractor.
     * @param activity Activity Instance.
     */
    public AccountIncomePieGraphImpl(Activity activity, LinearLayout layout) {
        super(activity, layout);
    }

    /**
     * @brief Check Exsit Record at Target Date.
     * @return true:exsit false:not exsit.
     */
    protected boolean isExsitRecord() {
        String start_date = getStartDateOfMonth();
        String end_date = getEndDateOfMonth();
        return this.accountTable.isExsitRecordBetweenStartDateAndEndDate(start_date, end_date);
    }

    /**
     * @brief Get Used Data In Graph.
     */
    protected List<AccountTableRecord> getUsedDataInGraph() {
        String start_date = getStartDateOfMonth();
        String end_date = getEndDateOfMonth();
        return this.accountTable.getRecordWithTargetMonthGroupByCategoryId(start_date, end_date);
    }

    /**
     * @brief Check Target Record Data.
     * @param master_record master table record.
     * @return true:target false:not target.
     */
    protected boolean isTargetAccountRecord(AccountMasterTableRecord master_record) {
        if( DatabaseHelper.INCOME_FLAG == master_record.getKindId() ) return true;
        return false;
    }
}


