package com.myapp.account;

import java.util.*;

import android.util.Log;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;

import com.myapp.account.R;
import com.myapp.account.dialog.AccountDialogInterface;
import com.myapp.account.dialog.AddAccountMasterDialogImpl;
import com.myapp.account.dialog.EditAccountMasterDialogImpl;
import com.myapp.account.observer.EventCompleteObserver;
import com.myapp.account.database.DatabaseHelper;
import com.myapp.account.database.AccountMasterTableAccessor;
import com.myapp.account.database.AccountMasterTableRecord;
import com.myapp.account.edit_account_master.EditAccountMasterRecord;

/**
 * @brief Edit AccountMasterTable Activity.
 */
public class EditAccountMasterActivity extends Activity implements OnClickListener, OnLongClickListener, EventCompleteObserver {

    private AccountMasterTableAccessor accountMasterAccessor = null;
    private TableLayout tableLayout = null;
    private EditAccountMasterRecord currentRow = null;
    private ImageView addCategoryImage = null;
    private AccountDialogInterface addAccountMasterDialog = null;
    private AccountDialogInterface editAccountMasterDialog = null;

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
    private void init() {
        this.addAccountMasterDialog = new AddAccountMasterDialogImpl(this);
        this.editAccountMasterDialog = new EditAccountMasterDialogImpl(this);
        this.accountMasterAccessor = new AccountMasterTableAccessor(new DatabaseHelper(getApplicationContext()));
        this.addCategoryImage = (ImageView)findViewById(R.id.add_master_image);
        this.addCategoryImage.setImageDrawable(getResources().getDrawable(R.drawable.add_button));
        this.addCategoryImage.setId(ViewId.ADD_MASTER.getId());
        this.addCategoryImage.setOnClickListener(this);

        // attach observer.
        this.addAccountMasterDialog.attachObserver(this);
        this.editAccountMasterDialog.attachObserver(this);

        // init table layout.
        this.tableLayout = (TableLayout)findViewById(R.id.account_master_table);
    }

    /**
     * @brief Create EditCategory Display.
     */
    private void createDisplay() {
        boolean is_first_row = false;
        this.tableLayout.removeAllViews();

        List<AccountMasterTableRecord> master_record = this.accountMasterAccessor.getAllRecord();

        for( AccountMasterTableRecord record : master_record ) {
            EditAccountMasterRecord master_table_row = new EditAccountMasterRecord(this);

            if( false == is_first_row ) {
                is_first_row = true;
                this.focusCurrentRow(master_table_row);
            }

            master_table_row.setAccountMasterInfo(record);
            this.tableLayout.addView(master_table_row);
            master_table_row.setId(ViewId.MASTER_RECORD.getId());
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
        if(ViewId.MASTER_RECORD.getId() == event.getId()) {
            focusCurrentRow((EditAccountMasterRecord)event);
        } else if(ViewId.ADD_MASTER.getId() == event.getId()) {
            this.addAccountMasterDialog.appear();
        }
    }

    /**
     * @brief Focus Current Row.
     * @param current_row EditAccountMasterRecord Instance.
     */
    private void focusCurrentRow(EditAccountMasterRecord current_row) {
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
        focusCurrentRow((EditAccountMasterRecord)event);
        this.editAccountMasterDialog.appear((EditAccountMasterRecord)event);
        return true;
    }

    /**
     * @brief AccountMaster Edit Complete.
     */
    @Override
    public void notifyAccountMasterEditComplete() {
        createDisplay();
    }

    // not support.
    @Override
    public void notifyAccountEditComplete() {}
    @Override
    public void notifyUserTableEditComplete() {}

    /**
     * @brief View ID Class.
     */
    private enum ViewId {
        MASTER_RECORD(0), ADD_MASTER(1);

        private final int id;

        private ViewId(int id) { this.id = id; }
        public int getId() { return this.id; }
    }
}

