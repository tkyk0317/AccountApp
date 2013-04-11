package com.myapp.account.graph;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import android.util.Log;
import android.app.Activity;
import android.widget.LinearLayout;
import android.graphics.Color;
import android.graphics.Paint.Align;

import com.myapp.account.R;
import com.myapp.account.utility.Utility;
import com.myapp.account.graph.AbstractAccountGraph;
import com.myapp.account.database.DatabaseHelper;
import com.myapp.account.database.AccountMasterTableAccessor;
import com.myapp.account.database.AccountMasterTableRecord;
import com.myapp.account.database.AccountTableRecord;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYSeriesRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

/**
 * @brief Account Payment Line Graph Class.
 */
public class AccountPaymentLineGraphImpl extends AbstractAccountGraph {

    protected XYMultipleSeriesRenderer multipleRenderer;
    protected List<Double[]> graphXItems;
    protected List<Double[]> graphYItems;
    protected List<String> graphTitle;
    protected List<AccountTableRecord> accountRecord;
    protected static final int MIN_Y_VALUE = 0;
    protected static final int MONTH_NUM_IN_YEAR = 12;
    protected static final int YLABEL_ITEM_NUM = 10;

    /**
     * @brief Constractor.
     *
     * @param activity Activity Instance.
     */
    public AccountPaymentLineGraphImpl(Activity activity, LinearLayout layout) {
        super(activity, layout);

        // delete unnecessary instance.
        this.categorySeries = null;
        this.renderer = null;

        // create new graph instance.
        createGraphInstance();
    }

    /**
     * @brief Create Graph Instance.
     */
    protected void createGraphInstance() {
        this.multipleRenderer= new XYMultipleSeriesRenderer();
        this.graphXItems = new ArrayList<Double[]>();
        this.graphYItems = new ArrayList<Double[]>();
        this.graphTitle = new ArrayList<String>();
        this.accountRecord = new ArrayList<AccountTableRecord>();
    }

    /**
     * @brief Create Graph Chart.
     */
    protected void createGraph() {
        // get account record.
        this.accountRecord.clear();
        this.accountRecord = getUsedDataInGraph();

        setGraphMetaData();
        addItemsIntoGraph();
        displayGraph();
    }

    /**
     * @brief Set MetaData for Account Graph.
     */
    protected void setGraphMetaData() {
        this.multipleRenderer.setYAxisMin(MIN_Y_VALUE);
        this.multipleRenderer.setYAxisMax((double)getMaxMoney());
        this.multipleRenderer.setXLabelsAlign(Align.CENTER);
        this.multipleRenderer.setYLabelsAlign(Align.RIGHT);
        this.multipleRenderer.setAxesColor(Color.LTGRAY);
        this.multipleRenderer.setShowGrid(true);
        this.multipleRenderer.setXLabels(MONTH_NUM_IN_YEAR);
        this.multipleRenderer.setYLabels(YLABEL_ITEM_NUM);
    }

    /**
     * @brief Get Max Money in AccountRecord.
     *
     * @return Max Money.
     */
    protected int getMaxMoney() {
        int max_money = 0;
        AccountMasterTableAccessor masterTable = new AccountMasterTableAccessor(new DatabaseHelper(this.activity.getApplicationContext()));

        for( AccountTableRecord record : this.accountRecord ) {
            AccountMasterTableRecord master_record = masterTable.getRecord(record.getCategoryId());

            if( true == isTargetAccountRecord(master_record) && record.getMoney() > max_money ) {
                max_money = record.getMoney();
            }
        }
        return max_money;
    }

    /**
     * @brief Clear Series Renderer.
     */
    protected void clearSeriesRender() {
        this.multipleRenderer.clearXTextLabels();
        this.multipleRenderer.clearYTextLabels();
        this.graphTitle.clear();
        this.graphXItems.clear();
        this.graphYItems.clear();

        SimpleSeriesRenderer[] simple_renderer = this.multipleRenderer.getSeriesRenderers();

        for( int i = 0 ; i < simple_renderer.length ; ++i ) {
            this.multipleRenderer.removeSeriesRenderer(simple_renderer[i]);
        }
    }

    /**
     * @brief Check Exsit Record at Target Date.
     *
     * @return true:exsit false:not exsit.
     */
    protected boolean isExsitRecord() {
        return this.accountTable.isExsitRecordAtTargetYear(this.targetDate);
    }

    /**
     * @brief Add Items Into Account Graph.
     */
    protected void addItemsIntoGraph() {

        // create X items.
        createXItems();

        // create Y Items.
        int prev_category_id = 0;
        ArrayList<Double> record_items = new ArrayList<Double>();
        ArrayList<Integer> month_items = new ArrayList<Integer>();

        for(int i = 0 ; i < this.accountRecord.size() ; ++i ) {
            AccountMasterTableRecord master_record = this.masterTable.getRecord(this.accountRecord.get(i).getCategoryId());

            // Check Target Data.
            if( false == isTargetAccountRecord(master_record) ) continue;

            // check init value.
            if( prev_category_id == 0 ) {
                this.graphTitle.add(master_record.getName());
                addRecordItem(record_items, month_items, this.accountRecord.get(i));
                prev_category_id = this.accountRecord.get(i).getCategoryId();
                continue;
            }

            // check category id changed timing.
            if( this.accountRecord.get(i).getCategoryId() != prev_category_id ) {
                prev_category_id = this.accountRecord.get(i).getCategoryId();

                // Add items into graph.
                addRecordItemIntoGraph(record_items, month_items);

                record_items.clear();
                month_items.clear();

                this.graphTitle.add(master_record.getName());
            }
            // add record item into tmporary buffer.
            addRecordItem(record_items, month_items, this.accountRecord.get(i));
        }
        // Add items into graph.
        addRecordItemIntoGraph(record_items, month_items);

        // set renderer.
        setMultipleRenderer();
    }

    /**
     * @brief Add Record Items.
     *
     * @param record_items Record list.
     * @param month_items month list.
     * @param record AccountTableRecord instance.
     */
    protected void addRecordItem(List<Double> record_items, List<Integer> month_items, AccountTableRecord record) {
        String month = Utility.splitMonth(record.getInsertDate());
        month_items.add(new Integer(Integer.valueOf(month)));
        record_items.add(new Double((double)record.getMoney()));
     }

    /**
     * @brief add record items into graph instance.
     *
     * @param record_items record items list.
     * @param month_items month items list.
     */
    protected void addRecordItemIntoGraph(List<Double> record_items, List<Integer> month_items) {
        Double[] result_data = new Double[] { (double)0, (double)0, (double)0, (double)0,
                                              (double)0, (double)0, (double)0, (double)0,
                                              (double)0, (double)0, (double)0, (double)0 };

        for( int j = 0 ; j < record_items.size() ; ++j ) {
            int month_index = month_items.get(j);
            result_data[month_index - 1] = record_items.get(j);
        }
        this.graphYItems.add(result_data);
    }

    /**
     * @brief Set multiple renderer.
     */
    protected void setMultipleRenderer() {
        int color_count = 0;
        int style_count = 0;
        PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE, PointStyle.DIAMOND, PointStyle.TRIANGLE, PointStyle.SQUARE };

        for (int i = 0; i < this.graphTitle.size() ; i++) {
            XYSeriesRenderer series_renderer = new XYSeriesRenderer();
            series_renderer.setColor(this.graphColors.get(color_count));
            series_renderer.setPointStyle(styles[style_count]);
            this.multipleRenderer.addSeriesRenderer(series_renderer);

            // Set Color(Use as ring buffer).
            color_count++;
            if( this.graphColors.size() <= color_count ) color_count = 0;

            style_count++;
            if( styles.length <= style_count ) style_count = 0;
        }
    }

    /**
     * @brief Get Used Data In Graph.
     */
    protected List<AccountTableRecord> getUsedDataInGraph() {
        return this.accountTable.getRecordWithTargetYearGroupByCategoryId(this.targetDate);
    }

    /**
     * @brief Create X Items.
     *
     * @param account_records AccountTable Record List.
     */
    protected void createXItems() {
        int category_count = calcXItemCount();

        for( int i = 0 ; i < category_count ; ++i ) {
            this.graphXItems.add(new Double[] { (double)1, (double)2, (double)3, (double)4,
                                                (double)5, (double)6, (double)7, (double)8,
                                                (double)9, (double)10, (double)11, (double)12 });
        }
    }

    /**
     * @brief Calculate X Items Count.
     *
     * @return Item Count.
     */
    protected int calcXItemCount() {
        int count = 0;
        int category_id = 0;
        for(AccountTableRecord record : this.accountRecord) {
            AccountMasterTableRecord master_record = this.masterTable.getRecord(record.getCategoryId());
            if( record.getCategoryId() != category_id &&
                true == isTargetAccountRecord(master_record)) {
                category_id = record.getCategoryId();
                count++;
            }
        }
        return count;
    }

    /**
     * @brief Check Target Record Data.
     *
     * @param master_record master table record.
     * @return true:target false:not target.
     */
    protected boolean isTargetAccountRecord(AccountMasterTableRecord master_record) {
        if( DatabaseHelper.PAYMENT_FLAG == master_record.getKindId() ) return true;
        return false;
    }

    /**
     * @brief Display Account Graph.
     */
    protected void displayGraph() {
        XYMultipleSeriesDataset dataset = buildDataset(this.graphTitle, this.graphXItems, this.graphYItems);
        GraphicalView line_chart = ChartFactory.getLineChartView(this.activity, dataset , this.multipleRenderer);
        line_chart.setOnTouchListener(this);
        this.chartArea.addView(line_chart);
    }

    /**
     * Builds an XY multiple dataset using the provided values.
     *
     * @param titles the series titles.
     * @param xValues the values for the X axis.
     * @param yValues the values for the Y axis.
     * @return the XY multiple dataset.
     */
    protected XYMultipleSeriesDataset buildDataset(List<String> titles, List<Double[]> xValues, List<Double[]> yValues) {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        addXYSeries(dataset, titles, xValues, yValues, 0);
        return dataset;
    }

    protected void addXYSeries(XYMultipleSeriesDataset dataset, List<String> titles, List<Double[]> xValues, List<Double[]> yValues, int scale) {
        int length = titles.size();

        for (int i = 0; i < length; i++) {
            XYSeries series = new XYSeries(titles.get(i), scale);
            Double[] xV = xValues.get(i);
            Double[] yV = yValues.get(i);
            int series_length = xV.length;
            for (int k = 0; k < series_length; k++) {
                series.add(xV[k].doubleValue(), yV[k].doubleValue());
            }
            dataset.addSeries(series);
        }
    }
}


