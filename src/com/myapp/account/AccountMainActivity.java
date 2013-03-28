package com.myapp.account;

import java.util.*;
import java.lang.ClassCastException;
import android.util.Log;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.view.MenuItem;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;
import android.widget.ImageView;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;

import com.myapp.account.AccountGraphActivity;
import com.myapp.account.titlearea.TitleArea;
import com.myapp.account.utility.Utility;
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
 * @brief Main Class in AccountApp Application.
 */
public class AccountMainActivity extends Activity implements ClickObserverInterface, AccountEditCompleteObserver, OnClickListener {

    protected TitleArea titleArea;
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
    protected TextView returnCurrentMonthView;
    protected ImageView graphImage;
    protected static final int ANIMATION_DURATION = 300;

    /**
     * @brief Create Activity.
     * @param savedInstanceState Bundle Instance.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * @brief Called Activity Start.
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
     * @brief Initialize Member-Variable.
     */
    protected void init() {
        this.viewFlipper = (ViewFlipper)findViewById(R.id.calendar_flipper);
        this.returnCurrentMonthView = (TextView)findViewById(R.id.return_current_month);
        this.titleArea = new TitleArea(this);
        this.tabContent = new TabContent(this);
        this.summary = new Summary(this);
        this.applicationMenu = new ApplicationMenu(this);
        this.currentCalendar = new AccountCalendar(this, (LinearLayout)findViewById(R.id.current_flipper));
        this.nextCalendar = new AccountCalendar(this, (LinearLayout)findViewById(R.id.next_flipper));
        this.currentDate = Utility.getCurrentDate();

        // init graph image.
        initGraphImage();

        // not display return current month view.
        this.returnCurrentMonthView.setClickable(false);
        this.returnCurrentMonthView.setVisibility(View.INVISIBLE);

        // create animation.
        createTranslateAnimation();

        // clear summary views.
        clearSummaryViews();

        // regist event.
        registEvent();

        // attach observer.
        this.currentCalendar.attachObserver(this);
        this.nextCalendar.attachObserver(this);
        this.tabContent.attachObserverForDailyInfo(this);
    }

    /**
     * @brief Initalize Graph Image.
     */
    protected void initGraphImage() {
        this.graphImage = (ImageView)findViewById(R.id.account_graph_image);
        this.graphImage.setImageDrawable(getResources().getDrawable(R.drawable.graph));
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
     * @brief Clear Summary Views.
     */
    protected void clearSummaryViews() {
        TableLayout summary_estimate_area = (TableLayout)findViewById(R.id.summary_table);
        summary_estimate_area.removeAllViews();
    }

    /**
     * @brief Regist Event.
     */
    protected void registEvent() {
        this.returnCurrentMonthView.setId(ViewId.CALENDAR_VIEW.getId());
        this.graphImage.setId(ViewId.GRAPH_VIEW.getId());
        this.returnCurrentMonthView.setOnClickListener(this);
        this.graphImage.setOnClickListener(this);
    }

    /**
     * @brief OnClickEvent Listener.
     * @param view View Instance.
     */
    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == ViewId.CALENDAR_VIEW.getId()) {
            moveToCurrentCalendar();
        } else if( id ==  ViewId.GRAPH_VIEW.getId()) {
            moveToGraphActivity();
        }
    }

    /**
     * @brief Move To Current Calendar.
     */
    protected void moveToCurrentCalendar() {
        float velocity_x = 0;
        String current_date = Utility.getCurrentDate();

        if( this.currentDate.compareTo(current_date) < 0 ) {
            velocity_x = -1; // past than current.
        } else {
            velocity_x = 1; // future than current.
        }
        this.currentDate = Utility.getCurrentDate();
        moveToNextCalendar(velocity_x);
    }

    /**
     * @brief Move To Graph Activity.
     */
    protected void moveToGraphActivity() {
        Intent intent = new Intent( this, AccountGraphActivity.class);
        intent.putExtra(AccountGraphActivity.INTENT_VALUE_KEY_CURRENT_DATE, this.currentDate);
        startActivity(intent);
    }

    /**
     * @brief Display Main Content.
     */
    protected void displayMainContent() {
        this.summary.appear(this.currentDate);
        this.titleArea.appear(this.currentDate);
        this.tabContent.appear(this.currentDate);
        this.currentCalendar.appear(this.currentDate);
        this.currentCalendarIndex = CalendarIndex.CURRENT_ID;
    }

    /**
     * @brief Called Activity is Destoryed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        terminate();
    }

    /**
     * @brief Terminate Process.
     */
    protected void terminate() {
        this.titleArea = null;
        this.tabContent = null;
        this.summary = null;
        this.applicationMenu = null;
        this.leftInAnimation = null;
        this.leftOutAnimation = null;
        this.rightInAnimation = null;
        this.rightOutAnimation = null;
        this.currentCalendar = null;
        this.nextCalendar = null;
        this.viewFlipper = null;
        this.currentDate = null;
        this.currentCalendarIndex = null;
        this.returnCurrentMonthView = null;
        this.graphImage = null;
    }

    /**
     * @brief Called First Create Option menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        this.applicationMenu.appear(menu);
        return true;
    }

    /**
     * @brief Called Option Menu Seelcted.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return this.applicationMenu.displayMenu(item.getItemId());
    }

    /**
     * @brief Called User can not see the Activity.
     */
    @Override
    protected void onStop() {
        super.onStop();
        terminate();
    }

    /**
     * @brief Notify from Subject(AccountCalendar).
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
     * @brief Notify Long Click from Subject(AccountCalendar).
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
     * @brief Notify Long Click from DailyInfoArea.
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
        moveToNextCalendar(velocity_x);
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
     * @brief Move To Next Calendar.
     * @param velocit_x Velocity for X.
     */
    protected void moveToNextCalendar(float velocity_x) {

        if( this.currentCalendarIndex == CalendarIndex.NEXT_ID ) {
            this.currentCalendarIndex = CalendarIndex.CURRENT_ID;
        } else {
            this.currentCalendarIndex = CalendarIndex.NEXT_ID;
        }
        // Animatio calendar.
        animationCalendar(velocity_x);

        // reflesh.
        refleshDisplay();
    }

    /**
     * @brief Animation Calendar.
     * @param velocity_x Velocity of X.
     */
    protected void animationCalendar(float velocity_x) {
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
     * @brief Notify AccountEdit Complete.
     */
    @Override
    public void notifyAccountEditComplete() {
        refleshDisplay();
    }

    /**
     * @brief Reflesh Display.
     */
    protected void refleshDisplay() {
        clearSummaryViews();
        refleshCalendar();
        refleshReturnCurrentMonthView();
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
     * @brief Reflesh Return Current Month View.
     */
    protected void refleshReturnCurrentMonthView() {
        if( false == Utility.isIncludeTargetDateInCurrentMonth(this.currentDate) ) {
            this.returnCurrentMonthView.setClickable(true);
            this.returnCurrentMonthView.setVisibility(View.VISIBLE);
        } else {
            this.returnCurrentMonthView.setClickable(false);
            this.returnCurrentMonthView.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * @brief Calendar Index Enum.
     */
    private enum CalendarIndex {
        CURRENT_ID(), NEXT_ID();
    }

    /**
     * @brief View ID Class.
     */
    private enum ViewId {
        CALENDAR_VIEW(0), GRAPH_VIEW(1);

        private final int id;

        private ViewId(int id) { this.id = id; }
        public int getId() { return this.id; }
    }
}

