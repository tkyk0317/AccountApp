package com.myapp.account;

import java.util.*;
import android.util.Log;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.view.MenuItem;
import android.os.Bundle;
import android.widget.ImageButton;
import com.myapp.account.titlearea.TitleArea;
import com.myapp.account.utility.Utility;
import com.myapp.account.estimate.Estimate;
import com.myapp.account.tabcontent.TabContent;
import com.myapp.account.summary.Summary;
import com.myapp.account.config.ApplicationMenu;
import com.myapp.account.calendar.AccountCalendar;
import com.myapp.account.calendar.AccountCalendarCell;
import com.myapp.account.ObserverInterface;

/**
 * Main Class in AccountApp Application.
 */
public class AccountMainActivity extends Activity implements ObserverInterface {

    protected TitleArea titleArea;
    protected Estimate estimateInfo;
    protected Summary summary;
    protected TabContent tabContent;
    protected ApplicationMenu applicationMenu;
    protected AccountCalendar accountCalendar;

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
        titleArea = new TitleArea(this);
        estimateInfo = new Estimate(this);
        tabContent = new TabContent(this);
        summary = new Summary(this);
        applicationMenu = new ApplicationMenu(this);
        accountCalendar = new AccountCalendar(this);

        // attach observer.
        accountCalendar.attachObserver(this);
    }

    /**
     * Display Main Content.
     */
    public void displayMainContent() {
        summary.appear();
        titleArea.appear(Utility.getCurrentDate());
        tabContent.appear(Utility.getCurrentDate());
        accountCalendar.appear();
   }

    /**
     * Called Activity is Destoryed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        titleArea = null;
        estimateInfo = null;
        tabContent = null;
        summary = null;
        applicationMenu = null;
    }

    /**
     * Called First Create Option menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        applicationMenu.appear(menu);
        return true;
    }

    /**
      * Called Option Menu Seelcted.
      */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return applicationMenu.displayMenu(item.getItemId());
    }

    /**
     * Called User can not see the Activity.
     */
    @Override
    protected void onStop() {
        super.onStop();
        titleArea = null;
        estimateInfo = null;
        tabContent = null;
        summary = null;
        applicationMenu = null;
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
        Intent intent = new Intent( AccountMainActivity.this, AccountAddActivity.class);
        startActivity(intent);
    }

    /**
     * Notify from Subject(AccountCalendar).
     * @param event AccountCalendarCell Instance.
     */
    public void notify(Object event) {
        AccountCalendarCell cell = (AccountCalendarCell)event;

        titleArea.appear(cell.getDate());
        tabContent.appear(cell.getDate());
    }
}

