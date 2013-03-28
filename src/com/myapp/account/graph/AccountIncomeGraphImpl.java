package com.myapp.account.graph;

import android.app.Activity;

import com.myapp.account.R;
import com.myapp.account.graph.AbstractAccountGraph;
import com.myapp.account.database.DatabaseHelper;
import com.myapp.account.database.AccountMasterTableRecord;
import com.myapp.account.utility.Utility;

/**
 * @brief Account Income Graph Class.
 */
public class AccountIncomeGraphImpl extends AbstractAccountGraph {

    /**
     * @brief Constractor.
     * @param activity Activity Instance.
     */
    public AccountIncomeGraphImpl(Activity activity) {
        super(activity);
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


