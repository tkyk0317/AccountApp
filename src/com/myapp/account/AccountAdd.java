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
    private TitleArea mTitleArea;
    private CategoryCheckbox mCategoryCheckbox;
    private Calculator mCalculator;
    private DatabaseHelper mDbHelper;

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
        mTitleArea.appear(this);
        registEvent();

        Log.d("AccountAdd", "[END] onCreate");
    }

   /**
     * Called Activity is Destoryed.
     */
   @Override
   public void onDestroy() {
       super.onDestroy();
       mTitleArea = null;
       mCategoryCheckbox = null;
       mCalculator = null;
       mDbHelper = null;
   }

   /**
    * Called User can not see the Activity.
    */
    @Override
    protected void onStop() {
        super.onStop();
        mTitleArea = null;
        mCategoryCheckbox = null;
        mCalculator = null;
        mDbHelper = null;
    }

   /**
    * Initialize Member-Variable.
    */
    private void init() {
        Log.d("AccountAdd", "[START] init");
        mTitleArea = new TitleArea();
        mCategoryCheckbox = new CategoryCheckbox(this);
        mCalculator = new Calculator();
        mDbHelper = new DatabaseHelper(getApplicationContext());
        Log.d("AccountAdd", "[END] init");
    }

    /**
    * Rejist Event
    */
    private void registEvent() {
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
    private void appearCheckbox() {
        mCategoryCheckbox.createDialog(this);
    }

    /**
      * Add AccountRecord into AccountTable.
      */
    private void addAccountRecord() {
        AccountTableRecord record = getInputUserAccountInfo();
        insertIntoDatabase(record);
        printAddCompleteMessage();
    }

    /**
      * Get Account Information from user's input information.
      */
    private AccountTableRecord getInputUserAccountInfo() {
        EditText edit_category= (EditText) findViewById(R.id.category_value);
        EditText edit_money = (EditText) findViewById(R.id.money_value);
        EditText edit_memo = (EditText) findViewById(R.id.memo_value);

        AccountMasterTableAccessImpl master_table = new AccountMasterTableAccessImpl(mDbHelper);
        AccountMasterTableRecord master_record = master_table.getRecordMatchName( edit_category.getText().toString() );

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
    private void insertIntoDatabase(AccountTableRecord record) {
        AccountTableAccessImpl account_table = new AccountTableAccessImpl(mDbHelper);
        account_table.insert(record);
    }

    /**
      * Complete Add Accont Message.
      */
    private void printAddCompleteMessage() {
        String message = getText(R.string.regist_msg).toString();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
      * Appear Calculator.
      */
    private void appearCalculator() {
        mCalculator.createDialog(this);
    }

    /**
     * Account Category CheckBox Class.
     */
    private class CategoryCheckbox implements AbstractDialog {

        private DatabaseHelper mDbHelper;
        private String[] mChkItems;

        /**
          * Class Constractor.
          */
        public CategoryCheckbox(Context context) {
            mDbHelper = new DatabaseHelper(context);
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
                (mChkItems,
                 new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int item) {
                         EditText category = (EditText) findViewById(R.id.category_value);
                         category.setText(mChkItems[item]);
                     }
                 });
            dialog.show();
        }

        /**
          * Set AccountMaster Item To CheckItem.
          */
        private void SetAccountMasterItemToCheckItems() {
            AccountMasterTableAccessImpl master_table = new AccountMasterTableAccessImpl(mDbHelper);
            List<AccountMasterTableRecord> record = master_table.getAll();
            mChkItems = new String[ record.size() ];

            for( int i = 0 ; i < record.size() ; i++ ) {
                mChkItems[i] = record.get(i).getName();
            }
       }
    }

    /**
      * Calculator Class.
      */
    private class Calculator implements AbstractDialog {

        /**
         * Create Calculator Dialog.
         */
        public void createDialog(Context context) {
        }
    };

}
