package com.myapp.account;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener; 
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import com.myapp.account.TitleArea;

/**
 * Add Account Class
 */
public class AddAccount extends Activity
{
    private TitleArea mTitleArea;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_account);
        // initialize.
        init();
        // title_area appear.
        mTitleArea.appear(this);
    }
    private void init() {
        mTitleArea = new TitleArea(this);
    }
}

