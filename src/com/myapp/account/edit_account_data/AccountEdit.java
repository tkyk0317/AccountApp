package com.myapp.account.edit_account_data;

import java.util.*;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.view.View;
import android.view.View.OnClickListener;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;

import com.myapp.account.R;
import com.myapp.account.utility.Utility;
import com.myapp.account.database.AccountTableRecord;
import com.myapp.account.edit_account_data.AccountAdd;
import com.myapp.account.infoarea.DailyInfoRecord;
import com.myapp.account.observer.EventCompleteObserver;

/**
 * @brief Edit Account Date Class.
 */
public class AccountEdit extends AccountAdd implements EventCompleteObserver {

    protected DailyInfoRecord updateRecord = null;
    protected CalendarClickEvent calendarEvent = null;
    protected static final int BUTTON_WIDTH = 100;

    /**
     * @brief Constractor.
     */
    public AccountEdit(Activity activity) {
        super(activity);
    }

    /**
     * @brief Appear the Account Add Display.
     */
    @Override
    public void appear(String date) {
        // not supported.
    }

    /**
     * @brief Appear the Account Display.
     * @param record AccoundTableRecord.
     */
    @Override
    public void appear(DailyInfoRecord record) {
        this.updateRecord = record;
        this.insertDate = record.getAccountDate();
        this.calendarEvent = new CalendarClickEvent(this.activity, this.insertDate);
        this.calendarEvent.attachObserver(this);

        // initalize.
        initialize();

        // set current data.
        setCurrentData();

        // Add Delete Button.
        addDeleteButton();
    }

    /**
     * @brief set Calendar Image.
     */
    protected void setCalendarImage() {
        ImageView calendar_image = (ImageView)this.layout.findViewById(R.id.calendar_image);
        calendar_image.setImageResource(R.drawable.calendar);
        calendar_image.setOnClickListener(this.calendarEvent);
    }

    /**
     * @brief Add Delete Button.
     */
    protected void addDeleteButton() {
        Button delete_button = new Button(this.activity);
        delete_button.setWidth(BUTTON_WIDTH);
        delete_button.setText(R.string.delete_btn_label);

        LinearLayout linear_layout = (LinearLayout)this.layout.findViewById(R.id.commit_btn_area);
        linear_layout.addView(delete_button);

        // set event listener.
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
                    public void onClick(View v) {
                        // Create Dialog.
                        AlertDialog.Builder confirm_dialog = new AlertDialog.Builder(activity);
                        confirm_dialog.setTitle(R.string.delete_confirm_msg);

                        // delete OK.
                        confirm_dialog.setPositiveButton(
                            R.string.delete_confirm_yes,
                            new DialogInterface.OnClickListener() {
                                @Override public void onClick(DialogInterface dialog, int which) {
                                    deleteAccountRecord();
                                    observer.notifyAccountEditComplete();
                                    closeInputDialog();
                                    displayDeleteCompleteMessage();
                                }
                        });
                        // delete NO.
                        confirm_dialog.setNegativeButton(
                            R.string.delete_confirm_no,
                            new DialogInterface.OnClickListener() {
                                @Override public void onClick(DialogInterface dialog, int which) {}
                            });
                        confirm_dialog.show();
                   }
                });
    }

    /**
     * @brief Delete Current Record.
     */
    protected void deleteAccountRecord() {
        this.accountTable.delete(updateRecord.getAccountRecord().getId());
    }

    /**
     * @brief Set Button Title.
     */
    @Override
    protected void setButtonTitle() {
        Button regist_button = (Button)this.layout.findViewById(R.id.regist_btn);
        regist_button.setText(this.activity.getText(R.string.modify_btn_label));
     }

    /**
     * @brief set Current Data.
     */
    protected void setCurrentData() {
        EditText current_money = (EditText)this.layout. findViewById(R.id.money_value);
        EditText current_memo = (EditText)this.layout.findViewById(R.id.memo_value);

        // set current data.
        this.categorySpinner.setSelection(getCategoryPositionFromSpinner(updateRecord.getCategoryName()));
        current_money.setText(String.valueOf(this.updateRecord.getAccountRecord().getMoney()));
        current_memo.setText(this.updateRecord.getAccountMemo());
    }

    /**
     * @brief Get Category Position in Spinner.
     */
    protected int getCategoryPositionFromSpinner(String category_name) {
        int pos = 0;
        boolean is_find = false;
        for( pos = 0 ; pos < this.categoryItems.length ; ++pos ) {
            if( this.categoryItems[pos].equals(category_name) ) {
                is_find = true;
                break;
            }
        }

        if( true == is_find ) return pos;
        return 0;
    }

    /**
     * @brief Insert or Update into Database.
     * @param record User input Account Infomation.
     */
    protected void insertOrUpdateIntoDatabase(AccountTableRecord record) {
        record.setId(this.updateRecord.getAccountRecord().getId());
        record.setUpdateDate(Utility.getCurrentDate());

        // update record.
        this.accountTable.update(record);
    }

    /**
     * @brief Display Complete Message.
     */
    protected void displayCompleteMessage() {
        String message = this.activity.getText(R.string.modify_msg).toString();
        Toast.makeText(this.activity, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * @brief Display Delete Complete Message.
     */
    protected void displayDeleteCompleteMessage() {
        String message = this.activity.getText(R.string.delete_msg).toString();
        Toast.makeText(this.activity, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * @brief Edit Complete.
     */
    public void notifyAccountEditComplete() {
        this.insertDate = this.calendarEvent.getCalendarDate();
        setTitleArea();
    }

    // not supportted.
    public void notifyAccountMasterEditComplete() {}
    public void notifyUserTableEditComplete() {}

    /**
     * @brief Calendar Click Event Class.
     */
    private class CalendarClickEvent implements OnClickListener {

        private Activity activity = null;
        private String insertDate = null;
        private DatePickerDialog calendarDialog = null;
        private EventCompleteObserver observer = null;

        /**
         * @brief constractor.
         *
         * @param activity Activity Instances.
         */
        public CalendarClickEvent(Activity activity, String insert_date) {
           this.activity = activity;
           this.insertDate = insert_date;
        }

        /**
         * @brief Attach Observer.
         *
         * @param observer EventCompleteObserver Instance.
         */
        public void attachObserver(EventCompleteObserver observer) {
            this.observer = observer;
        }

        /**
         * @brief OnClick Event Handler.
         *
         * @param view View Instances.
         */
        @Override
        public void onClick(View view) {
            // create picker.
            createCalendarPicker();
            // show dialog.
            this.calendarDialog.show();
        }

        /**
         * @brief create calendar dialog.
         */
        private void createCalendarPicker() {
            this.calendarDialog = new DatePickerDialog(
                    this.activity,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int day) {
                            insertDate = Utility.createDateFormat(year, month + 1, day);
                            if( observer != null ) observer.notifyAccountEditComplete();
                        }
                    },
                    Integer.valueOf(Utility.splitYear(this.insertDate)),
                    Integer.valueOf(Utility.splitMonth(this.insertDate)) - 1,
                    Integer.valueOf(Utility.splitDay(this.insertDate))
                    );
        }

        // getter.
        public String getCalendarDate() { return this.insertDate; }
    }
}

