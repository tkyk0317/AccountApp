package com.myapp.account.edit_account_data;

import android.app.Activity;
import android.view.Window;
import android.content.Context;
import android.view.Gravity;
import android.app.AlertDialog;
import android.widget.Toast;
import android.widget.Button;
import android.view.ViewGroup;
import android.widget.EditText;
import android.view.LayoutInflater;
import com.myapp.account.R;
import com.myapp.account.utility.Utility;
import com.myapp.account.database.AccountTableAccessor;
import com.myapp.account.database.AccountTableRecord;
import com.myapp.account.edit_account_data.AccountAdd;
import com.myapp.account.infoarea.DailyInfoRecord;

/**
 * Edit Account Date Class.
 */
public class AccountEdit extends AccountAdd {

    protected DailyInfoRecord updateRecord;

    /**
     * Constractor.
     */
    public AccountEdit(Activity activity) {
        super(activity);
    }

    /**
     * Appear the Account Add Display.
     */
    @Override
    public void appear(String date) {
        // not supported.
    }

    /**
     * Appear the Account Display.
     * @param record AccoundTableRecord.
     */
    @Override
    public void appear(DailyInfoRecord record) {
        this.updateRecord = record;
        this.insertDate = record.getAccountDate();
        LayoutInflater inflater = LayoutInflater.from(this.activity);
        layout = inflater.inflate(R.layout.account_edit, (ViewGroup)activity.findViewById(R.id.account_edit));
        inputDialog = new AlertDialog.Builder(activity).create();
        inputDialog.setView(layout);
        inputDialog.getWindow().setGravity(Gravity.TOP);
        inputDialog.show();

        // Title Display.
        setTitleArea();
        setButtonTitle();

        // set current data.
        setCurrentData();

        registEvent();
    }

    /**
     * Set Button Title.
     */
    protected void setButtonTitle() {
        Button regist_button = (Button)layout.findViewById(R.id.regist_btn);
        regist_button.setText(activity.getText(R.string.modify_btn_label));
     }

    /**
     * set Current Data.
     */
    protected void setCurrentData() {
        EditText current_category= (EditText)layout.findViewById(R.id.category_value);
        EditText current_money = (EditText)layout. findViewById(R.id.money_value);
        EditText current_memo = (EditText)layout.findViewById(R.id.memo_value);

        // set current data.
        current_category.setText(this.updateRecord.getCategoryName());
        current_money.setText(String.valueOf(this.updateRecord.getAccountRecord().getMoney()));
        current_memo.setText(this.updateRecord.getAccountMemo());
    }

    /**
     * Insert or Update into Database.
     * @param record User input Account Infomation.
     */
    protected void insertOrUpdateIntoDatabase(AccountTableRecord record) {
        record.setId(this.updateRecord.getAccountRecord().getId());
        record.setUpdateDate(Utility.getCurrentDate());

        // update record.
        accountTable.update(record);
    }


    /**
     * Display Complete Message.
     */
    protected void displayCompleteMessage() {
        String message = activity.getText(R.string.modify_msg).toString();
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }
}

