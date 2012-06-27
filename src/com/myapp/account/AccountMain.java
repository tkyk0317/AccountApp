package com.myapp.account;

import java.util.*;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener; 
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.ImageButton;
import com.myapp.account.DatabaseHelper;
import com.myapp.account.TitleArea;
import com.myapp.account.AccountAdd;

public class AccountMain extends Activity
{
    private TitleArea mTitleArea;
    private InfoArea  mInfoArea;
    /**
     * @brief called when the activity first created
     * @param Bundle
     * @return void
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        // initialize.
        init();
        registEvent();
        // title area/info area appear.
        mTitleArea.appear(this);
        mInfoArea.appear();
    }
    /**
     * @brief initialize
     * @param none
     * @return void
     */
    private void init() {
        mTitleArea = new TitleArea(this);
        mInfoArea = new InfoArea();
    }
    /**
     * @brief Rejist Event on Display Item
     */
    private void registEvent () {
        ImageButton btn = (ImageButton) findViewById(R.id.add_btn);
        btn.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    moveToAccountRegist();
                }
            });
    }
    /**
     * @brief Move to AccountRegist Display
     */
    private void moveToAccountRegist() {
        Intent intent = new Intent( AccountMain.this, AccountAdd.class);
        startActivity(intent);
    }
    /**
     * @brief InfoArea Class
     */
    private class InfoArea
    {
        private DatabaseHelper mDbHelper;
        private SQLiteDatabase mDatabase;
        /**
         * @brief constractor
         */
        InfoArea() {
            mDbHelper = new DatabaseHelper(getApplicationContext());
            mDatabase = mDbHelper.getReadableDatabase();
        }
        /**
         * @brief appear the info area
         * @param none
         * @return void
         */
        public void appear() {
            // get info from database.
            Cursor cur = mDatabase.query("AccountTable", null , null,
                                         null, null, null, null);
            TableLayout item_table = (TableLayout) findViewById(R.id.item_table);
            // item loop.
            while( cur.moveToNext() ){
                TextView account_item = new TextView(getApplicationContext());
                TextView account_money= new TextView(getApplicationContext());
                // get item name from AccountMaster.
                int master_id = cur.getInt(1);
                Cursor cur_master = mDatabase.rawQuery("select name from AccountMaster where _id =" +
                                                      master_id + ";",null);
                if( cur_master.moveToFirst() ) {
                    account_item.setText( cur_master.getString(0) );
                }
                String money = cur.getString(2) +
                    getText(R.string.money_unit).toString();
                account_money.setText( money );
                account_money.setTextSize(18);
                account_item.setTextSize(18);
                account_item.setGravity(Gravity.RIGHT);
                account_money.setGravity(Gravity.RIGHT);
                // display AccountTable.
                TableRow row = new TableRow(getApplicationContext());
                row.addView(account_item);
                row.addView(account_money);
                item_table.addView(row);
            }
        }
    }
    /**
     * Account Class
     */
    private class Account
    {
        private List<AccountItem> mAccountList;
        private DatabaseHelper mDbHelper;
        private SQLiteDatabase mDatabase;
        Account () {
            mAccountList = new ArrayList<AccountItem>();
            mDbHelper = new DatabaseHelper(getApplicationContext());
            mDatabase = mDbHelper.getReadableDatabase();
            getDataFromDB();
        }
        /**
         * @brief 
         */
        private void getDataFromDB() {
            Cursor cur = mDatabase.rawQuery("select _id from AccountTable;",null);
            while (cur.moveToNext() ) {
                mAccountList.add( new AccountItem(cur.getInt(0)) );
            }
        }
        /**
         * @brief get AccountList (String)
         */
        public List<AccountItem> getAccountList() {
            return mAccountList;
        }
    }
    /**
     * AccountItem Class
     */
    private class AccountItem
    {
        private int mId;
        private int mCategoryId;
        private int mMoney;
        private String mMemo;
        private DatabaseHelper mDbHelper;
        private SQLiteDatabase mDatabase;
//        private Date mDate;
        AccountItem(int id) {
            mDbHelper = new DatabaseHelper(getApplicationContext());
            mDatabase = mDbHelper.getReadableDatabase();
            mId = id;
            getDataFromDB();
        }
        /**
         * @brief get data from database
         */
        private void getDataFromDB() {
            Cursor cur =
                mDatabase.rawQuery("select * from AccountTable where id = "+ String.valueOf(mId) + ";", null);
            if( cur.moveToFirst() ) {
                mCategoryId = cur.getInt(1);
                mMoney = cur.getInt(2);
                mMemo = cur.getString(3);
            }
        }
        public int getId() { return mId; }
        public int getCategoryId() { return mCategoryId; }
        public int getMoney() { return mMoney; }
        public String getMemo() { return mMemo; }
//        public Date getDate() { return mDate; }
    }
}

