package com.myapp.account.tabcontent;

import android.app.Activity;
import android.util.Log;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import com.myapp.account.R;
import com.myapp.account.infoarea.AbstractInfoArea;
import com.myapp.account.infoarea.DailyInfoAreaImpl;
import com.myapp.account.infoarea.MonthInfoAreaImpl;

/**
 * TabContent Class.
 */
public class TabContent {

    protected Activity activity;
    protected AbstractInfoArea  infoDailyArea;
    protected AbstractInfoArea  infoMonthArea;
    protected static final int TAB_HEIGHT_SIZE = 30;

    /**
     * Constractor.
     */
    public TabContent(Activity activity) {
        this.activity = activity;
        infoDailyArea = new DailyInfoAreaImpl(activity);
        infoMonthArea = new MonthInfoAreaImpl(activity);
     }

    /**
     * Appear the TabContent.
     */
    public void appear() {
        Log.d("TabContent", "[START]");
        displayTabContent();
        changeTabHeight();
        infoDailyArea.appear();
        infoMonthArea.appear();
        Log.d("TabContent", "[END]");
     }

    /**
     * Display Tab Content.
     */
    protected void displayTabContent() {
        TabHost tab_host = (TabHost)activity.findViewById(R.id.tabhost);
        tab_host.setup();

        TabSpec daily_tab = tab_host.newTabSpec("daily_tab");
        daily_tab.setIndicator(activity.getText(R.string.daily_summary_tab_label));
        daily_tab.setContent(R.id.daily_summary);
        tab_host.addTab(daily_tab);

        TabSpec month_tab = tab_host.newTabSpec("month_tab");
        month_tab.setIndicator(activity.getText(R.string.month_summary_tab_label));
        month_tab.setContent(R.id.month_summary);
        tab_host.addTab(month_tab);
   }

   /**
    * Change TabWidget Height.
    */
   protected void changeTabHeight() {
       TabHost tab_host = (TabHost)activity.findViewById(R.id.tabhost);
       TabWidget tab_widget = tab_host.getTabWidget();
       for( int i = 0 ; i < tab_widget.getChildCount() ; i++ ) {
           tab_widget.getChildTabViewAt(i).getLayoutParams().height = TAB_HEIGHT_SIZE;
       }
   }

}

