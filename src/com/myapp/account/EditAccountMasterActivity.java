package com.myapp.account;

import java.util.*;
import android.util.Log;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import com.myapp.account.R;
import com.myapp.account.database.DatabaseHelper;
import com.myapp.account.database.AccountMasterTableAccessor;
import com.myapp.account.database.AccountMasterTableRecord;
import com.myapp.account.edit_account_master.EditAccountMasterRecord;

/**
 * Main Class in AccountApp Application.
 */
public class EditAccountMasterActivity extends Activity implements OnClickListener, OnLongClickListener {

    protected AccountMasterTableAccessor accountMasterAccessor;
    protected TableLayout tableLayout;
    protected TableRow currentRow;

    /**
     * @brief Create Activity.
     * @param savedInstanceState Bundle Instance.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * @brief Called Activity Start.
     */
    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.edit_account_master);

        // initialize.
        this.init();

        // create edit category display.
        this.createDisplay();
    }

    /**
     * @brief Called Activity is Destoryed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        this.accountMasterAccessor = null;
    }

    /**
     * @brief Called User can not see the Activity.
     */
    @Override
    protected void onStop() {
        super.onStop();
        this.accountMasterAccessor = null;
    }

    /**
     * @brief Initialize Class.
     */
    protected void init() {
        this.accountMasterAccessor = new AccountMasterTableAccessor(new DatabaseHelper(getApplicationContext()));
        this.tableLayout = (TableLayout)findViewById(R.id.account_master_table);
        this.tableLayout.removeAllViews();
    }

    /**
     * @brief Create EditCategory Display.
     */
    protected void createDisplay() {
        boolean is_first_row = false;
        List<AccountMasterTableRecord> master_record = this.accountMasterAccessor.getAll();

        for( AccountMasterTableRecord record : master_record ) {
            EditAccountMasterRecord master_table_row = new EditAccountMasterRecord(this);

            if( false == is_first_row ) {
                is_first_row = true;
                this.focusCurrentRow(master_table_row);
            }

            master_table_row.setAccountMasterInfo(record);
            this.tableLayout.addView(master_table_row);
            master_table_row.setOnClickListener(this);
            master_table_row.setOnLongClickListener(this);
        }
    }

    /**
     * @brief OnClickListener.
     * @param event TableRow Instance.
     */
    @Override
    public void onClick(View event) {
        focusCurrentRow((TableRow)event);
    }

    /**
     * @brief Focus Current Row.
     * @param current_row TableRow Instance.
     */
    protected void focusCurrentRow(TableRow current_row) {
        if( null != this.currentRow ) {
            this.currentRow.setBackgroundColor(getResources().getColor(R.color.default_background));
        }
        this.currentRow = current_row;
        this.currentRow.setBackgroundColor(getResources().getColor(R.color.focus_background));
    }

    /**
     * @brief OnLongClickListener.
     * @param event TableRow Instance.
     * @return true:successed event process false:failed event process.
     */
    @Override
    public boolean onLongClick(View event) {
        focusCurrentRow((TableRow)event);
        return true;
    }
}

