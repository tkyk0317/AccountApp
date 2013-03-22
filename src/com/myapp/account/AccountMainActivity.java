package com.myapp.account;

import java.util.*;
import android.util.Log;
import android.app.Activity;
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
    protected CalendarIndex currentCalendarIndex;
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

        // initialize.
        init();

        // display Main Content.
        displayMainContent();
    }

    /**
     * Initialize Member-Variable.
     */
    protected void init() {
        this.viewFlipper = (ViewFlipper)findViewById(R.id.calendar_flipper);
        this.titleArea = new TitleArea(this);
        this.estimateInfo = new Estimate(this);
        this.tabContent = new TabContent(this);
        this.summary = new Summary(this);
        this.applicationMenu = new ApplicationMenu(this);
        this.currentCalendar = new AccountCalendar(this, (LinearLayout)findViewById(R.id.current_flipper));
        this.nextCalendar = new AccountCalendar(this, (LinearLayout)findViewById(R.id.next_flipper));
        this.currentDate = Utility.getCurrentDate();

        // create animation.
        createTranslateAnimation();

        // clear summar/estimate.
        clearSummaryAndEstimateArea();

        // attach observer.
        this.currentCalendar.attachObserver(this);
        this.nextCalendar.attachObserver(this);
        this.tabContent.attachObserverForDailyInfo(this);
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
        this.estimateInfo.appear(this.currentDate);
        this.summary.appear(this.currentDate);
        this.titleArea.appear(this.currentDate);
        this.tabContent.appear(this.currentDate);
        this.currentCalendar.appear(this.currentDate);
        this.currentCalendarIndex = CalendarIndex.CURRENT_ID;
   }

    /**
     * Called Activity is Destoryed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        this.titleArea = null;
        this.estimateInfo = null;
        this.tabContent = null;
        this.summary = null;
        this.applicationMenu = null;
        this.leftInAnimation = null;
        this.leftOutAnimation = null;
        this.rightInAnimation = null;
        this.rightOutAnimation = null;
    }

    /**
     * Called First Create Option menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        this.applicationMenu.appear(menu);
        return true;
    }

    /**
      * Called Option Menu Seelcted.
      */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return this.applicationMenu.displayMenu(item.getItemId());
    }

    /**
     * Called User can not see the Activity.
     */
    @Override
    protected void onStop() {
        super.onStop();
        this.titleArea = null;
        this.estimateInfo = null;
        this.tabContent = null;
        this.summary = null;
        this.applicationMenu = null;
        this.leftInAnimation = null;
        this.leftOutAnimation = null;
        this.rightInAnimation = null;
        this.rightOutAnimation = null;
    }

    /**
     * Notify from Subject(AccountCalendar).
     * @param event AccountCalendarCell Instance.
     */
    @Override
    public void notifyClick(Object event) {
        AccountCalendarCell cell = (AccountCalendarCell)event;

        this.currentDate = cell.getDate();
        this.titleArea.appear(currentDate);
        this.tabContent.appear(currentDate);
    }

    /**
     * Notify Long Click from Subject(AccountCalendar).
     * @param event AccountCalendarCell Instance.
     */
    @Override
    public void notifyLongClick(Object event) {
        AccountCalendarCell cell = (AccountCalendarCell)event;

        // AccountAdd Displayed.
        this.currentDate = cell.getDate();
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
        this.currentDate = ((DailyInfoRecord)event).getAccountDate();

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
        setCurrentDate(velocity_x);

        if( this.nextCalendar.equals((AccountCalendar)event) ) {
            this.currentCalendar.appear(currentDate);
            this.currentCalendarIndex = CalendarIndex.CURRENT_ID;
        } else {
            this.nextCalendar.appear(currentDate);
            this.currentCalendarIndex = CalendarIndex.NEXT_ID;
        }

        // move to next calendar.
        moveCalendar(velocity_x);

        // reflesh.
        refleshDisplay();
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
        refleshDisplay();
    }

    /**
     * @brief Reflesh Display.
     */
    protected void refleshDisplay() {
        clearSummaryAndEstimateArea();
        refleshCalendar();
        this.estimateInfo.appear(this.currentDate);
        this.summary.appear(this.currentDate);
        this.titleArea.appear(this.currentDate);
        this.tabContent.appear(this.currentDate);
    }

    /**
     * @brief Reflesh Calendar.
     */
    protected void refleshCalendar() {
        if( this.currentCalendarIndex == CalendarIndex.CURRENT_ID ) {
            this.currentCalendar.appear(currentDate);
        } else if( this.currentCalendarIndex == CalendarIndex.NEXT_ID ) {
            this.nextCalendar.appear(currentDate);
        }
     }

    /**
     * @brief Calendar Index Enum.
     */
    private enum CalendarIndex {
        CURRENT_ID(), NEXT_ID();
    }
}

