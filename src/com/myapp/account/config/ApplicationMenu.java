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

    // Dialog List Index.
    private enum DialogListIndex {

        DAILY_ACCOUNT_EDIT_INDEX(0), MONTHLY_ACCOUNT_EDIT_INDEX(1), ADD_CATEGORY_INDEX(2);

        private final int index;

        private DialogListIndex(int index) { this.index = index; }
        public int getIndex() { return this.index; }
    }

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
                    public void onClick(DialogInterface dialog, int index) {
                        parseClickEvent(index);
                    }
                });
        edit_menu_dialog.show();
    }

    /**
     * Parse Click Event.
     * @param click_index Click List Item Index.
     */
    protected void parseClickEvent(int click_index) {
        if( click_index == DialogListIndex.DAILY_ACCOUNT_EDIT_INDEX.getIndex() ) {
            moveToEditDailyAccountData();
        } else if( click_index == DialogListIndex.MONTHLY_ACCOUNT_EDIT_INDEX.getIndex() ) {
            moveToEditMonthlyAccountData();
        } else if( click_index == DialogListIndex.ADD_CATEGORY_INDEX.getIndex() ) {
            moveToAddCategory();
        }
    }

    /**
     * Move To Edit Daily Account Data Activity.
     */
    protected void moveToEditDailyAccountData() {
    }

    /**
     * Move To Edit Monthly Account Data Activity.
     */
    protected void moveToEditMonthlyAccountData() {
    }

    /**
     * Move To Add Category into Master Activity.
     */
    protected void moveToAddCategory() {
    }
}

