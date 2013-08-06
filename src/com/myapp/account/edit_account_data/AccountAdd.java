package com.myapp.account.edit_account_data;

import java.util.List;

import android.app.Activity;
import android.view.Gravity;
import android.widget.EditText;
import android.app.AlertDialog;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.widget.LinearLayout;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.myapp.account.R;
import com.myapp.account.utility.Utility;
import com.myapp.account.database.DatabaseHelper;
import com.myapp.account.database.AccountTableAccessor;
import com.myapp.account.database.AccountTableRecord;
import com.myapp.account.database.AccountMasterTableAccessor;
import com.myapp.account.database.AccountMasterTableRecord;
import com.myapp.account.tabcontent.DailyInfoRecord;
import com.myapp.account.observer.EventCompleteObserver;
import com.myapp.account.config.AppConfigurationData;
import com.mylib.calculator.Calculator;
import com.mylib.calculator.observer.ClickObserverInterface;

/**
 * @brief Add Account Date Class.
 */
public class AccountAdd implements OnItemSelectedListener, ClickObserverInterface {

    protected Activity activity = null;
    protected AlertDialog inputDialog = null;
    protected AccountTableAccessor accountTable = null;
    protected AccountMasterTableAccessor masterTable = null;
    protected LinearLayout layout = null;
    protected String insertDate = null;
    protected EventCompleteObserver observer = null;
    protected Spinner categorySpinner = null;
    protected String selectedCategoryItem = null;
    protected String[] categoryItems = null;
    protected Calculator calculator = null;
    protected static final String PREFIX_WEEKDAY = "(";
    protected static final String SUFFIX_WEEKDAY = ")";

    /**
     * @brief Constructor.
     */
    public AccountAdd(Activity activity) {
        this.activity = activity;
        AppConfigurationData app_config = new AppConfigurationData(this.activity);
        this.accountTable = new AccountTableAccessor(new DatabaseHelper(this.activity.getApplicationContext()), app_config);
        this.masterTable = new AccountMasterTableAccessor(new DatabaseHelper(this.activity.getApplicationContext()) );
        this.calculator = new Calculator(this.activity);
        this.calculator.attachObserver(this);
    }

    /**
     * @brief Attach Observer.
     * @param observer EventCompleteObserver Instance.
     */
    public void attachObserver(EventCompleteObserver observer) {
        this.observer = observer;
    }

    /**
     * @brief Appear the Account Add Display.
     */
    public void appear(String date) {
        this.insertDate = date;

        // initialize.
        initialize();
   }

    /**
     * @brief Appear the Account Display.
     * @param record AccoundTableRecord.
     */
    public void appear(DailyInfoRecord record) {
        // not supported.
    }

    /**
     * @brief Initialize.
     */
    protected void initialize() {
        createDialog();
        createCategoryItems();
        createSpinner();
        setTitleArea();
        setButtonTitle();
        setCalendarImage();
        setCalculatorImage();
        registEvent();
     }

    /**
     * @brief Create Alert Dialog.
     */
    protected void createDialog() {
        LayoutInflater inflater = LayoutInflater.from(this.activity);
        this.layout = (LinearLayout)inflater.inflate(R.layout.edit_account_record, (ViewGroup)this.activity.findViewById(R.id.edit_account_record));
        this.inputDialog = new AlertDialog.Builder(this.activity).create();
        this.inputDialog.setView(this.layout);
        this.inputDialog.getWindow().setGravity(Gravity.TOP);
        this.inputDialog.show();

        // modify dialog width.
        Utility.modifyDialogWidthMax(this.inputDialog);
    }

    /**
     * @brief Create Category Spinner.
     */
    protected void createSpinner() {
       this.categorySpinner = (Spinner)this.layout.findViewById(R.id.category_spinner);
       ArrayAdapter<String> adapter =
           new ArrayAdapter<String>(this.activity.getApplicationContext(), android.R.layout.simple_spinner_item, this.categoryItems);
       adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       this.categorySpinner.setAdapter(adapter);

       // set event listener.
       this.categorySpinner.setOnItemSelectedListener(this);
    }

    /**
     * @brief Create Category Items.
     */
    protected void createCategoryItems() {
        List<AccountMasterTableRecord> record = this.masterTable.getAllRecord();
        this.categoryItems = new String[ record.size() ];

        for( int i = 0 ; i < record.size() ; i++ ) {
            this.categoryItems[i] = record.get(i).getName();
        }
    }

    /**
     * @brief Set Title Area.
     */
    protected void setTitleArea() {
        TextView title_view = (TextView)this.layout.findViewById(R.id.date_title);

        String day_of_week = Utility.getDayOfWeekString(Utility.getDayOfWeek(this.insertDate), this.activity);
        String title = this.insertDate + PREFIX_WEEKDAY + day_of_week + SUFFIX_WEEKDAY;

        title_view.setText(title);
   }

    /**
     * @brief Set Button Title.
     */
    protected void setButtonTitle() {
        Button regist_button = (Button)this.layout.findViewById(R.id.regist_btn);
        regist_button.setText(this.activity.getText(R.string.regist_btn_label));
     }

    /**
     * @brief Set Calendar Button Image.
     */
    protected void setCalendarImage() {
        ImageView calendar_button = (ImageView)this.layout.findViewById(R.id.calendar_image);
        calendar_button.setImageDrawable(null);
    }

    /**
     * @brief Set Calculator Image.
     */
    protected void setCalculatorImage() {
        ImageView calculator_image = (ImageView)this.layout.findViewById(R.id.calculator_image);
        calculator_image.setImageResource(R.drawable.calculator);
    }

    /**
     * @brief Register Event
     */
    protected void registEvent() {
        // register button event.
        Button regist_btn = (Button)this.layout.findViewById(R.id.regist_btn);
        regist_btn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                insertOrUpdateAccountRecord();
            }
        });

        // click event for calculator image.
        ImageView calculator_image = (ImageView)this.layout.findViewById(R.id.calculator_image);
        calculator_image.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View view) {
                EditText money_text = (EditText)layout.findViewById(R.id.money_value);
                calculator.appear(money_text.getText().toString());
            }
        });
    }

    /**
     * @brief Insert or Update AccountRecord into AccountTable.
     */
    protected void insertOrUpdateAccountRecord() {
        try {
            AccountTableRecord record = getInputUserAccountInfo();

            if( isEnableInputData(record) ) {
                insertOrUpdateIntoDatabase(record);
                updateUseDateOfMaterTable(record.getCategoryId());
                this.observer.notifyAccountEditComplete();
                closeInputDialog();
                displayCompleteMessage();
            } else {
                displayInputDataAlertMessage();
            }
        } catch (NumberFormatException exception) {
            displayInputDataAlertMessage();
        }
    }

    /**
     * @brief Get Account Information from user's input information.
     */
    protected AccountTableRecord getInputUserAccountInfo()  throws NumberFormatException {
        EditText edit_money = (EditText)this.layout.findViewById(R.id.money_value);
        EditText edit_memo = (EditText)this.layout.findViewById(R.id.memo_value);

        AccountMasterTableRecord master_record = this.masterTable.getRecordMatchName(this.selectedCategoryItem);

        AccountTableRecord record = new AccountTableRecord();
        try {
            record.setInsertDate(this.insertDate);
            record.setUpdateDate(getCurrentDate());
            record.setCategoryId(master_record.getId());
            record.setMoney(Integer.valueOf(edit_money.getText().toString()));
            record.setMemo(edit_memo.getText().toString());
        } catch (NumberFormatException exception) {
            throw new NumberFormatException();
        }
        return record;
    }

    /**
     * @brief Check User Input Data is Enable.
     * @return true if user input data is enable data.
     */
    protected boolean isEnableInputData(AccountTableRecord input_data) {
        if( 0 == input_data.getMoney() ||
            true == Utility.isStringNULL(this.selectedCategoryItem) ||
            true == Utility.isStringNULL(input_data.getInsertDate()) ) {
            return false;
        }
        return true;
    }

    /**
     * @brief Insert or Update into Database.
     * @param record User input Account Infomation.
     */
    protected void insertOrUpdateIntoDatabase(AccountTableRecord record) {
        this.accountTable.insert(record);
    }

    /**
     * @brief Update Use Date of Master Table.
     * @param Key of master Table.
     */
    protected void updateUseDateOfMaterTable(int key) {
        AccountMasterTableRecord master_record = this.masterTable.getRecord(key);
        this.masterTable.update(master_record);
    }

    /**
     * @brief Close Input Dialog.
     */
    protected void closeInputDialog() {
        this.inputDialog.dismiss();
    }

    /**
     * @brief Display Complete Message.
     */
    protected void displayCompleteMessage() {
        String message = this.activity.getText(R.string.regist_msg).toString();
        Toast.makeText(this.activity, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * @brief Display Alert Message.
     */
    protected void displayInputDataAlertMessage() {
        String message = this.activity.getText(R.string.regist_alert_msg).toString();
        Toast.makeText(this.activity, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * @brief Select item on Spinner.
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id ) {
        Spinner spinner = (Spinner)parent;
        this.selectedCategoryItem = (String)spinner.getSelectedItem().toString();
    }

    /**
     * @brief notify click from calculator.
     *
     * @param event Event Instance(Calculator Instance).
     */
    @Override
    public void notifyClick(Object event) {
        Calculator cal = (Calculator)event;
        EditText edit_money = (EditText)this.layout.findViewById(R.id.money_value);
        int calculate_result = cal.getCalculateResult();
        edit_money.setText(String.valueOf(calculate_result));
    }

    /**
     * @brief Get Current Date.
     *
     * @return current date string.
     */
    private String getCurrentDate() {
        return Utility.getCurrentDate();
    }

    // not supported.
    @Override
    public void onNothingSelected(AdapterView<?> parent) {}
}


