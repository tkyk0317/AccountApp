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
    protected static final String DAILY_TAB_ID = "daily_tab";
    protected static final String MONTHLY_TAB_ID = "monthly_tab";

    /**
     * Constractor.
     */
    public TabContent(Activity activity) {
        this.activity = activity;
        infoDailyArea = new DailyInfoAreaImpl(activity);
        infoMonthArea = new MonthInfoAreaImpl(activity);

        // create tab content.
        createTabContent();
     }

    /**
     * Appear the TabContent.
     */
    public void appear(String display_date) {
        Log.d("TabContent", "[START]");
        changeTabHeight();
        infoDailyArea.appear(display_date);
        infoMonthArea.appear(display_date);
        Log.d("TabContent", "[END]");
     }

    /**
     * Create Tab Content.
     */
    protected void createTabContent() {
        TabHost tab_host = (TabHost)activity.findViewById(R.id.tabhost);
        tab_host.setup();

        // daily/month tab.
        createDailyTab(tab_host);
        createMonthlyTab(tab_host);

        // TabChangedEvent.
        tab_host.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tab_id) {
                if( tab_id == DAILY_TAB_ID ) {
                } else if( tab_id == MONTHLY_TAB_ID ) {
                } else {
                }
            }
        });
    }

   /**
    * Create Daily Tab.
    * @param tab_host TabHost Instance.
    */
   protected void createDailyTab(TabHost tab_host) {
        TabSpec daily_tab = tab_host.newTabSpec(DAILY_TAB_ID);
        daily_tab.setIndicator(activity.getText(R.string.daily_summary_tab_label));
        daily_tab.setContent(R.id.daily_summary);
        tab_host.addTab(daily_tab);
   }

   /**
    * Create Monthly Tab.
    * @param tab_host TabHost Instance.
    */
   protected void createMonthlyTab(TabHost tab_host) {
        TabSpec month_tab = tab_host.newTabSpec(MONTHLY_TAB_ID);
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

