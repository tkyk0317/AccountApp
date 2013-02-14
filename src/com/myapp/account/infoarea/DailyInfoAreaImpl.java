package com.myapp.account.infoarea;

import java.util.*;
import android.app.Activity;
import android.widget.TableLayout;
import com.myapp.account.R;
import com.myapp.account.infoarea.AbstractInfoArea;
import com.myapp.account.database.AccountTableAccessor;
import com.myapp.account.database.AccountTableRecord;

/**
 * Daily Info Area Class.
 */
public class DailyInfoAreaImpl extends AbstractInfoArea {

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
    protected List<AccountTableRecord> getAccountRecord() {
        return accountTable.getRecordWithTargetDateGroupByCategoryId(displayDate);
    }

    /**
     * Get Table Rayout instansce.
     * @return TableRayout Instance.
     */
    protected TableLayout getTableLayout() {
        return (TableLayout) activity.findViewById(R.id.daily_item_table);
    }
}
