package com.myapp.account.dialog;

import android.util.Log;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.myapp.account.R;
import com.myapp.account.utility.Utility;
import com.myapp.account.dialog.DialogInterface;
import com.myapp.account.observer.EventCompleteObserver;
import com.myapp.account.database.DatabaseHelper;
import com.myapp.account.database.AccountMasterTableAccessor;
import com.myapp.account.database.AccountMasterTableRecord;

/**
 * @brief AddAccountMAsterRecord Dialog Class.
 */
public class AddAccountMasterDialogImpl implements OnItemSelectedListener, DialogInterface, EventCompleteObserver {

    private Activity activity;
    private View addCategoryView;
    private AlertDialog addCategoryDialog;
    private AddAccountRecordCommitEvent commitEvent;
    private Spinner kindSpinner;
    private EventCompleteObserver observer;

    /**
     * @brief Constractor.
     *
     * @param activity Activity Instance.
     */
    public AddAccountMasterDialogImpl(Activity activity) {
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
     */
    @Override
    public void appear() {
        init();
        displayTitle();
        displayDialog();
    }

    /**
     * @brief Initalize.
     */
    private void init() {
        LayoutInflater inflater = LayoutInflater.from(this.activity);
        this.addCategoryView = inflater.inflate(R.layout.edit_account_master_dialog,
                                                 (ViewGroup)this.activity.findViewById(R.id.edit_account_master_dialog));
        this.kindSpinner = (Spinner)this.addCategoryView.findViewById(R.id.kind_spinner);

        // init event instance.
        this.commitEvent = new AddAccountRecordCommitEvent(this.activity, this.addCategoryView);
        this.commitEvent.attachObserver(this);

        // regist event.
        registEvent();
    }

    /**
     * @brief Regist Event.
     */
    private void registEvent() {
        Button commit_button = (Button)this.addCategoryView.findViewById(R.id.regist_btn);
        commit_button.setOnClickListener(this.commitEvent);
        this.kindSpinner.setOnItemSelectedListener(this);
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
    }

    /**
     * @brief Select item on Spinner.
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id ) {
        Spinner spinner = (Spinner)parent;
        this.commitEvent.notifySelectedSpinner((String)spinner.getSelectedItem().toString());
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

    /**
     * @brief Commit AccountRecord Event.
     */
    private class AddAccountRecordCommitEvent implements View.OnClickListener {

        private Activity activity;
        private View dialogView;
        private String selectedCategoryKind;
        private EventCompleteObserver observer;
        private AccountMasterTableAccessor accountMasterAccessor;

        /**
         * @brief Constractor.
         */
        public AddAccountRecordCommitEvent(Activity activity, View dialog_view) {
            this.observer = null;
            this.activity = activity;
            this.dialogView = dialog_view;
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
            if(true == insertMasterRecord(master_record)) {
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

            master_record.setKindId(convertKindString(this.selectedCategoryKind));
            master_record.setName(Utility.deleteSpace(category_value.getText().toString()));

            return master_record;
        }

        /**
         * @brief Convert Kind Id(String to int).
         *
         * @param kind_str Kind String.
         *
         * @return kind id(int).
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
        private boolean insertMasterRecord(AccountMasterTableRecord record) {
            boolean is_success = false;

            if( true == isValidMasterRecord(record) ) {
                is_success = true;
                this.accountMasterAccessor.insert(record);
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
            String message = this.activity.getText(R.string.add_category_message).toString();
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
}

