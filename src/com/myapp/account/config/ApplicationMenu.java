package com.myapp.account.config;

import android.app.Activity;
import android.content.Intent;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Menu;
import com.myapp.account.R;

/**
 * Application Menu Class.
 */
public class ApplicationMenu {

    protected Activity activity;

    /**
     * Constractor.
     */
    public ApplicationMenu(Activity activity) {
        this.activity = activity;
    }

    /**
     * Appear the Application Menu.
     */
    public void appear(Menu menu) {
        activity.getMenuInflater().inflate(R.menu.app_menu,menu);
    }

    /**
     * Display Selected Menu.
     * @param item_id Selected Item Id.
     * @return true if match item_id.
     */
    public boolean displayMenu(int item_id) {
        boolean result = true;

        switch(item_id) {
            case R.id.menu_config:
                moveToConfig();
                break;
            case R.id.menu_account_data_edit:
                displayEditDialog();
                break;
            default:
                result = false;
                break;
        }
        return result;
    }

    /**
     * Move To Configuration Activity.
     */
    protected void moveToConfig() {
        Intent intent = new Intent( activity, AppConfigurationActivity.class);
        activity.startActivity(intent);
     }

    /**
     * Display Edit Dialog.
     */
    protected void displayEditDialog() {
        final String[] edit_menus
            = { activity.getText(R.string.menu_account_daily_edit_title).toString(),
                activity.getText(R.string.menu_account_month_edit_title).toString(),
                activity.getText(R.string.menu_master_add_title).toString() };

        AlertDialog.Builder edit_menu_dialog = new AlertDialog.Builder(activity);
        edit_menu_dialog.setTitle(R.string.menu_account_data_edit_list_title);
        edit_menu_dialog.setItems( edit_menus,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        edit_menu_dialog.create().show();
    }
}

