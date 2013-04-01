package com.myapp.account.edit_account_data;

import java.util.List;

import android.util.Log;
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
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.myapp.account.R;
import com.myapp.account.utility.Utility;
import com.myapp.account.database.DatabaseHelper;
import com.myapp.account.database.AccountTableAccessor;
import com.myapp.account.database.AccountTableRecord;
import com.myapp.account.database.AccountMasterTableAccessor;
import com.myapp.account.database.AccountMasterTableRecord;
import com.myapp.account.infoarea.DailyInfoRecord;
import com.myapp.account.observer.EventCompleteObserver;

/**
 * @brief Add Account Date Class.
 */
public class AccountAdd implements OnItemSelectedListener {

    protected Activity activity;
    protected AlertDialog inputDialog;
    protected AccountTableAccessor accountTable;
    protected AccountMasterTableAccessor masterTable;
    protected View layout;
    protected String insertDate;
    protected EventCompleteObserver observer;
    protected Spinner categorySpinner;
    protected String selectedCategoryItem;
    protected String[] categoryItems;

    /**
     * @brief Constractor.
     */
    public AccountAdd(Activity activity) {
        this.activity = activity;
        accountTable = new AccountTableAccessor( new DatabaseHelper(activity.getApplicationContext()) );
        masterTable = new AccountMasterTableAccessor( new DatabaseHelper(activity.getApplicationContext()) );
    }

    /**
     * @brief Attach Observer.
     * @param observer EventCompleteObserver Instance.
     */
    public void attachObserver(EventCompleteObserver observer) {
        this.observer = observer;
    }

    /**
     * @brief Appear the Account Add Display.
     */
    public void appear(String date) {
        this.insertDate = date;

        // initialize.
        initialize();
   }

    /**
     * @brief Appear the Account Display.
     * @param record AccoundTableRecord.
     */
    public void appear(DailyInfoRecord record) {
        // not supported.
    }

    /**
     * @brief Initialize.
     */
    protected void initialize() {
        createDialog();
        createCategoryItems();
        createSpinner();
        setTitleArea();
        setButtonTitle();
        registEvent();
     }

    /**
     * @brief Create Alert Dialog.
     */
    protected void createDialog() {
        LayoutInflater inflater = LayoutInflater.from(this.activity);
        layout = inflater.inflate(R.layout.edit_account_record, (ViewGroup)activity.findViewById(R.id.edit_account_record));
        inputDialog = new AlertDialog.Builder(activity).create();
        inputDialog.setView(layout);
        inputDialog.getWindow().setGravity(Gravity.TOP);
        inputDialog.show();
    }

    /**
     * @brief Create Category Spinner.
     */
    protected void createSpinner() {
       categorySpinner = (Spinner)layout.findViewById(R.id.category_spinner);
       ArrayAdapter<String> adapter =
           new ArrayAdapter(activity, android.R.layout.simple_spinner_item, categoryItems);
       adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       categorySpinner.setAdapter(adapter);

       // set event listener.
       categorySpinner.setOnItemSelectedListener(this);
    }

    /**
     * @brief Create Category Items.
     */
    protected void createCategoryItems() {
        List<AccountMasterTableRecord> record = masterTable.getAll();
        categoryItems = new String[ record.size() ];

        for( int i = 0 ; i < record.size() ; i++ ) {
            categoryItems[i] = record.get(i).getName();
        }
    }

    /**
     * @brief Set Title Area.
     */
    protected void setTitleArea() {
        TextView title= (TextView)layout.findViewById(R.id.date_title);
        title.setText(this.insertDate);
   }

    /**
     * @brief Set Button Title.
     */
    protected void setButtonTitle() {
        Button regist_button = (Button)layout.findViewById(R.id.regist_btn);
        regist_button.setText(activity.getText(R.string.regist_btn_label));
     }

    /**
     * @brief Rejist Event
     */
    protected void registEvent() {
        Button regist_btn = (Button)layout.findViewById(R.id.regist_btn);
        regist_btn.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        insertOrUpdateAccountRecord();
                    }
                });
    }

    /**
     * @brief Insert or Update AccountRecord into AccountTable.
     */
    protected void insertOrUpdateAccountRecord() {
        try {
            AccountTableRecord record = getInputUserAccountInfo();

            if( isEnableInputData(record) ) {
                insertOrUpdateIntoDatabase(record);
                updateUseDateOfMaterTable(record.getCategoryId());
                observer.notifyAccountEditComplete();
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
     * @brief Get Account Information from user's input information.
     */
    protected AccountTableRecord getInputUserAccountInfo()  throws NumberFormatException {
        EditText edit_money = (EditText)layout.findViewById(R.id.money_value);
        EditText edit_memo = (EditText)layout.findViewById(R.id.memo_value);

        AccountMasterTableRecord master_record = masterTable.getRecordMatchName( selectedCategoryItem );

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
     * @brief Check User Input Data is Enable.
     * @return true if user input data is enable data.
     */
    protected boolean isEnableInputData(AccountTableRecord input_data) {
        if( 0 == input_data.getMoney() ||
            true == Utility.isStringNULL(selectedCategoryItem) ||
            true == Utility.isStringNULL(input_data.getInsertDate()) ) {
            return false;
        }
        return true;
    }

    /**
     * @brief Insert or Update into Database.
     * @param record User input Account Infomation.
     */
    protected void insertOrUpdateIntoDatabase(AccountTableRecord record) {
        accountTable.insert(record);
    }

    /**
     * @brief Update Use Date of Master Table.
     * @param Key of master Table.
     */
    protected void updateUseDateOfMaterTable(int key) {
        AccountMasterTableRecord master_record = masterTable.getRecord(key);
        masterTable.update(master_record);
    }

    /**
     * @brief Close Input Dialog.
     */
    protected void closeInputDialog() {
        this.inputDialog.dismiss();
    }

    /**
     * @brief Display Complete Message.
     */
    protected void displayCompleteMessage() {
        String message = activity.getText(R.string.regist_msg).toString();
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * @brief Display Alert Message.
     */
    protected void displayInputDataAlertMessage() {
        String message = activity.getText(R.string.regist_alert_msg).toString();
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * @brief Select item on Spinner.
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id ) {
        Spinner spinner = (Spinner)parent;
        selectedCategoryItem = (String)spinner.getSelectedItem().toString();
    }

    /**
     * @brief Non Selected item on Spinner.
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}


