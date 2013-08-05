package com.myapp.account.config;

import android.app.Activity;
import android.content.Intent;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Menu;
import android.widget.Toast;

import com.myapp.account.R;
import com.myapp.account.AppConfigurationActivity;
import com.myapp.account.EditAccountMasterActivity;
import com.myapp.account.EditUserTableActivity;
import com.myapp.account.file_manager.ExportDatabaseTable;
import com.myapp.account.file_manager.ImportDatabaseTable;
import com.myapp.account.response.ResponseApplicationMenuInterface;

/**
 * @brief Application Menu Class.
 */
public class ApplicationMenu implements ResponseApplicationMenuInterface {

    // Dialog List Index for Edit Data.
    private enum EditDataListIndex {
        ADD_CATEGORY_INDEX(0), ADD_USER_INDEX(1);

        private final int index;

        private EditDataListIndex(int index) { this.index = index; }
        public int getIndex() { return this.index; }
    }

    // Dialog List Index for CSV Data.
    private enum CSVDataListIndex {
        INPUT_CSV_FILE_DATA(0), OUTPUT_CSV_FILE_DATA(1);

        private final int index;

        private CSVDataListIndex(int index) { this.index = index; }
        public int getIndex() { return this.index; }
    }

    private Activity activity = null;
    private ResponseApplicationMenuInterface responseAppMenu = null;

    /**
     * @brief Constructor.
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
    public boolean displayMenu(int item_id, ResponseApplicationMenuInterface response) {
        this.responseAppMenu = response;
        boolean result = true;

        switch(item_id) {
            case R.id.menu_config:
                moveToConfig();
                break;
            case R.id.menu_account_data_edit:
                displayEditDialog();
                break;
            case R.id.menu_account_data_input_output:
                displayInputOutputAccountDataDialog();
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
    private void moveToConfig() {
        Intent intent = new Intent( this.activity.getApplicationContext(), AppConfigurationActivity.class);
        this.activity.startActivity(intent);
    }

    /**
     * @brief Display Edit Dialog.
     */
    private void displayEditDialog() {
        final String[] edit_menus
            = { this.activity.getText(R.string.menu_master_edit_title).toString(),
                this.activity.getText(R.string.menu_user_edit_title).toString()};

        AlertDialog.Builder edit_menu_dialog = new AlertDialog.Builder(this.activity);
        edit_menu_dialog.setTitle(R.string.menu_account_data_edit_list_title);
        edit_menu_dialog.setItems(edit_menus,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int index) {
                        parseClickEventForEditData(index);
                    }
                });
        edit_menu_dialog.show();
    }

    /**
     * @brief Display Input and Output Account Data Dialog.
     */
    private void displayInputOutputAccountDataDialog() {
        final String[] edit_menus
            = { this.activity.getText(R.string.menu_input_account_data_title).toString(),
                this.activity.getText(R.string.menu_output_account_data_title).toString()};

        AlertDialog.Builder edit_menu_dialog = new AlertDialog.Builder(this.activity);
        edit_menu_dialog.setTitle(R.string.menu_input_output_account_data_title);
        edit_menu_dialog.setItems(edit_menus,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int index) {
                        parseClickEventForCSVData(index);
                    }
                });
        edit_menu_dialog.show();
    }

    /**
     * @brief Parse Click Event.
     * @param click_index Click List Item Index.
     */
    private void parseClickEventForEditData(int click_index) {
        if( click_index == EditDataListIndex.ADD_CATEGORY_INDEX.getIndex() ) {
            moveToAddCategory();
        } else if( click_index == EditDataListIndex.ADD_USER_INDEX.getIndex() ) {
            moveToUser();
        }
    }

    /**
     * @brief Parse Click Event For CSV Data.
     *
     * @param click_index clciked index.
     */
    private void parseClickEventForCSVData(int click_index) {
        if( click_index == CSVDataListIndex.INPUT_CSV_FILE_DATA.getIndex() ) {
            new ImportDatabaseTable(this.activity).importData(this);
        } else if( click_index == CSVDataListIndex.OUTPUT_CSV_FILE_DATA.getIndex() ) {
            new ExportDatabaseTable(this.activity).exportData(this);
        }
    }

    /**
     * @brief Response Import Data.
     *
     * @param boolean Import Data Resule(true:successed false:failed).
     */
    @Override
    public void OnResponseImportData(boolean is_result) {
        if( false == is_result ) {
            displayToast(this.activity.getText(R.string.import_data_error).toString());
        } else {
            // notify complete import data.
            this.responseAppMenu.OnResponseImportData(is_result);
            displayToast(this.activity.getText(R.string.import_data_success).toString());
        }
    }

    /**
     * @brief Response when TableData is Exported.
     *
     * @param boolean Export Data Resule(true:successed false:failed).
     */
    @Override
    public void OnResponseExportData(boolean is_result) {
        if( false == is_result ) {
            displayToast(this.activity.getText(R.string.export_data_error).toString());
        } else {
            displayToast(this.activity.getText(R.string.export_data_success).toString());
        }
    }

    /**
     * @brief Display Toast Message.
     *
     * @param message displayed message.
     */
    private void displayToast(String message) {
        Toast.makeText(this.activity, message, Toast.LENGTH_LONG).show();
    }

    /**
     * @brief Move To Add Category into Master Activity.
     */
    private void moveToAddCategory() {
        Intent intent = new Intent( this.activity.getApplicationContext(), EditAccountMasterActivity.class);
        this.activity.startActivity(intent);
    }

    /**
     * @brief Move To Add User into User Table.
     */
    private void moveToUser() {
        Intent intent = new Intent( this.activity.getApplicationContext(), EditUserTableActivity.class);
        this.activity.startActivity(intent);
    }
}

