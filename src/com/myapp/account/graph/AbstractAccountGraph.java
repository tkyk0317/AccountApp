package com.myapp.account.graph;

import java.util.*;
import android.util.Log;
import android.app.Activity;
import android.widget.LinearLayout;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.GestureDetector.OnGestureListener;

import com.myapp.account.R;
import com.myapp.account.database.DatabaseHelper;
import com.myapp.account.database.AccountTableAccessor;
import com.myapp.account.database.AccountTableRecord;
import com.myapp.account.database.AccountMasterTableAccessor;
import com.myapp.account.database.AccountMasterTableRecord;
import com.myapp.account.utility.Utility;
import com.myapp.account.observer.ClickObserverInterface;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

/**
 * @brief Abstract Account Graph Class.
 */
public abstract class AbstractAccountGraph implements OnGestureListener, View.OnTouchListener {

    protected Activity activity;
    protected LinearLayout chartArea;
    protected String targetDate;
    protected AccountTableAccessor accountTable;
    protected AccountMasterTableAccessor masterTable;
    protected CategorySeries categorySeries;
    protected DefaultRenderer renderer;
    protected ArrayList<Integer> graphColors;
    protected GestureDetector gestureDetector;
    protected ClickObserverInterface observer;
    protected static final int NODATA_TEXT_SIZE = 30;
    protected static final int GRAPH_TITLE_SIZE = 20;
    protected static final int GRAPH_LABEL_SIZE = 10;
    protected static final int GRAPH_LEGEND_SIZE = 15;
    protected static final float GRAPH_SCALE = 0.85F;
    protected static final float GRAPH_START_ANGLE = -90.0F;
    protected static final String SLASH_STRING = "/";
    protected static final String MONEY_DELIMITER = ":";

    /**
     * @brief Constractor.
     * @param activity Activity Instance.
     */
    public AbstractAccountGraph(Activity activity, LinearLayout layout) {
        this.activity = activity;
        this.chartArea = layout;
        this.accountTable= new AccountTableAccessor(new DatabaseHelper(this.activity.getApplicationContext()));
        this.masterTable= new AccountMasterTableAccessor(new DatabaseHelper(this.activity.getApplicationContext()));
        this.categorySeries = new CategorySeries(null);
        this.renderer = new DefaultRenderer();
        this.gestureDetector = new GestureDetector(this.activity, this);

        // create color array.
        createColors();
    }

    /**
     * @brief Set Observer Instance.
     * @param observer Observer Instance.
     */
    public void attachObserver(ClickObserverInterface observer) {
        this.observer = observer;
    }

    /**
     * @brief Create Colors.
     */
    protected void createColors() {
        this.graphColors = new ArrayList<Integer>();
        this.graphColors.add(Color.rgb(102, 0, 255));
        this.graphColors.add(Color.rgb(153, 0, 0));
        this.graphColors.add(new Integer(Color.MAGENTA));
        this.graphColors.add(Color.rgb(204, 153, 0));
        this.graphColors.add(Color.rgb(204, 102, 51));
        this.graphColors.add(new Integer(Color.GREEN));
        this.graphColors.add(Color.rgb(153, 153, 0));
        this.graphColors.add(Color.rgb(153, 102, 0));
        this.graphColors.add(new Integer(Color.BLUE));
        this.graphColors.add(new Integer(Color.YELLOW));
        this.graphColors.add(new Integer(Color.CYAN));
        this.graphColors.add(new Integer(Color.RED));
        this.graphColors.add(new Integer(Color.LTGRAY));
    }

    /**
     * @brief Appear Graph at TargetDate.
     * @param target_date Specified Date.
     * @return true:success display false:failed display(not exsit data).
     */
    public boolean appear(String target_date) {
        this.targetDate = target_date;
        this.chartArea.removeAllViews();
        clearSeriesRender();

        if( true == this.accountTable.isExsitRecordAtTargetMonth(this.targetDate) ) {
            createGraph();
        } else {
            return false;
        }
        return true;
    }

    /**
     * @brief Clear Series Renderer.
     */
    protected void clearSeriesRender() {
        SimpleSeriesRenderer[] simple_renderer = this.renderer.getSeriesRenderers();

        for( int i = 0 ; i < simple_renderer.length ; ++i ) {
            this.renderer.removeSeriesRenderer(simple_renderer[i]);
        }
    }

    /**
     * @brief Create Graph Chart.
     */
    protected void createGraph() {
        this.categorySeries.clear();
        setGraphMetaData();
        addItemsIntoGraph();
        displayGraph();
    }

    /**
     * @brief Set MetaData for Account Graph.
     */
    protected void setGraphMetaData() {
        this.renderer.setPanEnabled(false);
        this.renderer.setClickEnabled(false);
        this.renderer.setAntialiasing(true);
        this.renderer.setFitLegend(true);
        this.renderer.setShowLabels(true);
        this.renderer.setShowLegend(true);
        this.renderer.setLabelsTextSize(GRAPH_LABEL_SIZE);
        this.renderer.setLegendTextSize(GRAPH_LEGEND_SIZE);
        this.renderer.setScale(GRAPH_SCALE);
        this.renderer.setStartAngle(GRAPH_START_ANGLE);
    }

    /**
     * @brief Add Items Into Account Graph.
     */
    protected void addItemsIntoGraph() {
        List<AccountTableRecord> account_records = this.accountTable.getRecordWithTargetMonthGroupByCategoryId(this.targetDate);
        Collections.sort(account_records, new SortAccountRecordByMoney() );

        int count = 0;
        for( AccountTableRecord record : account_records ) {
            AccountMasterTableRecord master_record = this.masterTable.getRecord(record.getCategoryId());

            // Check Target Data.
            if( false == isTargetAccountRecord(master_record) ) continue;

            String label_str = master_record.getName() + MONEY_DELIMITER + String.valueOf(record.getMoney()) + this.activity.getText(R.string.money_unit).toString();
            this.categorySeries.add(label_str, new Double((double)record.getMoney()));
            SimpleSeriesRenderer simple_renderer = new SimpleSeriesRenderer();
            simple_renderer.setColor(this.graphColors.get(count));
            simple_renderer.setDisplayChartValues(true);
            simple_renderer.setChartValuesTextSize((float)15);
            this.renderer.addSeriesRenderer(simple_renderer);

            // Set Color(Use as ring buffer).
            count++;
            if( this.graphColors.size() <= count ) count = 0;
        }
    }

    /**
     * @brief Check Target Record Data.
     * @param master_record master table record.
     * @return true:target false:not target.
     */
    protected abstract boolean isTargetAccountRecord(AccountMasterTableRecord master_record);

    /**
     * @brief Display Account Graph.
     */
    protected void displayGraph() {
        GraphicalView pie_chart = ChartFactory.getPieChartView(this.activity, this.categorySeries, this.renderer);
        pie_chart.setOnTouchListener(this);
        chartArea.addView(pie_chart);
    }

    /**
     * @brief Display NoData Message.
     * @param message Display Message.
     */
    public void displayNoDataMessage(String message) {
        this.chartArea.removeAllViews();
        this.chartArea.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER);

        // set nodata message.
        TextView nodata_text = new TextView(this.activity);
        nodata_text.setTextColor(Color.CYAN);
        nodata_text.setTextSize(NODATA_TEXT_SIZE);
        nodata_text.setText(message);
        this.chartArea.setOnTouchListener(this);

        // add view into layout.
        this.chartArea.addView(nodata_text);
    }

    /**
     * @brief OnTouchEvent.
     * @return true:handle the event false:not handle the event.
     */
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        this.gestureDetector.onTouchEvent(event);
        return true;
    }

    /**
     * @brief Flick Event.
     * @return true:handle the event false:not handle the event.
     */
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if( null != this.observer ) this.observer.notifyOnFling(this, e1, e2, velocityX, velocityY);
        return true;
    }

    // not support module.
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float x, float y) { return true; }
    @Override
    public void onShowPress(MotionEvent e) {}
    @Override
    public boolean onSingleTapUp(MotionEvent e) { return false; }
    @Override
    public boolean onDown(MotionEvent e) { return true; }
    @Override
    public void onLongPress(MotionEvent e) { }

    /**
     * @brief Sort Account Record By Money Class.
     */
    private class SortAccountRecordByMoney implements Comparator {

        /**
         * @brief Compare Function.
         * @param obj1 AccountTableRecord Instance.
         * @param obj2 AccountTableRecord Instance.
         */
        public int compare(Object obj1, Object obj2) {
            return (((AccountTableRecord)obj2).getMoney()) - (((AccountTableRecord)obj1).getMoney());
        }
    }
}


