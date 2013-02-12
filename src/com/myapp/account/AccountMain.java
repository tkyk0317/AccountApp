package com.myapp.account;

import java.util.*;
import android.util.Log;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.widget.ImageButton;
import com.myapp.account.titlearea.TitleArea;
import com.myapp.account.utility.Utility;
import com.myapp.account.estimate.Estimate;
import com.myapp.account.tabcontent.TabContent;
import com.myapp.account.summary.Summary;

/**
 * Main Class in AccountApp Application.
 */
public class AccountMain extends Activity {

    protected TitleArea titleArea;
    protected Estimate estimateInfo;
    protected Summary summary;
    protected TabContent tabContent;

    /**
     * Create Activity.
     * @param savedInstanceState Bundle Instance.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Called Activity Start.
     */
    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.main);

        // initialize.
        init();
        registEvent();

        // appear estimate infomation.
        estimateInfo.appear();

        // display Main Content.
        displayMainContent();
    }

    /**
     * Initialize Member-Variable.
     */
    protected void init() {
        titleArea = new TitleArea(this);
        estimateInfo = new Estimate(this);
        tabContent = new TabContent(this);
        summary = new Summary(this);
    }

    /**
     * Display Main Content.
     */
    public void displayMainContent() {
        summary.appear();
        titleArea.appear();
        tabContent.appear();
   }

    /**
     * Called Activity is Destoryed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        titleArea = null;
        estimateInfo = null;
        tabContent = null;
        summary = null;
    }

    /**
     * Called User can not see the Activity.
     */
    @Override
    protected void onStop() {
        super.onStop();
        titleArea = null;
        estimateInfo = null;
        tabContent = null;
        summary = null;
    }

    /**
     * Rejist Event
     */
    protected void registEvent () {
        ImageButton btn = (ImageButton) findViewById(R.id.add_btn);
        btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        moveToAccountRegist();
                    }
                });
    }

    /**
     * Move to AccountAdd Activity.
     */
    protected void moveToAccountRegist() {
        Intent intent = new Intent( AccountMain.this, AccountAdd.class);
        startActivity(intent);
    }
}

