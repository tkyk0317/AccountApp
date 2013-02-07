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
import com.myapp.account.database.DatabaseHelper;
import com.myapp.account.database.AccountTableAccessImpl;
import com.myapp.account.database.AccountTableRecord;
import com.myapp.account.database.AccountMasterTableRecord;
import com.myapp.account.database.AccountMasterTableAccessImpl;

/**
 * Main Class in AccountApp Application.
 */
public class AccountMain extends Activity
{
    private TitleArea mTitleArea;
    private InfoArea  mInfoArea;
    private DatabaseHelper mDbHelper;

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

       // title area/info area appear.
       appearTotalIncome();
       appearTotalPayment();
       mTitleArea.appear(this);
       mInfoArea.appear();
   }

   /**
     * Called Activity is Destoryed.
     */
   @Override
   public void onDestroy() {
       super.onDestroy();
       mTitleArea = null;
       mInfoArea = null;
       mDbHelper = null;
   }

   /**
    * Called User can not see the Activity.
    */
    @Override
    protected void onStop() {
        super.onStop();
        mTitleArea = null;
        mInfoArea = null;
        mDbHelper = null;
    }

   /**
    * Initialize Member-Variable.
    */
   private void init() {
       mTitleArea = new TitleArea();
       mInfoArea = new InfoArea();
       mDbHelper = new DatabaseHelper(getApplicationContext());
   }

   /**
    * Rejist Event
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
    * Move to AccountAdd Activity.
    */
   private void moveToAccountRegist() {
       Intent intent = new Intent( AccountMain.this, AccountAdd.class);
       startActivity(intent);
   }

    /**
     * Appear Income Total.
     */
    private void appearTotalIncome() {
        TextView income_area= (TextView) findViewById(R.id.income);
        AccountTableAccessImpl account_table = new AccountTableAccessImpl(mDbHelper);
        long total = account_table.getTotalIncodemAtCurrentMonth();

        String incomde_value = String.valueOf(total) + getText(R.string.money_unit).toString();
        income_area.setText(incomde_value);
    }

    /**
     * Appear Payment Total Money.
     */
    private void appearTotalPayment() {
        TextView payment_area = (TextView) findViewById(R.id.payment);
        AccountTableAccessImpl account_table = new AccountTableAccessImpl(mDbHelper);
        long total = account_table.getTotalPaymentAtCurrentMonth();

        String payment_value = String.valueOf(total) + getText(R.string.money_unit).toString();
        payment_area.setText(payment_value);
    }

   /**
    * Infomation Area Class.
    */
   private class InfoArea
   {
       private DatabaseHelper mDbHelper;

       /**
        * InfoArea Class Constractor.
        */
       InfoArea() {
           mDbHelper = new DatabaseHelper(getApplicationContext());
       }

       /**
        * Appear the infomation area.
        */
       public void appear() {
           // get info from database.
           AccountTableAccessImpl account_table = new AccountTableAccessImpl(mDbHelper);
           List<AccountTableRecord> account_record = account_table.getRecordWithCurrentDate();
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
       private void drawRecord(TableLayout layout, AccountTableRecord account_record) {
           TextView account_date = new TextView(getApplicationContext());
           TextView account_item = new TextView(getApplicationContext());
           TextView account_money= new TextView(getApplicationContext());

           // get item name from AccountMaster.
           int master_id = account_record.getCategoryId();
           AccountMasterTableAccessImpl account_master = new AccountMasterTableAccessImpl(mDbHelper);
           AccountMasterTableRecord account_master_record = account_master.get(master_id);

           account_date.setText( account_record.getDate().substring(5) );
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

