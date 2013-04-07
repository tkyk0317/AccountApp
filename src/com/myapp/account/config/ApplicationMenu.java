package com.myapp.account.config;

import android.util.Log;
import android.app.Activity;
import android.content.Intent;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Menu;

import com.myapp.account.R;
import com.myapp.account.AppConfigurationActivity;
import com.myapp.account.EditAccountMasterActivity;
import com.myapp.account.EditUserTableActivity;

/**
 * @brief Application Menu Class.
 */
public class ApplicationMenu {

    // Dialog List Index.
    private enum DialogListIndex {
        ADD_CATEGORY_INDEX(0), ADD_USER_INDEX(1);

        private final int index;

        private DialogListIndex(int index) { this.index = index; }
        public int getIndex() { return this.index; }
    }

    protected Activity activity;

    /**
     * @brief Constractor.
     */
    public ApplicationMenu(Activity activity) {
        this.activity = activity;
    }

    /**
     * @brief Appear the Application Menu.
     */
    public void appear(Menu menu) {
        this.activity.getMenuInflater().inflate(R.menu.app_menu,menu);
    }

    /**
     * @brief Display Selected Menu.
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
     * @brief Move To Configuration Activity.
     */
    protected void moveToConfig() {
        Intent intent = new Intent( this.activity, AppConfigurationActivity.class);
        this.activity.startActivity(intent);
    }

    /**
     * @brief Display Edit Dialog.
     */
    protected void displayEditDialog() {
        final String[] edit_menus
            = { this.activity.getText(R.string.menu_master_edit_title).toString(),
                this.activity.getText(R.string.menu_user_edit_title).toString()};

        AlertDialog.Builder edit_menu_dialog = new AlertDialog.Builder(this.activity);
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
     * @brief Parse Click Event.
     * @param click_index Click List Item Index.
     */
    protected void parseClickEvent(int click_index) {
        if( click_index == DialogListIndex.ADD_CATEGORY_INDEX.getIndex() ) {
            moveToAddCategory();
        } else if( click_index == DialogListIndex.ADD_USER_INDEX.getIndex() ) {
            moveToUser();
        }
    }

    /**
     * @brief Move To Add Category into Master Activity.
     */
    protected void moveToAddCategory() {
        Intent intent = new Intent( this.activity, EditAccountMasterActivity.class);
        this.activity.startActivity(intent);
    }

    /**
     * @brief Move To Add User into User Table.
     */
    protected void moveToUser() {
        Intent intent = new Intent( this.activity, EditUserTableActivity.class);
        this.activity.startActivity(intent);
    }
}

