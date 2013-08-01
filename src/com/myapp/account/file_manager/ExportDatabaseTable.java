package com.myapp.account.file_manager;

import java.util.List;
import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.os.Handler;

import com.myapp.account.R;
import com.myapp.account.file_manager.ExportDataException;
import com.myapp.account.config.AppConfigurationData;
import com.myapp.account.database.DatabaseHelper;
import com.myapp.account.file_manager.AbstractExportImportDBTable;
import com.myapp.account.file_manager.SdCardFileManagerImpl;
import com.myapp.account.database.AccountMasterTableAccessor;
import com.myapp.account.database.AccountMasterTableRecord;
import com.myapp.account.database.AccountTableAccessor;
import com.myapp.account.database.AccountTableRecord;
import com.myapp.account.database.EstimateTableAccessor;
import com.myapp.account.database.EstimateTableRecord;
import com.myapp.account.database.UserTableAccessor;
import com.myapp.account.database.UserTableRecord;
import com.myapp.account.response.ResponseApplicationMenuInterface;

/**
 * @brief  Export Table Data Class.
 */
public class ExportDatabaseTable {

    private AbstractExportImportDBTable exportAccountMasterTable = null;
    private AbstractExportImportDBTable exportAccountDataTable = null;
    private AbstractExportImportDBTable exportEstimateTable = null;
    private AbstractExportImportDBTable exportUserTable = null;
    private ProgressDialog progressDialog = null;
    private ResponseApplicationMenuInterface responseAppMenu = null;

    /**
     * @brief Constructor.
     *
     * @param activity Activity Instance.
     */
    public ExportDatabaseTable(Activity activity) {
        this.exportAccountMasterTable = new ExportAccountMasterTableImpl(activity);
        this.exportAccountDataTable = new ExportAccountDataTableImpl(activity);
        this.exportEstimateTable = new ExportEstimateTableImpl(activity);
        this.exportUserTable = new ExportUserTableImpl(activity);
        this.progressDialog = new ProgressDialog(activity);

        // initialize progress dialog.
        initializeProgressDialog(activity);
    }

    /**
     * @brief Initialize Progress Dialog.
     */
    private void initializeProgressDialog(Activity activity) {
        this.progressDialog.setTitle(activity.getText(R.string.export_progress_dialog_title));
        this.progressDialog.setMessage(activity.getText(R.string.export_progress_dialog_message));
        this.progressDialog.setIndeterminate(false);
        this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        this.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        this.progressDialog.setCancelable(false);
    }

    /**
     * @brief Export Database Table Data.
     */
    public void exportData(ResponseApplicationMenuInterface response) {
        // if UI operation, must use Handler Thread.
        this.responseAppMenu = response;
        final Handler handler = new Handler();

        // display progress dialog.
        this.progressDialog.show();

        // start thread.
        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        startExportData();
                    }
                });
            }
        }).start();
    }

    /**
     * @brief start Export Data.
     */
    public void startExportData() {
        try {
            // export data.
            this.exportAccountMasterTable.exportData();
            this.exportAccountDataTable.exportData();
            this.exportEstimateTable.exportData();
            this.exportUserTable.exportData();

            // notify export data complete.
            this.responseAppMenu.OnResponseExportData(true);
        } catch(ExportDataException exception) {
            Log.d("ExportDatabaseTable", "ExportData Exception");
            this.responseAppMenu.OnResponseExportData(false);
        } finally {
            this.progressDialog.dismiss();
        }
    }

    /**
     * @brief Export AccountMaster Table Class.
     */
    private class ExportAccountMasterTableImpl extends AbstractExportImportDBTable {

        private AccountMasterTableAccessor accountMaster = null;
        private static final String EXPORT_FILE_NAME = "AccountMaster.csv";

        /**
         * @brief Constructor.
         */
        public ExportAccountMasterTableImpl(Activity activity) {
            this.sdCardFileManager = new SdCardFileManagerImpl();
            this.accountMaster = new AccountMasterTableAccessor(new DatabaseHelper(activity.getApplicationContext()));
        }

        /**
         * @brief Export AccountTableData to CSV File.
         */
        @Override
        public void exportData() throws ExportDataException {
            if( false == this.sdCardFileManager.writeFile(EXPORT_FILE_NAME, getAccountMasterData()) ) {
                throw new ExportDataException("ExportData Error");
            }
        }

        /**
         * @brief Get AccountMaster All Record.
         */
        private String getAccountMasterData() {
            List<AccountMasterTableRecord> record = this.accountMaster.getAllRecord();
            return serialize(record);
        }

        /**
         * @brief Serialize AccountMaster Record Format.
         * @param record AccountMasterTable Record List.
         * @return Serialized AccountTable Record Data.
         */
        private String serialize(List<AccountMasterTableRecord> record_list) {
            String serialize_data= new String();

            if( false == record_list.isEmpty() ) {
                for( AccountMasterTableRecord record : record_list) {
                    serialize_data += (String.valueOf(record.getId()) + CSV_DELIMITER);
                    serialize_data += (String.valueOf(record.getKindId()) + CSV_DELIMITER);
                    serialize_data += (record.getName() + CSV_DELIMITER);
                    serialize_data += (record.getUseDate() + CSV_DELIMITER);
                    serialize_data += (record.getUpdateDate() + CSV_DELIMITER);
                    serialize_data += (record.getInsertDate() + LINE_END);
                }
            }
            return serialize_data;
        }
    }

    /**
     * @brief Export AccountData Table Class.
     */
    private class ExportAccountDataTableImpl extends AbstractExportImportDBTable {

        private AccountTableAccessor accountTable = null;
        private static final String EXPORT_FILE_NAME = "AccountData.csv";

        /**
         * @brief Constructor.
         */
        public ExportAccountDataTableImpl(Activity activity) {
            AppConfigurationData app_config = new AppConfigurationData(activity);
            this.sdCardFileManager = new SdCardFileManagerImpl();
            this.accountTable = new AccountTableAccessor(new DatabaseHelper(activity.getApplicationContext()), app_config);
        }

        /**
         * @brief Export AccountTableData to CSV File.
         */
        @Override
        public void exportData() throws ExportDataException {
            if( false == this.sdCardFileManager.writeFile(EXPORT_FILE_NAME, getAccountTableData()) ) {
                throw new ExportDataException("ExportData Error");
            }
        }

        /**
         * @brief Get AccountTable All Record.
         */
        private String getAccountTableData() {
            List<AccountTableRecord> record = this.accountTable.getAllRecordNotSpecifiedUserId();
            return serialize(record);
        }

        /**
         * @brief Serialize AccountTable Record Format.
         * @param record AccountTable Record List.
         * @return Serialized AccountTable Record Data.
         */
        private String serialize(List<AccountTableRecord> record_list) {
            String serialize_data= new String();

            if( false == record_list.isEmpty() ) {
                for( AccountTableRecord record : record_list) {
                    serialize_data += (String.valueOf(record.getId()) + CSV_DELIMITER);
                    serialize_data += (String.valueOf(record.getUserId()) + CSV_DELIMITER);
                    serialize_data += (String.valueOf(record.getCategoryId()) + CSV_DELIMITER);
                    serialize_data += (String.valueOf(record.getMoney()) + CSV_DELIMITER);
                    serialize_data += (record.getMemo() + CSV_DELIMITER);
                    serialize_data += (record.getUpdateDate() + CSV_DELIMITER);
                    serialize_data += (record.getInsertDate() + LINE_END);
                }
            }
            return serialize_data;
        }
    }

    /**
     * @brief Export Estimate Table Class.
     */
    private class ExportEstimateTableImpl extends AbstractExportImportDBTable {

        private EstimateTableAccessor estimateTable = null;
        private static final String EXPORT_FILE_NAME = "Estimate.csv";

        /**
         * @brief Constructor.
         */
        public ExportEstimateTableImpl(Activity activity) {
            AppConfigurationData app_config = new AppConfigurationData(activity);
            this.sdCardFileManager = new SdCardFileManagerImpl();
            this.estimateTable = new EstimateTableAccessor(new DatabaseHelper(activity.getApplicationContext()), app_config);
        }

        /**
         * @brief Export EstimateTable to CSV File.
         */
        @Override
        public void exportData() throws ExportDataException {
            if( false == this.sdCardFileManager.writeFile(EXPORT_FILE_NAME, getEstimateTableData()) ) {
                throw new ExportDataException("ExportData Error");
            }
        }

        /**
         * @brief Get EstimateTable All Record.
         */
        private String getEstimateTableData() {
            List<EstimateTableRecord> record = this.estimateTable.getAllRecordNotSpecifiedUserId();
            return serialize(record);
        }

        /**
         * @brief Serialize EstimateTable Record Format.
         * @param record EstimateTable Record List.
         * @return Serialized EstimateTable Record Data.
         */
        private String serialize(List<EstimateTableRecord> record_list) {
            String serialize_data= new String();

            if( false == record_list.isEmpty() ) {
                for( EstimateTableRecord record : record_list) {
                    serialize_data += (String.valueOf(record.getId()) + CSV_DELIMITER);
                    serialize_data += (String.valueOf(record.getEstimateMoney()) + CSV_DELIMITER);
                    serialize_data += (record.getTargetDate() + CSV_DELIMITER);
                    serialize_data += (record.getUpdateDate() + CSV_DELIMITER);
                    serialize_data += (record.getInsertDate() + CSV_DELIMITER);
                    serialize_data += (String.valueOf(record.getUserId()) + LINE_END);
                }
            }
            return serialize_data;
        }
    }

    /**
     * @brief Export USerTable Class.
     */
    private class ExportUserTableImpl extends AbstractExportImportDBTable {

        private UserTableAccessor userTable = null;
        private static final String EXPORT_FILE_NAME = "UserTable.csv";

        /**
         * @brief Constructor.
         */
        public ExportUserTableImpl(Activity activity) {
            this.sdCardFileManager = new SdCardFileManagerImpl();
            this.userTable = new UserTableAccessor(new DatabaseHelper(activity.getApplicationContext()));
        }

        /**
         * @brief Export AccountTableData to CSV File.
         */
        @Override
        public void exportData() throws ExportDataException {
            if( false == this.sdCardFileManager.writeFile(EXPORT_FILE_NAME, getUserTableData()) ) {
                throw new ExportDataException("ExportData Error");
            }
        }

        /**
         * @brief Get USerTableData All Record.
         */
        private String getUserTableData() {
            List<UserTableRecord> record = this.userTable.getAllRecord();
            return serialize(record);
        }

        /**
         * @brief Serialize UserTable Record Format.
         * @param record USerTable Record List.
         * @return Serialized UserTable Record Data.
         */
        private String serialize(List<UserTableRecord> record_list) {
            String serialize_data= new String();

            if( false == record_list.isEmpty() ) {
                for( UserTableRecord record : record_list) {
                    serialize_data += (String.valueOf(record.getId()) + CSV_DELIMITER);
                    serialize_data += (record.getName() + CSV_DELIMITER);
                    serialize_data += (record.getUpdateDate() + CSV_DELIMITER);
                    serialize_data += (record.getInsertDate() + CSV_DELIMITER);
                    serialize_data += (record.getMemo() + LINE_END);
                }
            }
            return serialize_data;
        }
    }
}

