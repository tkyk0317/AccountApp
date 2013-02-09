package com.myapp.account;

import java.util.*;
import android.util.Log;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.widget.TextView;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.ImageButton;
import com.myapp.account.TitleArea;
import com.myapp.account.utility.Utility;
import com.myapp.account.database.DatabaseHelper;
import com.myapp.account.database.AccountTableAccessImpl;
import com.myapp.account.database.AccountTableRecord;
import com.myapp.account.database.AccountMasterTableRecord;
import com.myapp.account.database.AccountMasterTableAccessImpl;
import com.myapp.account.estimate.Estimate;

/**
 * Main Class in AccountApp Application.
 */
public class AccountMain extends Activity {

    protected TitleArea titleArea;
    protected InfoArea  infoArea;
    protected Estimate estimateInfo;
    protected AccountTableAccessImpl accountTable;
    protected static final int TEXT_FONT_SIZE = 20;

    /**
     * Create Activity.
     * @param savedInstanceState Bundle Instance.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Called Activity Start.
     */
    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.main);

        // initialize.
        init();
        registEvent();

        // appear estimate infomation.
        estimateInfo.appear();

        // display Main Content.
        displayMainContent();
    }

    /**
     * Display Main Content.
     */
    public void displayMainContent() {
        // title area/info area appear.
        appearTotalIncome();
        appearTotalPayment();
        titleArea.appear(this);
        infoArea.appear();
    }

    /**
     * Called Activity is Destoryed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        titleArea = null;
        infoArea = null;
        accountTable = null;
        estimateInfo = null;
    }

    /**
     * Called User can not see the Activity.
     */
    @Override
    protected void onStop() {
        super.onStop();
        titleArea = null;
        infoArea = null;
        accountTable = null;
        estimateInfo = null;
    }

    /**
     * Initialize Member-Variable.
     */
    protected void init() {
        titleArea = new TitleArea();
        infoArea = new InfoArea();

        DatabaseHelper db_helper = new DatabaseHelper(getApplicationContext());
        accountTable = new AccountTableAccessImpl(db_helper);
        estimateInfo = new Estimate(db_helper, this);
    }

    /**
     * Rejist Event
     */
    protected void registEvent () {
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
     * Move to AccountAdd Activity.
     */
    protected void moveToAccountRegist() {
        Intent intent = new Intent( AccountMain.this, AccountAdd.class);
        startActivity(intent);
    }

    /**
     * Appear Income Total.
     */
    protected void appearTotalIncome() {
        TableLayout summry_table = (TableLayout) findViewById(R.id.summry_table);
        TextView income_label = new TextView(getApplicationContext());
        TextView income_value = new TextView(getApplicationContext());

        int total = accountTable.getTotalIncomeAtCurrentMonth();

        income_value.setText(String.valueOf(total) + getText(R.string.money_unit).toString());
        income_label.setText(getText(R.string.income_label));
        income_label.setTextSize(TEXT_FONT_SIZE);
        income_value.setTextSize(TEXT_FONT_SIZE);
        income_label.setGravity(Gravity.RIGHT);
        income_value.setGravity(Gravity.RIGHT);

        TableRow row = new TableRow(getApplicationContext());
        row.addView(income_label);
        row.addView(income_value);
        summry_table.addView(row);
    }

    /**
     * Appear Payment Total Money.
     */
    protected void appearTotalPayment() {
        TableLayout summry_table = (TableLayout) findViewById(R.id.summry_table);
        TextView payment_label = new TextView(getApplicationContext());
        TextView payment_value = new TextView(getApplicationContext());

        int total = accountTable.getTotalPaymentAtCurrentMonth();

        payment_value.setText(String.valueOf(total) + getText(R.string.money_unit).toString());
        payment_label.setText(getText(R.string.payment_label));
        payment_label.setTextSize(TEXT_FONT_SIZE);
        payment_value.setTextSize(TEXT_FONT_SIZE);
        payment_label.setGravity(Gravity.RIGHT);
        payment_value.setGravity(Gravity.RIGHT);

        TableRow row = new TableRow(getApplicationContext());
        row.addView(payment_label);
        row.addView(payment_value);
        summry_table.addView(row);
    }

    /**
     * Infomation Area Class.
     */
    private class InfoArea
    {
        protected AccountTableAccessImpl accountTable;
        protected AccountMasterTableAccessImpl masterTable;

        /**
         * InfoArea Class Constractor.
         */
        InfoArea() {
            DatabaseHelper db_helper = new DatabaseHelper(getApplicationContext());
            accountTable = new AccountTableAccessImpl(db_helper);
            masterTable = new AccountMasterTableAccessImpl(db_helper);
        }

        /**
         * Appear the infomation area.
         */
        public void appear() {
            // get info from database.
            List<AccountTableRecord> account_record = accountTable.getRecordWithCurrentDateGroupByCategoryId();
            TableLayout item_table = (TableLayout) findViewById(R.id.item_table);

            Log.d("InfoArea", "AccountRecord Number = " + account_record.size() );
            // item loop.
            for( int i = 0 ; i < account_record.size() ; i++ ) {
                // draw.
                drawRecord(item_table, account_record.get(i) );
            }
        }

        /**
         * Draw Item from AccountTable.
         * @param layout TableLayout instance.
         * @param account_record AccountTable Record(Displayed Item).
         */
        protected void drawRecord(TableLayout layout, AccountTableRecord account_record) {
            TextView account_date = new TextView(getApplicationContext());
            TextView account_item = new TextView(getApplicationContext());
            TextView account_money= new TextView(getApplicationContext());

            // get item name from AccountMaster.
            int master_id = account_record.getCategoryId();
            AccountMasterTableRecord account_master_record = masterTable.get(master_id);

            account_date.setText( Utility.splitCurrentMonthAndDay(account_record.getInsertDate()) );
            account_item.setText( account_master_record.getName() );
            String money = String.valueOf( account_record.getMoney() ) + getText(R.string.money_unit).toString();
            account_money.setText( "(" + money +")" );

            account_date.setTextSize(18);
            account_money.setTextSize(18);
            account_item.setTextSize(18);

            account_date.setGravity(Gravity.RIGHT);
            account_item.setGravity(Gravity.RIGHT);
            account_money.setGravity(Gravity.RIGHT);

            // display AccountTable.
            TableRow row = new TableRow(getApplicationContext());
            row.addView(account_date);
            row.addView(account_item);
            row.addView(account_money);
            layout.addView(row);
        }
    }
}

