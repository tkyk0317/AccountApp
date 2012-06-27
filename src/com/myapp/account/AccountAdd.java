package com.myapp.account;

import android.view.View;
import android.view.View.OnClickListener;
import android.content.Context;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import com.myapp.account.TitleArea;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.app.AlertDialog;  
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.Toast;
import com.myapp.account.DatabaseHelper;
import com.myapp.account.dialog.AbstractDialog;

/**
 * AccountAdd Class
 */
public class AccountAdd extends Activity
{
    private TitleArea mTitleArea;
    private CategoryCheckbox mCategoryCheckbox;
    private Calculator mCalculator;
    /**
     * @brief called when the activity first created
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_account);
        // initialize.
        init();
        // title_area appear.
        mTitleArea.appear(this);
        registEvent();
    }
    /**
     * @brief initialize
     */
    private void init() {
        mTitleArea = new TitleArea(this);
        mCategoryCheckbox = new CategoryCheckbox(this);
        mCalculator = new Calculator();
    }
    /**
     * @brief Regist Event
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
                    registAccount();
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
     * @brief Appear Checkbox
     */
    private void appearCheckbox() {
        mCategoryCheckbox.createDialog(this);
    }
    /**
     * @brief Regist AccountInfomation.
     */
    private void registAccount() {
        String message = getText(R.string.regist_msg).toString();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    /**
     * @brief Appear Calculator
     */
    private void appearCalculator() {
        mCalculator.createDialog(this);
    }
    /**
     * CheckBox Dialog Class
     */
    private class CategoryCheckbox implements AbstractDialog {
        private DatabaseHelper mDbHelper;
        private SQLiteDatabase mDatabase;
        private String[] mChkItems;
        public CategoryCheckbox(Context context) {
            mDbHelper = new DatabaseHelper(context);
            mDatabase = mDbHelper.getReadableDatabase();
        }
        /**
         * @beirf Create Checkbox
         */
        public void createDialog(Context context) {
            // get AccountCategory.
            int i = 0;
            Cursor cur = mDatabase.rawQuery("select name from AccountMaster",null);
            mChkItems = new String[cur.getCount()];
            while ( cur.moveToNext() ) {
                mChkItems[i++] = cur.getString(0);
            }
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
    }
    /**
     * Calculator Class
     */
    private class Calculator implements AbstractDialog {
        /**
         * Create Calculator Dialog
         */
        public void createDialog(Context context) {
        }
    };

}
