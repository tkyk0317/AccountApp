package com.myapp.account;

import android.util.Log;
import android.os.AsyncTask;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.view.MenuItem;
import android.os.Bundle;
import android.widget.TableLayout;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;
import android.widget.ImageView;
import android.widget.Toast;
import android.view.animation.Animation;
import android.app.ProgressDialog;
import android.view.animation.TranslateAnimation;

import com.myapp.account.AccountLineGraphActivity;
import com.myapp.account.AccountPieGraphActivity;
import com.myapp.account.titlearea.TitleArea;
import com.myapp.account.utility.Utility;
import com.myapp.account.tabcontent.TabContent;
import com.myapp.account.summary.Summary;
import com.myapp.account.config.ApplicationMenu;
import com.myapp.account.config.AppConfigurationData;
import com.myapp.account.calendar.AccountCalendar;
import com.myapp.account.calendar.AccountCalendarCell;
import com.myapp.account.observer.ClickObserverInterface;
import com.myapp.account.observer.EventCompleteObserver;
import com.myapp.account.edit_account_data.AccountAdd;
import com.myapp.account.edit_account_data.AccountEdit;
import com.myapp.account.tabcontent.DailyInfoRecord;
import com.myapp.account.response.ResponseApplicationMenuInterface;
import com.myapp.account.edit_account_data.ClearAccountData;

/**
 * @brief Main Class in AccountApp Application.
 */
public class AccountMainActivity extends Activity implements ClickObserverInterface, EventCompleteObserver,
                                                             OnClickListener, ResponseApplicationMenuInterface,
                                                             ClearAccountData.OnClearAccountDataInterface {

    private TitleArea titleArea;
    private Summary summary;
    private TabContent tabContent;
    private ApplicationMenu applicationMenu;
    private AccountCalendar currentCalendar;
    private AccountCalendar nextCalendar;
    private ViewFlipper viewFlipper;
    private String currentDate;
    private Animation leftInAnimation;
    private Animation rightInAnimation;
    private Animation leftOutAnimation;
    private Animation rightOutAnimation;
    private CalendarIndex currentCalendarIndex;
    private TextView returnCurrentMonthView;
    private ImageView pieGraphImage;
    private ImageView lineGraphImage;
    private ImageView addAccountImage;
    private AppConfigurationData appConfig;
    private ProgressDialog progressDialog = null;
    private static final int ANIMATION_DURATION = 300;

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

        // start worker thread.
        new LoadingDatabaseASync((Context)this).execute("");
    }

    /**
     * @brief Initialize Member-Variable.
     */
    private void init() {
        this.viewFlipper = (ViewFlipper)findViewById(R.id.calendar_flipper);
        this.returnCurrentMonthView = (TextView)findViewById(R.id.return_current_month);
        this.titleArea = new TitleArea(this);
        this.tabContent = new TabContent(this);
        this.summary = new Summary(this);
        this.applicationMenu = new ApplicationMenu(this);
        this.currentCalendar = new AccountCalendar(this, (LinearLayout)findViewById(R.id.current_flipper));
        this.nextCalendar = new AccountCalendar(this, (LinearLayout)findViewById(R.id.next_flipper));
        this.currentDate = getCurrentDate();
        this.appConfig = new AppConfigurationData(this);

        // initialize image.
        initImage();

        // not display return current month view.
        this.returnCurrentMonthView.setClickable(false);
        this.returnCurrentMonthView.setVisibility(View.INVISIBLE);

        // create animation.
        createTranslateAnimation();

        // clear summary views.
        clearSummaryViews();

        // register event.
        registEvent();

        // attach observer.
        this.currentCalendar.attachObserver(this);
        this.nextCalendar.attachObserver(this);
        this.tabContent.attachObserverForDailyInfo(this);
    }

    /**
     * @brief Initialize Image.
      */
    private void initImage() {
        this.pieGraphImage = (ImageView)findViewById(R.id.account_pie_chart_image);
        this.pieGraphImage.setImageDrawable(getResources().getDrawable(R.drawable.pie_chart));
        this.lineGraphImage = (ImageView)findViewById(R.id.account_line_chart_image);
        this.lineGraphImage.setImageDrawable(getResources().getDrawable(R.drawable.line_chart));
        this.addAccountImage = (ImageView)findViewById(R.id.add_account_image);
        this.addAccountImage.setImageDrawable(getResources().getDrawable(R.drawable.add_button));
    }

    /**
     * @brief Create Translate Animation.
     */
    private void createTranslateAnimation() {
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
    private void clearSummaryViews() {
        TableLayout summary_estimate_area = (TableLayout)findViewById(R.id.summary_table);
        summary_estimate_area.removeAllViews();
    }

    /**
     * @brief Register Event.
     */
    private void registEvent() {
        this.returnCurrentMonthView.setId(ViewId.CALENDAR_VIEW.getId());
        this.pieGraphImage.setId(ViewId.PIE_GRAPH_VIEW.getId());
        this.lineGraphImage.setId(ViewId.LINE_GRAPH_VIEW.getId());
        this.addAccountImage.setId(ViewId.ADD_ACCOUNT_VIEW.getId());
        this.returnCurrentMonthView.setOnClickListener(this);
        this.pieGraphImage.setOnClickListener(this);
        this.lineGraphImage.setOnClickListener(this);
        this.addAccountImage.setOnClickListener(this);
    }

    /**
     * @brief Display Main Content.
     */
    public void displayMainContent() {
        this.summary.appear(this.currentDate);
        this.titleArea.appear(this.currentDate);
        this.tabContent.appear(this.currentDate);
        this.currentCalendar.appear(this.currentDate);
        this.currentCalendarIndex = CalendarIndex.CURRENT_ID;
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
        } else if(id ==  ViewId.PIE_GRAPH_VIEW.getId()) {
            moveToPieChartActivity();
        } else if(id == ViewId.LINE_GRAPH_VIEW.getId()) {
            moveToLineChartActivity();
        } else if(id == ViewId.ADD_ACCOUNT_VIEW.getId()) {
            appearAddAccountDialog();
        }
    }

    /**
     * @brief Move To Current Calendar.
     */
    private void moveToCurrentCalendar() {
        float velocity_x = 0;
        String current_date = getCurrentDate();

        if( this.currentDate.compareTo(current_date) < 0 ) {
            velocity_x = -1; // past than current.
        } else {
            velocity_x = 1; // future than current.
        }
        this.currentDate = current_date;
        moveToNextCalendar(velocity_x);
    }

    /**
     * @brief Move To Pie Chart Activity.
     */
    private void moveToPieChartActivity() {
        Intent intent = new Intent( this, AccountPieGraphActivity.class);
        intent.putExtra(AccountPieGraphActivity.INTENT_VALUE_KEY_CURRENT_DATE, this.currentDate);
        startActivity(intent);
    }

    /**
     * @brief Move To Line Chart Activity.
     */
    private void moveToLineChartActivity() {
        Intent intent = new Intent( this, AccountLineGraphActivity.class);
        intent.putExtra(AccountLineGraphActivity.INTENT_VALUE_KEY_CURRENT_DATE, this.currentDate);
        startActivity(intent);
    }

    /**
     * @brief Appear Account Add Dialog.
     */
    private void appearAddAccountDialog() {
        AccountAdd account_add = new AccountAdd(this);
        account_add.attachObserver(this);
        account_add.appear(this.currentDate);
    }

    /**
     * @brief Called Activity is Destroyed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        terminate();
    }

    /**
     * @brief Terminate Process.
     */
    private void terminate() {
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
        this.pieGraphImage = null;
        this.lineGraphImage = null;
        this.addAccountImage = null;
        this.appConfig = null;
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
     * @brief Called Option Menu Selected.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return this.applicationMenu.displayMenu(item.getItemId(), this);
    }

    /**
     * @brief Response when Import Data.
     *
     * @param boolean Import Data Result(true:successed false:failed).
     */
    @Override
    public void OnResponseImportData(boolean is_result) {
        refreshDisplay();
    }

    /**
     * @brief Response when TableData is Exported.
     *
     * @param boolean Export Data Resule(true:successed false:failed).
     */
    @Override
    public void OnResponseExportData(boolean is_result) {
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

        // check refresh timing.
        if( true == isSummaryReflesh(cell) ) {
            clearSummaryViews();
            this.summary.appear(cell.getDate());
        }
        this.currentDate = cell.getDate();
        this.titleArea.appear(this.currentDate);
        this.tabContent.appear(this.currentDate);
    }

    /**
     * @brief Check Refresh Summary.
     *
     * @param cell AccountCalendarCell instance.
     *
     * @return true:refresh false:not refresh.
     */
    private boolean isSummaryReflesh(AccountCalendarCell cell) {
        int start_day = this.appConfig.getStartDay();
        int last_day = Integer.valueOf(Utility.splitDay(getLastDateOfTargetMonth(cell.getDate())));

        // check lastday and current day.
        if( start_day > last_day ) start_day = last_day;

        int current_day = Integer.valueOf(Utility.splitDay(cell.getDate()));
        int previous_day = Integer.valueOf(Utility.splitDay(this.currentDate));

        // check refresh timing.
        if( previous_day < start_day && start_day <= current_day ) {
            return true;
        }
        if( previous_day >= start_day && current_day < start_day ) {
            return true;
        }
        return false;
    }

    /**
     * @brief Notify Long Click.
     * @param event DailyInfoRecord Instance.
     */
    @Override
    public void notifyLongClick(Object event) {
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
    private void setCurrentDate(float velocity_x) {
        if( velocity_x < 0 ) {
            this.currentDate = getNextMonthDate(this.currentDate);
        } else {
            this.currentDate = getPreviousMonthDate(this.currentDate);
        }
    }

    /**
     * @brief Move To Next Calendar.
     * @param velocit_x Velocity for X.
     */
    private void moveToNextCalendar(float velocity_x) {

        if( this.currentCalendarIndex == CalendarIndex.NEXT_ID ) {
            this.currentCalendarIndex = CalendarIndex.CURRENT_ID;
        } else {
            this.currentCalendarIndex = CalendarIndex.NEXT_ID;
        }
        // Animation calendar.
        animationCalendar(velocity_x);

        // refresh.
        refreshDisplay();
    }

    /**
     * @brief Animation Calendar.
     * @param velocity_x Velocity of X.
     */
    private void animationCalendar(float velocity_x) {
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
        refreshDisplay();
    }

    // not supported.
    @Override
    public void notifyAccountMasterEditComplete() {}
    @Override
    public void notifyUserTableEditComplete() {}

    /**
     * @brief Refresh Display.
     */
    private void refreshDisplay() {
        clearSummaryViews();
        refreshCalendar();
        refleshReturnCurrentMonthView();
        this.summary.appear(this.currentDate);
        this.titleArea.appear(this.currentDate);
        this.tabContent.appear(this.currentDate);
    }

    /**
     * @brief Refresh Calendar.
     */
    private void refreshCalendar() {
        if( this.currentCalendarIndex == CalendarIndex.CURRENT_ID ) {
            this.currentCalendar.appear(currentDate);
        } else if( this.currentCalendarIndex == CalendarIndex.NEXT_ID ) {
            this.nextCalendar.appear(currentDate);
        }
    }

    /**
     * @brief Refresh Return Current Month View.
     */
    private void refleshReturnCurrentMonthView() {
        if( false == Utility.isIncludeTargetDateInCurrentMonth(this.currentDate) ) {
            this.returnCurrentMonthView.setClickable(true);
            this.returnCurrentMonthView.setVisibility(View.VISIBLE);
        } else {
            this.returnCurrentMonthView.setClickable(false);
            this.returnCurrentMonthView.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * @brief Get Next Month Date.
     *
     * @param target_date target date of next month.
     *
     * @return next month date.
     */
    private String getNextMonthDate(String target_date) {
        return Utility.getNextMonthDate(target_date);
    }

    /**
     * @brief Get Previous Month Date.
     *
     * @param target_date target date of previous month.
     *
     * @return previous month date.
     */
    private String getPreviousMonthDate(String target_date) {
        return Utility.getPreviousMonthDate(target_date);
    }

    /**
     * @brief Get Current Date.
     *
     * @return current date.
     */
    private String getCurrentDate() {
        return Utility.getCurrentDate();
    }

    /**
     * @brief Get Last Date of Month.
     *
     * @param target_date target_date of month.
     *
     * @return last date.
     */
    private String getLastDateOfTargetMonth(String target_date) {
        return Utility.getLastDateOfTargetMonth(target_date);
    }

    /**
     * @brief Calendar Index.
     */
    private enum CalendarIndex {
        CURRENT_ID(), NEXT_ID();
    }

    /**
     * @brief View ID Class.
     */
    private enum ViewId {
        CALENDAR_VIEW(0), PIE_GRAPH_VIEW(1), LINE_GRAPH_VIEW(2), ADD_ACCOUNT_VIEW(3);

        private final int id;

        private ViewId(int id) { this.id = id; }
        public int getId() { return this.id; }
    }

    /**
     * @brief Notify Start Clear Data.
     */
    public void onStartClearData() {
        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setTitle(getText(R.string.clear_account_data_progress_dialog_title));
        this.progressDialog.setMessage(getText(R.string.clear_account_data_progress_dialog_message));
        this.progressDialog.setIndeterminate(false);
        this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        this.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        this.progressDialog.setCancelable(false);
        this.progressDialog.show();
    }

    /**
     * @brief Notify Finish Clear Data.
     */
    public void onFinishClearData() {
        this.progressDialog.dismiss();
        this.progressDialog = null;
        Toast.makeText(this, getText(R.string.clear_account_data_complete).toString(), Toast.LENGTH_SHORT).show();
        refreshDisplay();
    }

    /**
     * @brief Loading Database Class(ASync).
     */
    @SuppressLint("NewApi")
    static private class LoadingDatabaseASync extends AsyncTask<String, Integer, Boolean> {
        private Context context = null;
        private ProgressDialog progressDialog = null;
        private boolean IsCalledProgressUpdate = false;
        private static final int THREAD_SLEEP_TIME = 500;

        /**
         * @brief Constructor.
         *
         * @param context Context Instance.
         */
        public LoadingDatabaseASync(Context context) {
            this.context = context;
        }

        /**
         * @brief First Called from UI Thread.
         */
        @Override
        protected void onPreExecute() {
            this.progressDialog = new ProgressDialog(this.context);
            this.progressDialog.setTitle(this.context.getText(R.string.loading_progress_dialog_title));
            this.progressDialog.setMessage(this.context.getText(R.string.loading_progress_dialog_message));
            this.progressDialog.setIndeterminate(false);
            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
        }

        /**
         * @brief BackGround Work.
         *
         * @param params String Parameters.
         */
        @Override
        protected Boolean doInBackground(String... params) {
            this.IsCalledProgressUpdate = false;
            publishProgress(0);

            // waiting until displayMainContent is end.
            while( false == this.IsCalledProgressUpdate ) {
                try {
                    Thread.sleep(THREAD_SLEEP_TIME);
                } catch (InterruptedException e) {
                    Log.d("LoadingDatabaseASync", "InterruptedException");
                }
            }
            return Boolean.valueOf(true);
        }

        /**
         * @brief Called from Worker Thread when publishProgress called.
         *
         * @param values progress value.
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            // because displayMainContent is processing related to userinterface,
            // it is treated in onProgressUpdate.
            // UI process and Not UI Process in displayMainContent should be split.
            ((AccountMainActivity)this.context).displayMainContent();
            this.IsCalledProgressUpdate = true;
        }

        /**
         * @brief Called when Cancel.
         */
        @Override
        protected void onCancelled() {
        }

        /**
         * @brief Called when Worker Thread is Complete.
         *
         * @param result Result Parameter.
         */
        @Override
        protected void onPostExecute(Boolean result) {
            this.progressDialog.dismiss();
            this.progressDialog = null;
        }
    }
}

