package com.myapp.account.edit_account_data;

import java.util.List;
import android.app.Activity;
import android.view.Window;
import android.content.Context;
import android.view.Gravity;
import android.widget.EditText;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import com.myapp.account.R;
import com.myapp.account.utility.Utility;
import com.myapp.account.database.DatabaseHelper;
import com.myapp.account.dialog.AbstractDialog;
import com.myapp.account.database.AccountTableAccessor;
import com.myapp.account.database.AccountTableRecord;
import com.myapp.account.database.AccountMasterTableAccessor;
import com.myapp.account.database.AccountMasterTableRecord;
import com.myapp.account.infoarea.DailyInfoRecord;

/**
 * Add Account Date Class.
 */
public class AccountAdd {

    protected Activity activity;
    protected AlertDialog inputDialog;
    protected CategoryItems categoryItems;
    protected AccountTableAccessor accountTable;
    protected AccountMasterTableAccessor masterTable;
    protected View layout;
    protected String insertDate;

    /**
     * Constractor.
     */
    public AccountAdd(Activity activity) {
        this.activity = activity;
        categoryItems = new CategoryItems(activity);
        accountTable = new AccountTableAccessor( new DatabaseHelper(activity.getApplicationContext()) );
        masterTable = new AccountMasterTableAccessor( new DatabaseHelper(activity.getApplicationContext()) );
    }

    /**
     * Appear the Account Add Display.
     */
    public void appear(String date) {
        this.insertDate = date;
        LayoutInflater inflater = LayoutInflater.from(this.activity);
        layout = inflater.inflate(R.layout.account_edit, (ViewGroup)activity.findViewById(R.id.account_edit));
        inputDialog = new AlertDialog.Builder(activity).create();
        inputDialog.setView(layout);
        inputDialog.getWindow().setGravity(Gravity.TOP);
        inputDialog.show();

        // Title Display.
        setTitleArea();
        setButtonTitle();
        registEvent();
    }

    /**
     * Appear the Account Display.
     * @param record AccoundTableRecord.
     */
    public void appear(DailyInfoRecord record) {
        // not supported.
    }

    /**
     * Set Title Area.
     */
    protected void setTitleArea() {
        TextView title= (TextView)layout.findViewById(R.id.date_title);
        title.setText(this.insertDate);
   }

    /**
     * Set Button Title.
     */
    protected void setButtonTitle() {
        Button regist_button = (Button)layout.findViewById(R.id.regist_btn);
        regist_button.setText(activity.getText(R.string.regist_btn_label));
     }

    /**
     * Rejist Event
     */
    protected void registEvent() {
        ImageButton category_btn = (ImageButton)layout.findViewById(R.id.category_select_btn);
        category_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        appearCheckbox();
                    }
                });
        Button regist_btn = (Button)layout.findViewById(R.id.regist_btn);
        regist_btn.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        insertOrUpdateAccountRecord();
                    }
                });
        ImageButton money_btn = (ImageButton)layout.findViewById(R.id.money_btn);
        money_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        appearCalculator();
                    }
                });
    }

    /**
     * Appear Checkbox.
     */
    protected void appearCheckbox() {
        categoryItems.createDialog(activity);
    }

    /**
     * Insert or Update AccountRecord into AccountTable.
     */
    protected void insertOrUpdateAccountRecord() {
        try {
            AccountTableRecord record = getInputUserAccountInfo();

            if( isEnableInputData(record) ) {
                insertOrUpdateIntoDatabase(record);
                updateUseDateOfMaterTable(record.getCategoryId());
                closeInputDialog();
                displayCompleteMessage();
            } else {
                displayInputDataAlertMessage();
            }
        } catch (NumberFormatException exception) {
            displayInputDataAlertMessage();
        }
    }

    /**
     * Get Account Information from user's input information.
     */
    protected AccountTableRecord getInputUserAccountInfo()  throws NumberFormatException {
        EditText edit_category= (EditText)layout.findViewById(R.id.category_value);
        EditText edit_money = (EditText)layout. findViewById(R.id.money_value);
        EditText edit_memo = (EditText)layout.findViewById(R.id.memo_value);

        AccountMasterTableRecord master_record = masterTable.getRecordMatchName( edit_category.getText().toString() );

        AccountTableRecord record = new AccountTableRecord();
        try {
            record.setInsertDate(this.insertDate);
            record.setCategoryId(master_record.getId());
            record.setMoney(Integer.valueOf(edit_money.getText().toString()));
            record.setMemo(edit_memo.getText().toString());
        } catch (NumberFormatException exception) {
            throw new NumberFormatException();
        }
        return record;
    }

    /**
     * Check User Input Data is Enable.
     * @return true if user input data is enable data.
     */
    protected boolean isEnableInputData(AccountTableRecord input_data) {
        if( 0 == input_data.getMoney() ||
            0 == input_data.getCategoryId() ||
            Utility.isStringNULL(input_data.getInsertDate()) ) {
            return false;
        }
        return true;
    }

    /**
     * Insert or Update into Database.
     * @param record User input Account Infomation.
     */
    protected void insertOrUpdateIntoDatabase(AccountTableRecord record) {
        accountTable.insert(record);
    }

    /**
     * Update Use Date of Master Table.
     * @param Key of master Table.
     */
    protected void updateUseDateOfMaterTable(int key) {
        AccountMasterTableRecord master_record = masterTable.getRecord(key);

        masterTable.update(master_record);
    }

    /**
     * Close Input Dialog.
     */
    protected void closeInputDialog() {
        this.inputDialog.dismiss();
    }

    /**
     * Display Complete Message.
     */
    protected void displayCompleteMessage() {
        String message = activity.getText(R.string.regist_msg).toString();
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Display Alert Message.
     */
    protected void displayInputDataAlertMessage() {
        String message = activity.getText(R.string.regist_alert_msg).toString();
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Appear Calculator.
     */
    protected void appearCalculator() {
    }

    /**
     * Account Category Items Class.
     */
    protected class CategoryItems implements AbstractDialog {

        protected String[] checkItems;
        protected AccountMasterTableAccessor masterTable;

        /**
         * Class Constractor.
         */
        public CategoryItems(Context context) {
            masterTable = new AccountMasterTableAccessor( new DatabaseHelper(context) );
        }

        /**
         * Create Checkbox.
         * @param context Context instance.
         */
        public void createDialog(Context context) {
            SetAccountMasterItemToCheckItems();

            // Create Dialog.
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setTitle(R.string.regist_category_label);
            dialog.setItems
                (checkItems,
                 new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int item) {
                         EditText category = (EditText)layout.findViewById(R.id.category_value);
                         category.setText(checkItems[item]);
                     }
                 });
            dialog.show();
        }

        /**
         * Set AccountMaster Item To CheckItem.
         */
        protected void SetAccountMasterItemToCheckItems() {
            List<AccountMasterTableRecord> record = masterTable.getAll();
            checkItems = new String[ record.size() ];

            for( int i = 0 ; i < record.size() ; i++ ) {
                checkItems[i] = record.get(i).getName();
            }
        }
    }
}


