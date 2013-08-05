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

import com.myapp.account.R;
import com.myapp.account.utility.Utility;
import com.myapp.account.observer.EventCompleteObserver;
import com.myapp.account.database.DatabaseHelper;
import com.myapp.account.edit_user_table.EditUserTableRecord;
import com.myapp.account.database.UserTableAccessor;
import com.myapp.account.database.UserTableRecord;

/**
 * @brief AddUserTable Dialog Class.
 */
public class AddUserTableDialogImpl implements UserTableDialogInterface, EventCompleteObserver {

    private Activity activity;
    private View addUserTableView;
    private AlertDialog addUserTableDialog;
    private UserTableAddEvent addEvent;
    private Button commitButton;
    private EventCompleteObserver observer;

    /**
     * @brief Constructor.
     *
     * @param activity Activity Instance.
     */
    public AddUserTableDialogImpl(Activity activity) {
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
     * @brief Appear Dialog.
     * @note not supported.
     */
    @Override
    public void appear(EditUserTableRecord table_row) {}

    /**
     * @brief Initialize.
     */
    private void init() {
        LayoutInflater inflater = LayoutInflater.from(this.activity);
        this.addUserTableView = inflater.inflate(R.layout.edit_user_table_dialog,
                                                 (ViewGroup)this.activity.findViewById(R.id.edit_user_table_dialog));

        // initialize button.
        initButton();

        // initialize event instance.
        this.addEvent = new UserTableAddEvent(this.activity, this.addUserTableView);
        this.addEvent.attachObserver(this);

        // register event.
        registEvent();
    }

    /**
     * @brief Initialize Commit Button.
     */
    private void initButton() {
        this.commitButton = (Button)this.addUserTableView.findViewById(R.id.regist_btn);
        this.commitButton.setText(this.activity.getText(R.string.regist_btn_label).toString());
    }

    /**
     * @brief Register Event.
     */
    private void registEvent() {
        this.commitButton.setOnClickListener(this.addEvent);
    }

    /**
     * @brief Display Dialog Title.
     */
    private void displayTitle() {
        TextView title = (TextView)this.addUserTableView.findViewById(R.id.title);
        title.setText(this.activity.getText(R.string.add_user_dialog_title).toString());
    }

    /**
     * @brief Display Dialog.
     */
    private void displayDialog() {
        this.addUserTableDialog= new AlertDialog.Builder(this.activity).create();
        this.addUserTableDialog.setView(this.addUserTableView);
        this.addUserTableDialog.getWindow().setGravity(Gravity.TOP);
        this.addUserTableDialog.show();

        // modify dialog width.
        Utility.modifyDialogWidthMax(this.addUserTableDialog);
    }

    /**
     * @brief UserTable Edited Complete.
     */
    @Override
    public void notifyUserTableEditComplete() {
        if( null != this.observer ) this.observer.notifyUserTableEditComplete();
        this.addUserTableDialog.dismiss();
    }


    // not support.
    @Override
    public void notifyAccountEditComplete() {}
    @Override
    public void notifyAccountMasterEditComplete() {}

    /**
     * @brief Commit UserTableRecord Event.
     */
    private class UserTableAddEvent implements View.OnClickListener {

        private Activity activity;
        private View dialogView;
        private EventCompleteObserver observer;
        private UserTableAccessor userTable;

        /**
         * @brief Constructor.
         */
        public UserTableAddEvent(Activity activity, View dialog_view) {
            this.observer = null;
            this.activity = activity;
            this.dialogView = dialog_view;
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
            UserTableRecord record = getInputUserInfo();
            if(true == insertUserRecord(record)) {
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

            user_record.setName(Utility.deleteSpace(user_name_value.getText().toString()));
            user_record.setMemo(user_memo_value.getText().toString());
            return user_record;
        }

        /**
         * @brief Insert User Record into UserTable.
         *
         * @param record UserTableRecord instance.
         *
         * @return true:success insert false:failed insert.
         */
        private boolean insertUserRecord(UserTableRecord record) {
            boolean is_success = false;

            if( true == isValidUserRecord(record) ) {
                is_success = true;
                this.userTable.insert(record);
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
            if( true == this.userTable.isExsitRecordMatchName(record.getName()) ||
                true == Utility.isStringNULL(record.getName()) ) {
                return false;
            }
            return true;
        }

        /**
         * @brief Display Complete Message.
         */
        private void displayCompleteMessage() {
            String message = this.activity.getText(R.string.add_user_message).toString();
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
}

