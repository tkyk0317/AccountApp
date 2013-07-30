package com.myapp.account.dialog;

import android.view.LayoutInflater;
import android.app.AlertDialog;
import android.view.View;
import android.widget.TextView;
import android.view.ViewGroup;
import android.view.Gravity;
import android.app.Activity;
import android.widget.Button;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.LinearLayout;
import android.widget.AdapterView;
import android.content.DialogInterface;
import android.widget.AdapterView.OnItemSelectedListener;

import com.myapp.account.R;
import com.myapp.account.utility.Utility;
import com.myapp.account.config.AppConfigurationData;
import com.myapp.account.dialog.AccountDialogInterface;
import com.myapp.account.observer.EventCompleteObserver;
import com.myapp.account.database.DatabaseHelper;
import com.myapp.account.database.AccountTableAccessor;
import com.myapp.account.database.AccountMasterTableAccessor;
import com.myapp.account.database.AccountMasterTableRecord;
import com.myapp.account.edit_account_master.EditAccountMasterRecord;

/**
 * @brief EditAccountMAsterRecord Dialog Class.
 */
public class EditAccountMasterDialogImpl implements OnItemSelectedListener, AccountDialogInterface, EventCompleteObserver {

    private Activity activity = null;
    private View addCategoryView = null;
    private AlertDialog addCategoryDialog = null;
    private AccountRecordModifyEvent modifyEvent = null;
    private AccountRecordDeleteEvent deleteEvent = null;
    private Spinner kindSpinner = null;
    private Button modifyButton = null;
    private Button deleteButton = null;
    private EventCompleteObserver observer = null;
    private EditAccountMasterRecord editRecord = null;
    private static final int BUTTON_WIDTH = 100;

    /**
     * @brief Constructor.
     *
     * @param activity Activity Instance.
     */
    public EditAccountMasterDialogImpl(Activity activity) {
        this.activity = activity;
    }

    /**
     * @brief Attach Observer.
     * @param observer EventCompleteObserver Instance.
     */
    @Override
    public void attachObserver(EventCompleteObserver observer) {
        this.observer = observer;
    }

    /**
     * @brief Appear the Add AccountMasterRecord Dialog.
     * @note not supported.
     */
    @Override
    public void appear() {}

    /**
     * @brief Appear Dialog.
     *
     * @param TableRow table row instances.
     */
    public void appear(EditAccountMasterRecord table_row) {
        this.editRecord = table_row;

        init();
        displayTitle();
        displayDialog();
    }

    /**
     * @brief Initialize.
     */
    private void init() {
        LayoutInflater inflater = LayoutInflater.from(this.activity);
        this.addCategoryView = inflater.inflate(R.layout.edit_account_master_dialog,
                                                 (ViewGroup)this.activity.findViewById(R.id.edit_account_master_dialog));
        this.kindSpinner = (Spinner)this.addCategoryView.findViewById(R.id.kind_spinner);

        // initialize button.
        initButton();

        // initialize event instance.
        this.modifyEvent = new AccountRecordModifyEvent(this.activity, this.addCategoryView, this.editRecord);
        this.deleteEvent = new AccountRecordDeleteEvent(this.activity, this.editRecord);
        this.modifyEvent.attachObserver(this);
        this.deleteEvent.attachObserver(this);

        // register event.
        registEvent();

        // initialize current data.
        setCurrentData();
    }

    /**
     * @brief Initialize Button.
     */
    private void initButton() {
        this.modifyButton = (Button)this.addCategoryView.findViewById(R.id.regist_btn);
        this.modifyButton.setText(this.activity.getText(R.string.modify_btn_label).toString());

        // add delete button.
        this.deleteButton = new Button(this.activity);
        this.deleteButton.setWidth(BUTTON_WIDTH);
        this.deleteButton.setText(this.activity.getText(R.string.delete_btn_label).toString());

        // check enable delete.
        if( false == isEnableDeleteRecord() ) {
            this.deleteButton.setEnabled(false);
            this.deleteButton.setClickable(false);
            displayRepletionMessage();
        }

        LinearLayout layout = (LinearLayout)this.addCategoryView.findViewById(R.id.commit_btn_area);
        layout.addView(this.deleteButton);
    }

    /**
     * @brief Check Enable Delete Record.
     *
     * @return true:enable delete false:unenable delete.
     */
    private boolean isEnableDeleteRecord() {
        AppConfigurationData app_config = new AppConfigurationData(this.activity);
        AccountTableAccessor accountTable = new AccountTableAccessor(new DatabaseHelper(this.activity.getApplicationContext()), app_config);
        return !(accountTable.isExsitRecordAtCategoryId(this.editRecord.getPrimaryId()));
    }

    /**
     * @brief Display Repletion.
     */
    private void displayRepletionMessage() {
        String message = this.activity.getText(R.string.del_category_repletion_message).toString();
        Toast.makeText(this.activity, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * @brief Register Event.
     */
    private void registEvent() {
        this.modifyButton.setOnClickListener(this.modifyEvent);
        this.deleteButton.setOnClickListener(this.deleteEvent);
        this.kindSpinner.setOnItemSelectedListener(this);
    }

    /**
     * @brief Set Current Data.
     */
    private void setCurrentData() {
        EditText category_value = (EditText)this.addCategoryView.findViewById(R.id.category_value);

        // set current data.
        this.kindSpinner.setSelection(this.editRecord.getKindId());
        category_value.setText(this.editRecord.getName());
    }

    /**
     * @brief Display Dialog Title.
     */
    private void displayTitle() {
        TextView title = (TextView)this.addCategoryView.findViewById(R.id.title);
        title.setText(this.activity.getText(R.string.add_category_dialog_title).toString());
    }

    /**
     * @brief Display Dialog.
     */
    private void displayDialog() {
        this.addCategoryDialog = new AlertDialog.Builder(this.activity).create();
        this.addCategoryDialog.setView(this.addCategoryView);
        this.addCategoryDialog.getWindow().setGravity(Gravity.TOP);
        this.addCategoryDialog.show();

        // modify dialog width.
        Utility.modifyDialogWidthMax(this.addCategoryDialog);
    }

    /**
     * @brief Select item on Spinner.
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id ) {
        Spinner spinner = (Spinner)parent;
        this.modifyEvent.notifySelectedSpinner((String)spinner.getSelectedItem().toString());
    }

    /**
     * @brief Non Selected item on Spinner.
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    /**
     * @brief AccountMaster Edit Complete.
     */
    @Override
    public void notifyAccountMasterEditComplete() {
        if(null != this.observer) this.observer.notifyAccountMasterEditComplete();
        this.addCategoryDialog.dismiss();
    }

    // not support.
    @Override
    public void notifyAccountEditComplete() {}
    @Override
    public void notifyUserTableEditComplete() {}

    /**
     * @brief Modify AccountRecord Event.
     */
    private class AccountRecordModifyEvent implements View.OnClickListener {

        private Activity activity;
        private View dialogView;
        private String selectedCategoryKind;
        private EditAccountMasterRecord editRecord;
        private EventCompleteObserver observer;
        private AccountMasterTableAccessor accountMasterAccessor;

        /**
         * @brief Constructor.
         */
        public AccountRecordModifyEvent(Activity activity, View dialog_view, EditAccountMasterRecord edit_record) {
            this.observer = null;
            this.activity = activity;
            this.dialogView = dialog_view;
            this.editRecord = edit_record;
            this.accountMasterAccessor = new AccountMasterTableAccessor(new DatabaseHelper(this.activity.getApplicationContext()));
        }

        /**
         * @brief Attach Observer.
         *
         * @param observer Observer Instance.
         */
        public void attachObserver(EventCompleteObserver observer) {
            this.observer = observer;
        }

        /**
         * @brief notify selected spinner.
         *
         * @param kind_value Selected String.
         */
        public void notifySelectedSpinner(String kind_value) {
            this.selectedCategoryKind = kind_value;
        }

        /**
         * @brief OnClickEvent.
         *
         * @param view View Instance.
         */
        @Override
        public void onClick(View view) {
            AccountMasterTableRecord master_record = getInputCategoryInfo();
            if(true == updateMasterRecord(master_record)) {
                displayCompleteMessage();
                notifyComplete();
            } else {
                displayAlertMessage();
            }
        }

        /**
         * @brief Get Category Info.
         */
        private AccountMasterTableRecord getInputCategoryInfo() {
            EditText category_value = (EditText)this.dialogView.findViewById(R.id.category_value);
            AccountMasterTableRecord master_record = new AccountMasterTableRecord();

            master_record.setId(this.editRecord.getPrimaryId());
            master_record.setKindId(convertKindString(this.selectedCategoryKind));
            master_record.setName(Utility.deleteSpace(category_value.getText().toString()));
            master_record.setUseDate(this.editRecord.getUseDate());
            master_record.setUpdateDate(Utility.getCurrentDate());
            master_record.setInsertDate(this.editRecord.getInsertDate());

            return master_record;
        }

        /**
         * @brief Convert Kind Id(String to integer).
         *
         * @param kind_str Kind String.
         *
         * @return kind id(integer).
         */
        private int convertKindString(String kind_string) {
            int kind_id = DatabaseHelper.PAYMENT_FLAG;

            if( true == kind_string.equals(this.activity.getText(R.string.income_label).toString()) ) {
                kind_id = DatabaseHelper.INCOME_FLAG;
            }
            return kind_id;
        }

        /**
         * @brief Insert Master Record into AccountMasterTable.
         *
         * @param record AccountMasterRecord.
         *
         * @return true:success insert false:failed insert.
         */
        private boolean updateMasterRecord(AccountMasterTableRecord record) {
            boolean is_success = false;

            if( true == isValidMasterRecord(record) ) {
                is_success = true;
                this.accountMasterAccessor.update(record);
            }
            return is_success;
        }

        /**
         * @brief Check Valid Master Record.
         *
         * @param record AccountMasterRecord Instance.
         *
         * @return true:valid false:invalid.
         */
        private boolean isValidMasterRecord(AccountMasterTableRecord record) {
            if( true == this.accountMasterAccessor.isExsitRecordMatchName(record.getName()) ||
                true == Utility.isStringNULL(record.getName()) ) {
                return false;
            }
            return true;
        }

        /**
         * @brief Display Complete Message.
         */
        private void displayCompleteMessage() {
            String message = this.activity.getText(R.string.modify_category_message).toString();
            Toast.makeText(this.activity, message, Toast.LENGTH_SHORT).show();
        }

        /**
         * @brief Display Alert Message.
         */
        private void displayAlertMessage() {
            String message = this.activity.getText(R.string.add_category_alert_message).toString();
            Toast.makeText(this.activity, message, Toast.LENGTH_SHORT).show();
        }

        /**
         * @brief Notify Complete Event.
         */
        private void notifyComplete() {
            if(null != this.observer) this.observer.notifyAccountMasterEditComplete();
        }
    }

    /**
     * @brief Delete AccountRecord Event.
     */
    private class AccountRecordDeleteEvent implements View.OnClickListener {

        private Activity activity;
        private EditAccountMasterRecord editRecord;
        private EventCompleteObserver observer;
        private AccountMasterTableAccessor accountMasterAccessor;

        /**
         * @brief Constructor.
         */
        public AccountRecordDeleteEvent(Activity activity, EditAccountMasterRecord edit_record) {
            this.observer = null;
            this.activity = activity;
            this.editRecord = edit_record;
            this.accountMasterAccessor = new AccountMasterTableAccessor(new DatabaseHelper(this.activity.getApplicationContext()));
        }

        /**
         * @brief Attach Observer.
         *
         * @param observer Observer Instance.
         */
        public void attachObserver(EventCompleteObserver observer) {
            this.observer = observer;
        }

        /**
         * @brief OnClickEvent.
         *
         * @param view View Instance.
         */
        @Override
        public void onClick(View view) {
            displayConfirmDialog();
        }

        /**
         * @brief Confirm Dialog.
         */
        private void displayConfirmDialog() {
            // Create Dialog.
            AlertDialog.Builder confirm_dialog = new AlertDialog.Builder(this.activity);
            confirm_dialog.setTitle(this.activity.getText(R.string.delete_confirm_msg).toString());

            // delete OK.
            confirm_dialog.setPositiveButton(
                    this.activity.getText(R.string.delete_confirm_yes).toString(),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteAccountMaster();
                            displayCompleteMessage();
                            notifyComplete();
                        }
                    });

            // delete NO.
            confirm_dialog.setNegativeButton(
                    this.activity.getText(R.string.delete_confirm_no).toString(),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {}
                    });
            confirm_dialog.show();
        }
        /**
         * @brief Delete AccountMasterRecord.
         */
        private void deleteAccountMaster() {
            this.accountMasterAccessor.delete(this.editRecord.getPrimaryId());
        }

        /**
         * @brief Display Complete Message.
         */
        private void displayCompleteMessage() {
            String message = this.activity.getText(R.string.delete_msg).toString();
            Toast.makeText(this.activity, message, Toast.LENGTH_SHORT).show();
        }

        /**
         * @brief Notify Complete Event.
         */
        private void notifyComplete() {
            if(null != this.observer) this.observer.notifyAccountMasterEditComplete();
        }
    }
}

