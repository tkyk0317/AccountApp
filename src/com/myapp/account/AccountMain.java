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
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import com.myapp.account.TitleArea;
import com.myapp.account.utility.Utility;
import com.myapp.account.database.DatabaseHelper;
import com.myapp.account.database.AccountTableAccessImpl;
import com.myapp.account.database.AccountTableRecord;
import com.myapp.account.database.AccountMasterTableRecord;
import com.myapp.account.database.AccountMasterTableAccessImpl;
import com.myapp.account.estimate.Estimate;
import com.myapp.account.infoarea.AbstractInfoArea;
import com.myapp.account.infoarea.DailyInfoAreaImpl;
import com.myapp.account.infoarea.MonthInfoAreaImpl;

/**
 * Main Class in AccountApp Application.
 */
public class AccountMain extends Activity {

    protected TitleArea titleArea;
    protected AbstractInfoArea  infoDailyArea;
    protected AbstractInfoArea  infoMonthArea;
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
     * Initialize Member-Variable.
     */
    protected void init() {
        titleArea = new TitleArea();
        infoDailyArea = new DailyInfoAreaImpl(this);
        infoMonthArea = new MonthInfoAreaImpl(this);
        DatabaseHelper db_helper = new DatabaseHelper(getApplicationContext());
        accountTable = new AccountTableAccessImpl(db_helper);
        estimateInfo = new Estimate(db_helper, this);
    }

    /**
     * Display Main Content.
     */
    public void displayMainContent() {
        // display tab.
        displayTabContent();

        // title area/info area appear.
        appearTotalIncome();
        appearTotalPayment();
        titleArea.appear(this);
        infoDailyArea.appear();
        infoMonthArea.appear();
    }

    /**
     * Display Tab Content.
     */
    protected void displayTabContent() {
        TabHost tab_host = (TabHost)findViewById(R.id.tabhost);
        tab_host.setup();

        TabSpec daily_tab = tab_host.newTabSpec("daily_tab");
        daily_tab.setIndicator(getText(R.string.daily_summary_tab_label));
        daily_tab.setContent(R.id.daily_summary);
        tab_host.addTab(daily_tab);

        TabSpec month_tab = tab_host.newTabSpec("month_tab");
        month_tab.setIndicator(getText(R.string.month_summary_tab_label));
        month_tab.setContent(R.id.month_summary);
        tab_host.addTab(month_tab);
   }

    /**
     * Appear Income Total.
     */
    protected void appearTotalIncome() {
        TableLayout summry_table = (TableLayout) findViewById(R.id.summry_table);
        TextView income_label = new TextView(getApplicationContext());
        TextView income_value = new TextView(getApplicationContext());

        int total = accountTable.getTotalIncomeAtCurrentMonth();

        income_value.setText(String.format("%,d", total) + getText(R.string.money_unit).toString());
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

        payment_value.setText(String.format("%,d", total) + getText(R.string.money_unit).toString());
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
     * Called Activity is Destoryed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        titleArea = null;
        infoDailyArea = null;
        infoMonthArea = null;
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
        infoDailyArea = null;
        infoMonthArea = null;
        accountTable = null;
        estimateInfo = null;
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
}

