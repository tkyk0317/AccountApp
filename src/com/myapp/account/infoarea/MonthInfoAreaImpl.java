package com.myapp.account.infoarea;

import java.util.*;
import android.app.Activity;
import android.widget.TableLayout;
import com.myapp.account.R;
import com.myapp.account.infoarea.AbstractInfoArea;
import com.myapp.account.database.AccountTableAccessImpl;
import com.myapp.account.database.AccountTableRecord;
import com.myapp.account.database.AccountMasterTableRecord;
import com.myapp.account.database.AccountMasterTableAccessImpl;

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
    protected List<AccountTableRecord> getAccountRecord() {
        return accountTable.getRecordWithCurrentMonthGroupByCategoryId();
    }

    /**
     * Get Table Rayout instansce.
     * @return TableRayout Instance.
     */
    protected TableLayout getTableLayout() {
        return (TableLayout) activity.findViewById(R.id.month_item_table);
    }
}
