package com.myapp.account;

import android.util.Log;
import android.os.Bundle;
import android.content.Intent;
import android.app.Activity;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.view.MotionEvent;
import android.widget.ViewFlipper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;

import com.myapp.account.graph.AbstractAccountGraph;
import com.myapp.account.graph.AccountPaymentPieGraphImpl;
import com.myapp.account.utility.Utility;
import com.myapp.account.observer.ClickObserverInterface;

/**
 * @brief AccountPieGraph Activity Class.
 */
public class AccountPieGraphActivity extends Activity implements ClickObserverInterface {

    public static final String INTENT_VALUE_KEY_CURRENT_DATE = "CurrentDate";
    private AbstractAccountGraph currentGraph;
    private AbstractAccountGraph nextGraph;
    private String currentDate;
    private ViewFlipper viewFlipper;
    private Animation leftInAnimation;
    private Animation rightInAnimation;
    private Animation leftOutAnimation;
    private Animation rightOutAnimation;
    private GraphIndex currentGraphIndex;
    private static final String SLASH_STRING = "/";
    private static final int ANIMATION_DURATION = 0;

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
        this.currentGraphIndex = GraphIndex.CURRENT_ID;
        displayGraph(this.currentGraph);
   }

    /**
     * @brief Initialize.
     */
    private void init() {
        Intent intent = getIntent();
        this.currentDate = intent.getStringExtra(INTENT_VALUE_KEY_CURRENT_DATE);
        this.viewFlipper = (ViewFlipper)findViewById(R.id.graph_flipper);
        this.currentGraph = new AccountPaymentPieGraphImpl(this, (LinearLayout)findViewById(R.id.current_flipper));
        this.nextGraph = new AccountPaymentPieGraphImpl(this, (LinearLayout)findViewById(R.id.next_flipper));

        // create animation.
        createTranslateAnimation();

        // attach observer.
        this.currentGraph.attachObserver(this);
        this.nextGraph.attachObserver(this);
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
     * @brief Display the Graph at Target Date.
     */
    private void displayGraph(AbstractAccountGraph graph) {
        displayGraphTitle();
        graph.appear(this.currentDate);
    }

    /**
     * @brief Display Graph Title.
     */
    private void displayGraphTitle() {
        TextView graph_title = (TextView)findViewById(R.id.graph_title);
        String title = Utility.splitYearAndMonth(this.currentDate) + getText(R.string.payment_pie_chart_title_suffix).toString();
        graph_title.setText(title.replaceAll(SLASH_STRING, getString(R.string.year_str).toString()));
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
    private void terminate() {
        this.currentGraph = null;
        this.nextGraph = null;
        this.nextGraph = null;
        this.viewFlipper = null;
        this.leftInAnimation = null;
        this.leftOutAnimation = null;
        this.rightInAnimation = null;
        this.rightOutAnimation = null;
    }

    /**
     * @brief Notify onFiling Event.
     */
    public void notifyOnFling(Object event, MotionEvent motion_start, MotionEvent motion_end, float velocityX, float velocityY) {
        setCurrentDate(velocityX);
        moveToNextGraph(velocityX);
    }

    /**
     * @brief Set Current Date.
     * @param velocity_x Velocity of X.
     */
    private void setCurrentDate(float velocity_x) {
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
    private void moveToNextGraph(float velocity_x) {

        if( this.currentGraphIndex == GraphIndex.NEXT_ID ) {
            this.currentGraphIndex = GraphIndex.CURRENT_ID;
            displayGraph(this.currentGraph);
        } else {
            this.currentGraphIndex = GraphIndex.NEXT_ID;
            displayGraph(this.nextGraph);
        }
        // Animatio graph.
        animationGraph(velocity_x);
    }

    /**
     * @brief Animation Graph.
     * @param velocity_x Velocity of X.
     */
    private void animationGraph(float velocity_x) {
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

    // not support.
    public void notifyClick(Object event) {}
    public void notifyLongClick(Object event) {}
    public void notifyLongClickForDailyInfo(Object event) {}

    /**
     * @brief Graph Index Enum.
     */
    private enum GraphIndex {
        CURRENT_ID(), NEXT_ID();
    }

}

