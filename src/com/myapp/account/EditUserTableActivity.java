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
import com.myapp.account.observer.EventCompleteObserver;
import com.myapp.account.database.DatabaseHelper;
import com.myapp.account.database.UserTableAccessor;
import com.myapp.account.database.UserTableRecord;
import com.myapp.account.edit_user_table.EditUserTableRecord;
import com.myapp.account.dialog.AddUserTableDialogImpl;
import com.myapp.account.dialog.EditUserTableDialogImpl;

/**
 * @brief Edit UserTable Activity.
 */
public class EditUserTableActivity extends Activity implements OnClickListener, OnLongClickListener, EventCompleteObserver {

    private UserTableAccessor userTable = null;
    private TableLayout tableLayout = null;
    private EditUserTableRecord currentRow = null;
    private ImageView addUserImage = null;
    private AddUserTableDialogImpl addUserTableDialog = null;
    private EditUserTableDialogImpl editUserTableDialog = null;

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
        setContentView(R.layout.edit_user);

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
        this.userTable = null;
    }

    /**
     * @brief Called User can not see the Activity.
     */
    @Override
    protected void onStop() {
        super.onStop();
        this.userTable = null;
    }

    /**
     * @brief Initialize Class.
     */
    private void init() {
        this.userTable = new UserTableAccessor(new DatabaseHelper(this.getApplicationContext()));
        this.addUserImage = (ImageView)findViewById(R.id.add_user_image);
        this.addUserImage.setImageDrawable(getResources().getDrawable(R.drawable.add_button));
        this.addUserImage.setId(ViewId.ADD_USER_TABLE.getId());
        this.addUserImage.setOnClickListener(this);
        this.addUserTableDialog = new AddUserTableDialogImpl(this);
        this.editUserTableDialog = new EditUserTableDialogImpl(this);

        // attach observer.
        this.addUserTableDialog.attachObserver(this);
        this.editUserTableDialog.attachObserver(this);

        // init table layout.
        this.tableLayout = (TableLayout)findViewById(R.id.user_table_layout);
    }

    /**
     * @brief Create EditCategory Display.
     */
    private void createDisplay() {
        boolean is_first_row = false;
        this.tableLayout.removeAllViews();

        List<UserTableRecord> user_records = this.userTable.getAllRecord();

        for( UserTableRecord record : user_records ) {
            EditUserTableRecord user_table_row = new EditUserTableRecord(this);

            if( false == is_first_row ) {
                is_first_row = true;
                this.focusCurrentRow(user_table_row);
            }
            user_table_row.setUserTableRecord(record);
            this.tableLayout.addView(user_table_row);
            user_table_row.setId(ViewId.USER_RECORD.getId());
            user_table_row.setOnClickListener(this);
            user_table_row.setOnLongClickListener(this);
        }
    }

    /**
     * @brief OnClickListener.
     * @param event TableRow Instance.
     */
    @Override
    public void onClick(View event) {
        if(ViewId.USER_RECORD.getId() == event.getId()) {
            focusCurrentRow((EditUserTableRecord)event);
        } else if(ViewId.ADD_USER_TABLE.getId() == event.getId()) {
            this.addUserTableDialog.appear();
        }
    }

    /**
     * @brief Focus Current Row.
     * @param current_row EditAccountMasterRecord Instance.
     */
    private void focusCurrentRow(EditUserTableRecord current_row) {
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
        focusCurrentRow((EditUserTableRecord)event);
        this.editUserTableDialog.appear((EditUserTableRecord)event);
        return true;
    }

    /**
     * @brief UserTable Edit Complete.
     *
     * @return
     */
    @Override
    public void notifyUserTableEditComplete() {
        createDisplay();
    }

    // not support.
    @Override
    public void notifyAccountMasterEditComplete() {}
    @Override
    public void notifyAccountEditComplete() {}

    /**
     * @brief View ID Class.
     */
    private enum ViewId {
        ADD_USER_TABLE(0), USER_RECORD(1);

        private final int id;

        private ViewId(int id) { this.id = id; }
        public int getId() { return this.id; }
    }
}

