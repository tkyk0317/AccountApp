package com.myapp.account;

import java.util.*;
import java.lang.NumberFormatException;
import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.app.AlertDialog;
import android.content.DialogInterface;
import com.myapp.account.titlearea.TitleArea;
import com.myapp.account.add_account_data.AccountAdd;
import com.myapp.account.config.ApplicationMenu;

/**
 * AccountAdd Activity Class.
 */
public class AccountAddActivity extends Activity
{
    protected TitleArea titleArea;
    protected AccountAdd accountAdd;
    protected ApplicationMenu applicationMenu;

    /**
     * Create Activity.
     * @param savedInstanceState
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
        setContentView(R.layout.add_account);
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
        applicationMenu = new ApplicationMenu(this);
    }

    /**
     * Called Activity is Destoryed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        titleArea = null;
        accountAdd = null;
        applicationMenu = null;
    }

    /**
     * Called User can not see the Activity.
     */
    @Override
    protected void onStop() {
        super.onStop();
        titleArea = null;
        accountAdd = null;
        applicationMenu = null;
    }

    /**
     * Called First Create Option menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        applicationMenu.appear(menu);
        return true;
    }

    /**
      * Called Option Menu Seelcted.
      */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return applicationMenu.displayMenu(item.getItemId());
    }
}

