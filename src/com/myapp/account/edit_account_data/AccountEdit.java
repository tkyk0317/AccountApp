package com.myapp.account.edit_account_data;

import android.app.Activity;
import android.util.Log;
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

        // initalize.
        initialize();

        // set current data.
        setCurrentData();
    }

    /**
     * Set Button Title.
     */
    @Override
    protected void setButtonTitle() {
        Button regist_button = (Button)layout.findViewById(R.id.regist_btn);
        regist_button.setText(activity.getText(R.string.modify_btn_label));
     }

    /**
     * set Current Data.
     */
    protected void setCurrentData() {
        EditText current_money = (EditText)layout. findViewById(R.id.money_value);
        EditText current_memo = (EditText)layout.findViewById(R.id.memo_value);

        // set current data.
        categorySpinner.setSelection(getCategoryPositionFromSpinner(updateRecord.getCategoryName()));
        current_money.setText(String.valueOf(this.updateRecord.getAccountRecord().getMoney()));
        current_memo.setText(this.updateRecord.getAccountMemo());
    }

    /**
     * Get Category Position in Spinner.
     */
    protected int getCategoryPositionFromSpinner(String category_name) {
        int pos = 0;
        boolean is_find = false;
        for( pos = 0 ; pos < categoryItems.length ; ++pos ) {
            if( categoryItems[pos].equals(category_name) ) {
                is_find = true;
                break;
            }
        }

        if( true == is_find ) return pos;
        return 0;
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

