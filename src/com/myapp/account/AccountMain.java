package com.myapp.account;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener; 
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.text.format.Time;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.ImageButton;
import com.myapp.account.DatabaseHelper;
import com.myapp.account.TitleArea;
import com.myapp.account.AddAccount;

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
                    Intent intent = new Intent( AccountMain.this, AddAccount.class);
                    startActivity(intent);
                }
            });
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
                String money = cur.getString(2) + "円";
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
}

