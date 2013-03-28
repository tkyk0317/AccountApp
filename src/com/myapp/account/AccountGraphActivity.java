package com.myapp.account;

import android.util.Log;
import android.os.Bundle;
import android.content.Intent;
import android.app.Activity;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.graphics.Color;
import android.view.Gravity;

import com.myapp.account.graph.AbstractAccountGraph;
import com.myapp.account.graph.AccountPaymentGraphImpl;
import com.myapp.account.graph.AccountIncomeGraphImpl;
import com.myapp.account.utility.Utility;

/**
 * @brief AccountGraph Activity Class.
 */
public class AccountGraphActivity extends Activity {

    public static final String INTENT_VALUE_KEY_CURRENT_DATE = "CurrentDate";
    protected AbstractAccountGraph accountPaymentGraph;
    protected String currentDate;
    protected static final int NODATA_TEXT_SIZE = 30;
    protected static final String SLASH_STRING = "/";

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
        setContentView(R.layout.account_graph);

        // initalize.
        init();

        // display graph.
        displayGraph();
   }

    /**
     * @brief Initialize.
     */
    protected void init() {
        Intent intent = getIntent();
        this.currentDate = intent.getStringExtra(AccountGraphActivity.INTENT_VALUE_KEY_CURRENT_DATE);
        this.accountPaymentGraph = new AccountPaymentGraphImpl(this);
    }

    /**
     * @brief Display the Graph at Target Date.
     */
    protected void displayGraph() {
        displayGraphTitle();

        if( false == this.accountPaymentGraph.appear(this.currentDate) ) {
            displayNoDataMessage();
        }
     }

    /**
     * @brief Display Graph Title.
     */
    protected void displayGraphTitle() {
        TextView graph_title = (TextView)findViewById(R.id.graph_title);
        String title = Utility.splitYearAndMonth(this.currentDate) + getText(R.string.payment_graph_title_suffix).toString();
        graph_title.setText(title.replaceAll(SLASH_STRING, getString(R.string.year_str).toString()));
    }

    /**
     * @brief Display No Data Message.
     */
    protected void displayNoDataMessage() {
        LinearLayout layout = (LinearLayout)findViewById(R.id.graph_chart);
        layout.removeAllViews();

        // set graph title.
        layout.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER);
        TextView nodata_text = new TextView(this);
        nodata_text.setTextColor(Color.CYAN);
        nodata_text.setTextSize(NODATA_TEXT_SIZE);

        nodata_text.setText(getText(R.string.graph_no_data).toString());
        layout.addView(nodata_text);
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
     * @brief Called User can not see the Activity.
     */
    @Override
    protected void onStop() {
        super.onStop();
        terminate();
    }

    /**
     * @brief Ternimate Process.
     */
    protected void terminate() {
        this.accountPaymentGraph = null;
    }
}

