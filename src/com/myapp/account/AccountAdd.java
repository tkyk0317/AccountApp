package com.myapp.account;

import java.util.*;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Context;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.util.Log;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.Toast;
import com.myapp.account.TitleArea;
import com.myapp.account.database.DatabaseHelper;
import com.myapp.account.dialog.AbstractDialog;
import com.myapp.account.database.AccountTableAccessImpl;
import com.myapp.account.database.AccountTableRecord;
import com.myapp.account.database.AccountMasterTableAccessImpl;
import com.myapp.account.database.AccountMasterTableRecord;

/**
 * AccountAdd Activity Class.
 */
public class AccountAdd extends Activity
{
    protected TitleArea titleArea;
    protected CategoryItems categoryItems;
    protected AccountTableAccessImpl accountTable;
    protected AccountMasterTableAccessImpl masterTable;

    /**
     * Create Activity.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("AccountAdd", "[START] onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_account);
        // initialize.
        init();
        // title_area appear.
        titleArea.appear(this);
        registEvent();

        Log.d("AccountAdd", "[END] onCreate");
    }

    /**
     * Called Activity is Destoryed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        titleArea = null;
        categoryItems = null;
        accountTable = null;
        masterTable = null;
    }

    /**
     * Called User can not see the Activity.
     */
    @Override
    protected void onStop() {
        super.onStop();
        titleArea = null;
        categoryItems = null;
        accountTable = null;
        masterTable = null;
    }

    /**
     * Initialize Member-Variable.
     */
    protected void init() {
        Log.d("AccountAdd", "[START] init");
        titleArea = new TitleArea();
        categoryItems = new CategoryItems(this);
        accountTable = new AccountTableAccessImpl( new DatabaseHelper(getApplicationContext()) );
        masterTable = new AccountMasterTableAccessImpl( new DatabaseHelper(getApplicationContext()) );
        Log.d("AccountAdd", "[END] init");
    }

    /**
     * Rejist Event
     */
    protected void registEvent() {
        ImageButton category_btn = (ImageButton) findViewById(R.id.category_select_btn);
        category_btn.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    appearCheckbox();
                }
            });
        Button regist_btn = (Button) findViewById(R.id.regist_btn);
        regist_btn.setOnClickListener(
            new View.OnClickListener() {
                public void onClick(View v) {
                    addAccountRecord();
                }
            });
        ImageButton money_btn = (ImageButton) findViewById(R.id.money_btn);
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
        categoryItems.createDialog(this);
    }

    /**
     * Add AccountRecord into AccountTable.
     */
    protected void addAccountRecord() {
        AccountTableRecord record = getInputUserAccountInfo();
        insertIntoDatabase(record);
        printAddCompleteMessage();
    }

    /**
     * Get Account Information from user's input information.
     */
    protected AccountTableRecord getInputUserAccountInfo() {
        EditText edit_category= (EditText) findViewById(R.id.category_value);
        EditText edit_money = (EditText) findViewById(R.id.money_value);
        EditText edit_memo = (EditText) findViewById(R.id.memo_value);

        AccountMasterTableRecord master_record = masterTable.getRecordMatchName( edit_category.getText().toString() );

        AccountTableRecord record = new AccountTableRecord();
        record.setCategoryId( master_record.getId() );
        record.setMoney( Integer.valueOf(edit_money.getText().toString()) );
        record.setMemo( edit_memo.getText().toString() );
        return record;
    }

    /**
     * Insert into Database.
     * @param record User input Account Infomation.
     */
    protected void insertIntoDatabase(AccountTableRecord record) {
        accountTable.insert(record);
    }

    /**
     * Complete Add Accont Message.
     */
    protected void printAddCompleteMessage() {
        String message = getText(R.string.regist_msg).toString();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
        protected AccountMasterTableAccessImpl masterTable;

        /**
         * Class Constractor.
         */
        public CategoryItems(Context context) {
            masterTable = new AccountMasterTableAccessImpl( new DatabaseHelper(context) );
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
                         EditText category = (EditText) findViewById(R.id.category_value);
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
