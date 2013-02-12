package com.myapp.account;

import java.util.*;
import java.lang.NumberFormatException;
import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.myapp.account.titlearea.TitleArea;
import com.myapp.account.add_account_data.AccountAdd;
import com.myapp.account.config.AppConfigurationActivity;

/**
 * AccountAdd Activity Class.
 */
public class AccountAddActivity extends Activity
{
    protected TitleArea titleArea;
    protected AccountAdd accountAdd;

    /**
     * Create Activity.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_account);
        // initialize.
        init();
        titleArea.appear();
        accountAdd.appear();
    }

    /**
     * Initialize Member-Variable.
     */
    protected void init() {
        titleArea = new TitleArea(this);
        accountAdd = new AccountAdd(this);
    }

    /**
     * Called Activity is Destoryed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        titleArea = null;
        accountAdd = null;
    }

    /**
     * Called User can not see the Activity.
     */
    @Override
    protected void onStop() {
        super.onStop();
        titleArea = null;
        accountAdd = null;
    }

    /**
     * Called First Create Option menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.app_menu,menu);
        return true;
    }

    /**
      * Called Option Menu Seelcted.
      */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean result = true;

        switch(item.getItemId()) {
            case R.id.menu_config:
                moveToConfig();
                break;
            case R.id.menu_account_data_edit:
                break;
            default:
                result = false;
                break;
        }
        return result;
    }

    /**
      * Move To Config Activity.
      */
    protected void moveToConfig() {
        Intent intent = new Intent( AccountAddActivity.this, AppConfigurationActivity.class);
        startActivity(intent);
    }
}
