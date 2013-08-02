package com.myapp.account.dialog;

import java.util.List;

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
import android.widget.LinearLayout;
import android.content.DialogInterface;

import com.myapp.account.R;
import com.myapp.account.utility.Utility;
import com.myapp.account.dialog.UserTableDialogInterface;
import com.myapp.account.observer.EventCompleteObserver;
import com.myapp.account.database.DatabaseHelper;
import com.myapp.account.edit_user_table.EditUserTableRecord;
import com.myapp.account.database.UserTableAccessor;
import com.myapp.account.database.UserTableRecord;
import com.myapp.account.config.AppConfigurationData;
import com.myapp.account.database.AccountTableAccessor;
import com.myapp.account.database.AccountTableRecord;
import com.myapp.account.database.EstimateTableRecord;
import com.myapp.account.database.EstimateTableAccessor;

/**
 * @brief EditUserTable Dialog Class.
 */
public class EditUserTableDialogImpl implements UserTableDialogInterface, EventCompleteObserver {

    private Activity activity = null;
    private View editUserTableView = null;
    private AlertDialog editUserTableDialog = null;
    private UserTableModifyEvent modifyEvent = null;
    private UserTableDeleteEvent deleteEvent = null;
    private Button modifyButton = null;
    private Button deleteButton = null;
    private EventCompleteObserver observer = null;
    private EditUserTableRecord editRecord = null;
    private static final int BUTTON_WIDTH = 100;

    /**
     * @brief Constructor.
     *
     * @param activity Activity Instance.
     */
    public EditUserTableDialogImpl(Activity activity) {
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
    public void appear(EditUserTableRecord table_row) {
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
        this.editUserTableView = inflater.inflate(R.layout.edit_user_table_dialog,
                                                 (ViewGroup)this.activity.findViewById(R.id.edit_user_table_dialog));

        // initialize button.
        initButton();

        // initialize event instance.
        this.modifyEvent = new UserTableModifyEvent(this.activity, this.editUserTableView, this.editRecord);
        this.deleteEvent = new UserTableDeleteEvent(this.activity, this.editRecord);
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
        this.modifyButton = (Button)this.editUserTableView.findViewById(R.id.regist_btn);
        this.modifyButton.setText(this.activity.getText(R.string.modify_btn_label).toString());

        // add delete button.
        this.deleteButton = new Button(this.activity.getApplicationContext());
        this.deleteButton.setWidth(BUTTON_WIDTH);
        this.deleteButton.setText(this.activity.getText(R.string.delete_btn_label).toString());

        // check enable delete button.
        if( false == isEnableDeleteRecord() ) {
            this.deleteButton.setEnabled(false);
            this.deleteButton.setClickable(false);
            displayRepletionMessage();
        }

        LinearLayout layout = (LinearLayout)this.editUserTableView.findViewById(R.id.commit_btn_area);
        layout.addView(this.deleteButton);
    }

    /**
     * @brief Check Enable Delete Record.
     *
     * @return true:enable delete false:unenable delete.
     */
    private boolean isEnableDeleteRecord() {
        // not delete default user.
        if( this.editRecord.getPrimaryId() == 1 ) {
            return false;
        }
        return true;
    }

    /**
     * @brief Display Repletion.
     */
    private void displayRepletionMessage() {
        String message = this.activity.getText(R.string.del_user_repletion_message).toString();
        Toast.makeText(this.activity, message, Toast.LENGTH_SHORT).show();
    }


    /**
     * @brief Register Event.
     */
    private void registEvent() {
        this.modifyButton.setOnClickListener(this.modifyEvent);
        this.deleteButton.setOnClickListener(this.deleteEvent);
    }

    /**
     * @brief Set Current Data.
     */
    private void setCurrentData() {
        EditText user_name_value = (EditText)this.editUserTableView.findViewById(R.id.user_name_value);
        EditText user_memo_value = (EditText)this.editUserTableView.findViewById(R.id.user_memo_value);

        user_name_value.setText(this.editRecord.getName());
        user_memo_value.setText(this.editRecord.getMemo());
    }

    /**
     * @brief Display Dialog Title.
     */
    private void displayTitle() {
        TextView title = (TextView)this.editUserTableView.findViewById(R.id.title);
        title.setText(this.activity.getText(R.string.edit_user_dialog_title).toString());
    }

    /**
     * @brief Display Dialog.
     */
    private void displayDialog() {
        this.editUserTableDialog = new AlertDialog.Builder(this.activity).create();
        this.editUserTableDialog.setView(this.editUserTableView);
        this.editUserTableDialog.getWindow().setGravity(Gravity.TOP);
        this.editUserTableDialog.show();

        // modify dialog width.
        Utility.modifyDialogWidthMax(this.editUserTableDialog);
    }

    /**
     * @brief Edit UserTable Complete.
     *
     * @return
     */
    @Override
    public void notifyUserTableEditComplete() {
        if(null != this.observer) this.observer.notifyUserTableEditComplete();
        this.editUserTableDialog.dismiss();
    }

    // not support.
    @Override
    public void notifyAccountMasterEditComplete() {}
    @Override
    public void notifyAccountEditComplete() {}

    /**
     * @brief Modify EditUserTable Modify Event.
     */
    private class UserTableModifyEvent implements View.OnClickListener {

        private Activity activity = null;
        private View dialogView = null;
        private EditUserTableRecord editRecord = null;
        private EventCompleteObserver observer = null;
        private UserTableAccessor userTable = null;

        /**
         * @brief Constructor.
         */
        public UserTableModifyEvent(Activity activity, View dialog_view, EditUserTableRecord edit_record) {
            this.observer = null;
            this.activity = activity;
            this.dialogView = dialog_view;
            this.editRecord = edit_record;
            this.userTable = new UserTableAccessor(new DatabaseHelper(this.activity.getApplicationContext()));
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
            UserTableRecord user_record = getInputUserInfo();
            if(true == updateUserRecord(user_record)) {
                displayCompleteMessage();
                notifyComplete();
            } else {
                displayAlertMessage();
            }
        }

        /**
         * @brief Get Category Info.
         */
        private UserTableRecord getInputUserInfo() {
            EditText user_name_value = (EditText)this.dialogView.findViewById(R.id.user_name_value);
            EditText user_memo_value = (EditText)this.dialogView.findViewById(R.id.user_memo_value);
            UserTableRecord user_record = new UserTableRecord();

            user_record.setId(this.editRecord.getPrimaryId());
            user_record.setName(Utility.deleteSpace(user_name_value.getText().toString()));
            user_record.setMemo(user_memo_value.getText().toString());
            user_record.setUpdateDate(this.editRecord.getUpdateDate());
            user_record.setInsertDate(this.editRecord.getInsertDate());

            return user_record;
        }

        /**
         * @brief update User Record into UserTable.
         *
         * @param record UserTableRecord Instance.
         *
         * @return true:success insert false:failed insert.
         */
        private boolean updateUserRecord(UserTableRecord record) {
            boolean is_success = false;

            if( true == isValidUserRecord(record) ) {
                is_success = true;
                this.userTable.update(record);
            }
            return is_success;
        }

        /**
         * @brief Check Valid User Record.
         *
         * @param record UserTableRecord Instance.
         *
         * @return true:valid false:invalid.
         */
        private boolean isValidUserRecord(UserTableRecord record) {
            if( true == Utility.isStringNULL(record.getName()) ) {
                return false;
            }
            return true;
        }

        /**
         * @brief Display Complete Message.
         */
        private void displayCompleteMessage() {
            String message = this.activity.getText(R.string.modify_user_message).toString();
            Toast.makeText(this.activity, message, Toast.LENGTH_SHORT).show();
        }

        /**
         * @brief Display Alert Message.
         */
        private void displayAlertMessage() {
            String message = this.activity.getText(R.string.add_user_alert_message).toString();
            Toast.makeText(this.activity, message, Toast.LENGTH_SHORT).show();
        }

        /**
         * @brief Notify Complete Event.
         */
        private void notifyComplete() {
            if(null != this.observer) this.observer.notifyUserTableEditComplete();
        }
    }

    /**
     * @brief Delete UserTableRecord Event.
     */
    private class UserTableDeleteEvent implements View.OnClickListener {

        private Activity activity = null;
        private EditUserTableRecord editRecord = null;
        private EventCompleteObserver observer = null;
        private UserTableAccessor userTable = null;
        private AppConfigurationData appConfig = null;

        /**
         * @brief Constructor.
         */
        public UserTableDeleteEvent(Activity activity, EditUserTableRecord edit_record) {
            this.observer = null;
            this.activity = activity;
            this.editRecord = edit_record;
            this.appConfig = new AppConfigurationData(this.activity);
            this.userTable = new UserTableAccessor(new DatabaseHelper(this.activity.getApplicationContext()));
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
            confirm_dialog.setTitle(this.activity.getText(R.string.delete_user_confirm_msg).toString());

            // delete OK.
            confirm_dialog.setPositiveButton(
                    this.activity.getText(R.string.delete_confirm_yes).toString(),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteUserRecord();
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
         * @brief Delete UserTableRecord.
         */
        private void deleteUserRecord() {
            deleteAccountData(this.editRecord.getPrimaryId());
            deleteEstimateData(this.editRecord.getPrimaryId());
            this.userTable.delete(this.editRecord.getPrimaryId());
            this.appConfig.saveUserNameId(AppConfigurationData.USER_NAME_ID_DEFAULT);
        }

        /**
         * @brief delete account data.
         *
         * @param user_id user_id.
         */
        private void deleteAccountData(int user_id) {
            AccountTableAccessor account_table = new AccountTableAccessor(new DatabaseHelper(this.activity.getApplicationContext()), this.appConfig);
            List<AccountTableRecord> account_records = account_table.getAllRecordSpecifiedUserId(this.editRecord.getPrimaryId());

            for( AccountTableRecord record : account_records ) {
                account_table.delete(record.getId());
            }
        }

        /**
         * @brief delete estimate data.
         *
         * @param user_id specified user_id.
         */
        private void deleteEstimateData(int user_id) {
            EstimateTableAccessor estimate_table = new EstimateTableAccessor(new DatabaseHelper(this.activity.getApplicationContext()), this.appConfig);
            List<EstimateTableRecord> estimate_records = estimate_table.getAllRecordSpecifiedUserId(this.editRecord.getPrimaryId());

            for( EstimateTableRecord record : estimate_records ) {
                estimate_table.delete(record.getId());
            }
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
            if(null != this.observer) this.observer.notifyUserTableEditComplete();
        }
    }
}

