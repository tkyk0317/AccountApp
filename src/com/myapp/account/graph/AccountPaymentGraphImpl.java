package com.myapp.account.graph;

import android.app.Activity;

import com.myapp.account.R;
import com.myapp.account.graph.AbstractAccountGraph;
import com.myapp.account.database.DatabaseHelper;
import com.myapp.account.database.AccountMasterTableRecord;
import com.myapp.account.utility.Utility;

/**
 * @brief Account PaymentGraph Class.
 */
public class AccountPaymentGraphImpl extends AbstractAccountGraph {

    /**
     * @brief Constractor.
     * @param activity Activity Instance.
     */
    public AccountPaymentGraphImpl(Activity activity) {
        super(activity);
    }

    /**
     * @brief Check Target Record Data.
     * @param master_record master table record.
     * @return true:target false:not target.
     */
    protected boolean isTargetAccountRecord(AccountMasterTableRecord master_record) {
        if( DatabaseHelper.PAYMENT_FLAG == master_record.getKindId() ) return true;
        return false;
    }
}


