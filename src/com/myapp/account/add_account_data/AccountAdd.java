package com.myapp.account.add_account_data;

import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.widget.EditText;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.Toast;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import com.myapp.account.R;
import com.myapp.account.utility.Utility;
import com.myapp.account.database.DatabaseHelper;
import com.myapp.account.dialog.AbstractDialog;
import com.myapp.account.database.AccountTableAccessor;
import com.myapp.account.database.AccountTableRecord;
import com.myapp.account.database.AccountMasterTableAccessor;
import com.myapp.account.database.AccountMasterTableRecord;

/**
 * Add Account Date Class.
 */
public class AccountAdd {

    protected Activity activity;
    protected CategoryItems categoryItems;
    protected AccountTableAccessor accountTable;
    protected AccountMasterTableAccessor masterTable;

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
    public void appear() {
        registEvent();
        setCurrentDateToInputDateArea();
    }

    /**
     * Rejist Event
     */
    protected void registEvent() {
        ImageButton input_date_btn = (ImageButton) activity.findViewById(R.id.input_date_select_btn);
        input_date_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        appearInputDateBox();
                    }
                });
        ImageButton category_btn = (ImageButton) activity.findViewById(R.id.category_select_btn);
        category_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        appearCheckbox();
                    }
                });
        Button regist_btn = (Button) activity.findViewById(R.id.regist_btn);
        regist_btn.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        addAccountRecord();
                    }
                });
        ImageButton money_btn = (ImageButton) activity.findViewById(R.id.money_btn);
        money_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        appearCalculator();
                    }
                });
    }

    /**
     * Set Current Date To Input Date Area.
     */
    protected void setCurrentDateToInputDateArea() {
        EditText input_date= (EditText) activity.findViewById(R.id.input_date_value);
        input_date.setText(Utility.getCurrentDate());
    }

    /**
     * Appear Input-date dialog.
     */
    protected void appearInputDateBox() {
        String current_date = Utility.getCurrentDate();

        DatePickerDialog dialog = new DatePickerDialog(activity,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker picker, int year, int monthOfYear, int dayOfMonth) {
                        EditText input_date= (EditText) activity.findViewById(R.id.input_date_value);
                        input_date.setText(Utility.CreateDateFormat(year, monthOfYear, dayOfMonth));
                    }
                },
                Integer.valueOf(current_date.substring(Utility.DATE_YEAR_ST_POS, Utility.DATE_YEAR_ST_POS + Utility.DATE_YEAR_SIZE)),
                Integer.valueOf(current_date.substring(Utility.DATE_MONTH_ST_POS, Utility.DATE_MONTH_ST_POS + Utility.DATE_MONTH_SIZE)) - 1,
                Integer.valueOf(current_date.substring(Utility.DATE_DAY_ST_POS, Utility.DATE_DAY_ST_POS + Utility.DATE_DAY_SIZE)) );

        dialog.show();
    }

    /**
     * Appear Checkbox.
     */
    protected void appearCheckbox() {
        categoryItems.createDialog(activity);
    }

    /**
     * Add AccountRecord into AccountTable.
     */
    protected void addAccountRecord() {
        try {
            AccountTableRecord record = getInputUserAccountInfo();

            if( isEnableInputData(record) ) {
                insertIntoDatabase(record);
                updateUseDateOfMaterTable(record.getCategoryId());
                displayAddCompleteMessage();
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
        EditText input_date= (EditText) activity.findViewById(R.id.input_date_value);
        EditText edit_category= (EditText) activity.findViewById(R.id.category_value);
        EditText edit_money = (EditText)activity. findViewById(R.id.money_value);
        EditText edit_memo = (EditText) activity.findViewById(R.id.memo_value);

        AccountMasterTableRecord master_record = masterTable.getRecordMatchName( edit_category.getText().toString() );

        AccountTableRecord record = new AccountTableRecord();
        try {
            record.setInsertDate(input_date.getText().toString());
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
    private boolean isEnableInputData(AccountTableRecord input_data) {
        if( 0 == input_data.getMoney() ||
            0 == input_data.getCategoryId() ||
            Utility.isStringNULL(input_data.getInsertDate()) ) {
            return false;
        }
        return true;
    }

    /**
     * Insert into Database.
     * @param record User input Account Infomation.
     */
    protected void insertIntoDatabase(AccountTableRecord record) {
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
     * Display Complete Add Accont Message.
     */
    protected void displayAddCompleteMessage() {
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
    private class CategoryItems implements AbstractDialog {

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
                         EditText category = (EditText) activity.findViewById(R.id.category_value);
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


