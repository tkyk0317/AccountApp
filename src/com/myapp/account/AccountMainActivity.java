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
import android.widget.TableLayout;
import com.myapp.account.titlearea.TitleArea;
import com.myapp.account.utility.Utility;
import com.myapp.account.estimate.Estimate;
import com.myapp.account.tabcontent.TabContent;
import com.myapp.account.summary.Summary;
import com.myapp.account.config.ApplicationMenu;
import com.myapp.account.calendar.AccountCalendar;
import com.myapp.account.calendar.AccountCalendarCell;
import com.myapp.account.observer.ClickObserverInterface;
import com.myapp.account.observer.AccountEditCompleteObserver;
import com.myapp.account.edit_account_data.AccountAdd;
import com.myapp.account.edit_account_data.AccountEdit;
import com.myapp.account.infoarea.DailyInfoRecord;

/**
 * Main Class in AccountApp Application.
 */
public class AccountMainActivity extends Activity implements ClickObserverInterface, AccountEditCompleteObserver {

    protected TitleArea titleArea;
    protected Estimate estimateInfo;
    protected Summary summary;
    protected TabContent tabContent;
    protected ApplicationMenu applicationMenu;
    protected AccountCalendar accountCalendar;
    protected String currentDate;

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

        // clear summar/estimate.
        clearSummaryAndEstimateArea();

        // attach observer.
        accountCalendar.attachObserver(this);
        tabContent.attachObserverForDailyInfo(this);
    }

    /**
     * Clear Summary and Estimate Area.
     */
    protected void clearSummaryAndEstimateArea() {
        TableLayout summary_estimate_area = (TableLayout)findViewById(R.id.summary_table);
        summary_estimate_area.removeAllViews();
    }

    /**
     * Display Main Content.
     */
    protected void displayMainContent() {
        currentDate = Utility.getCurrentDate();

        summary.appear();
        titleArea.appear(currentDate);
        tabContent.appear(currentDate);
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
     * Notify from Subject(AccountCalendar).
     * @param event AccountCalendarCell Instance.
     */
    @Override
    public void notifyClick(Object event) {
        AccountCalendarCell cell = (AccountCalendarCell)event;

        currentDate = cell.getDate();
        titleArea.appear(currentDate);
        tabContent.appear(currentDate);
    }

    /**
     * Notify Long Click from Subject(AccountCalendar).
     * @param event AccountCalendarCell Instance.
     */
    @Override
    public void notifyLongClick(Object event) {
        AccountCalendarCell cell = (AccountCalendarCell)event;

        // AccountAdd Displayed.
        currentDate = cell.getDate();
        AccountAdd account_add = new AccountAdd(this);
        account_add.attachObserver(this);
        account_add.appear(currentDate);
    }

    /**
     * Notify Long Click from DailyInfoArea.
     * @param event DailyInfoRecord Instance.
     */
    @Override
    public void notifyLongClickForDailyInfo(Object event) {
        currentDate = ((DailyInfoRecord)event).getAccountDate();
        // Modify dialog Display.
        AccountEdit account_edit = new AccountEdit(this);
        account_edit.attachObserver(this);
        account_edit.appear((DailyInfoRecord)event);
    }

    /**
     * Notify AccountEdit Complete.
     */
    @Override
    public void notifyAccountEditComplete() {
        // reflesh display.
        clearSummaryAndEstimateArea();
        estimateInfo.appear();
        summary.appear();
        titleArea.appear(currentDate);
        tabContent.appear(currentDate);
     }
}

