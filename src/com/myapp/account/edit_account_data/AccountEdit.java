package com.myapp.account.edit_account_data;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.app.AlertDialog;
import android.content.DialogInterface;
import com.myapp.account.R;
import com.myapp.account.utility.Utility;
import com.myapp.account.database.AccountTableRecord;
import com.myapp.account.edit_account_data.AccountAdd;
import com.myapp.account.infoarea.DailyInfoRecord;

/**
 * @brief Edit Account Date Class.
 */
public class AccountEdit extends AccountAdd {

    protected DailyInfoRecord updateRecord;
    protected static final int BUTTON_WIDTH = 100;

    /**
     * @brief Constractor.
     */
    public AccountEdit(Activity activity) {
        super(activity);
    }

    /**
     * @brief Appear the Account Add Display.
     */
    @Override
    public void appear(String date) {
        // not supported.
    }

    /**
     * @brief Appear the Account Display.
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
        // Add Delete Button.
        addDeleteButton();
    }

    /**
     * @brief Add Delete Button.
     */
    protected void addDeleteButton() {
        Button delete_button = new Button(activity);
        delete_button.setWidth(BUTTON_WIDTH);
        delete_button.setText(R.string.delete_btn_label);

        LinearLayout linear_layout = (LinearLayout)layout.findViewById(R.id.commit_btn_area);
        linear_layout.addView(delete_button);

        // set event listener.
        delete_button.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        // Create Dialog.
                        AlertDialog.Builder confirm_dialog = new AlertDialog.Builder(activity);
                        confirm_dialog.setTitle(R.string.delete_confirm_msg);

                        // delete OK.
                        confirm_dialog.setPositiveButton(
                            R.string.delete_confirm_yes,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteAccountRecord();
                                    observer.notifyAccountEditComplete();
                                    closeInputDialog();
                                    displayDeleteCompleteMessage();
                                }
                        });

                        // delete NO.
                        confirm_dialog.setNegativeButton(
                            R.string.delete_confirm_no,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                        confirm_dialog.show();
                   }
                });
    }

    /**
     * @brief Delete Current Record.
     */
    protected void deleteAccountRecord() {
        accountTable.delete(updateRecord.getAccountRecord().getId());
    }

    /**
     * @brief Set Button Title.
     */
    @Override
    protected void setButtonTitle() {
        Button regist_button = (Button)layout.findViewById(R.id.regist_btn);
        regist_button.setText(activity.getText(R.string.modify_btn_label));
     }

    /**
     * @brief set Current Data.
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
     * @brief Get Category Position in Spinner.
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
     * @brief Insert or Update into Database.
     * @param record User input Account Infomation.
     */
    protected void insertOrUpdateIntoDatabase(AccountTableRecord record) {
        record.setId(this.updateRecord.getAccountRecord().getId());
        record.setUpdateDate(Utility.getCurrentDate());

        // update record.
        accountTable.update(record);
    }

    /**
     * @brief Display Complete Message.
     */
    protected void displayCompleteMessage() {
        String message = activity.getText(R.string.modify_msg).toString();
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * @brief Display Delete Complete Message.
     */
    protected void displayDeleteCompleteMessage() {
        String message = activity.getText(R.string.delete_msg).toString();
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }
}

