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
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
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
    protected AccountCalendar currentCalendar;
    protected AccountCalendar nextCalendar;
    protected ViewFlipper viewFlipper;
    protected String currentDate;
    protected Animation leftInAnimation;
    protected Animation rightInAnimation;
    protected Animation leftOutAnimation;
    protected Animation rightOutAnimation;
    protected static final int ANIMATION_DURATION = 300;

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
        viewFlipper = (ViewFlipper)findViewById(R.id.calendar_flipper);

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
        currentCalendar = new AccountCalendar(this, (LinearLayout)findViewById(R.id.current_flipper));
        nextCalendar = new AccountCalendar(this, (LinearLayout)findViewById(R.id.next_flipper));

        // create animation.
        createTranslateAnimation();

        // clear summar/estimate.
        clearSummaryAndEstimateArea();

        // attach observer.
        currentCalendar.attachObserver(this);
        nextCalendar.attachObserver(this);
        tabContent.attachObserverForDailyInfo(this);
    }

    /**
     * @brief Create Translate Animation.
     */
    protected void createTranslateAnimation() {
        this.leftInAnimation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,-1.0f, Animation.RELATIVE_TO_PARENT,0.0f, Animation.RELATIVE_TO_PARENT,0.0f, Animation.RELATIVE_TO_PARENT,0.0f);
        this.rightInAnimation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,1.0f, Animation.RELATIVE_TO_PARENT,0.0f, Animation.RELATIVE_TO_PARENT,0.0f, Animation.RELATIVE_TO_PARENT,0.0f);
        this.leftOutAnimation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,0.0f, Animation.RELATIVE_TO_PARENT,-1.0f, Animation.RELATIVE_TO_PARENT,0.0f, Animation.RELATIVE_TO_PARENT,0.0f);
        this.rightOutAnimation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,0.0f, Animation.RELATIVE_TO_PARENT,1.0f, Animation.RELATIVE_TO_PARENT,0.0f, Animation.RELATIVE_TO_PARENT,0.0f);
        this.leftInAnimation.setDuration(ANIMATION_DURATION);
        this.leftOutAnimation.setDuration(ANIMATION_DURATION);
        this.rightInAnimation.setDuration(ANIMATION_DURATION);
        this.rightOutAnimation.setDuration(ANIMATION_DURATION);
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
        currentCalendar.appear(currentDate);
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
        leftInAnimation = null;
        leftOutAnimation = null;
        rightInAnimation = null;
        rightOutAnimation = null;
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
     * @brief OnFiling Event.
     */
    @Override
    public void notifyOnFling(Object event, MotionEvent motion_start, MotionEvent motion_end, float velocity_x, float velocity_y) {

        // appear the next calendar.
        setCurrentDate(velocity_x);
        if( nextCalendar.equals((AccountCalendar)event) ) {
            currentCalendar.appear(currentDate);
        } else {
            nextCalendar.appear(currentDate);
        }

        // move to next calendar.
        moveCalendar(velocity_x);
        this.titleArea.appear(this.currentDate);
        this.tabContent.appear(this.currentDate);
     }

    /**
     * @brief Set Current Date.
     * @param velocity_x Velocity of X.
     */
    protected void setCurrentDate(float velocity_x) {
        if( velocity_x < 0 ) {
            this.currentDate = Utility.getNextMonthDate(currentDate);
        } else {
            this.currentDate = Utility.getPreviousMonthDate(currentDate);
        }
    }

    /**
     * @brief Move Next Calendar.
     * @param velocity_x Velocity of X.
     */
    protected void moveCalendar(float velocity_x) {
        if( velocity_x < 0 ) {
            this.viewFlipper.setInAnimation(rightInAnimation);
            this.viewFlipper.setOutAnimation(leftOutAnimation);
            this.viewFlipper.showNext();
        } else {
            this.viewFlipper.setInAnimation(leftInAnimation);
            this.viewFlipper.setOutAnimation(rightOutAnimation);
            this.viewFlipper.showPrevious();
        }
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

