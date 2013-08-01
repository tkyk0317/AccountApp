package com.myapp.account.file_manager;

import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.os.Handler;

import com.myapp.account.R;
import com.myapp.account.file_manager.ImportDataException;
import com.myapp.account.config.AppConfigurationData;
import com.myapp.account.database.DatabaseHelper;
import com.myapp.account.file_manager.AbstractExportImportDBTable;
import com.myapp.account.file_manager.SdCardFileManagerImpl;
import com.myapp.account.database.AccountTableAccessor;
import com.myapp.account.database.AccountTableRecord;
import com.myapp.account.database.AccountMasterTableRecord;
import com.myapp.account.database.AccountMasterTableAccessor;
import com.myapp.account.database.EstimateTableRecord;
import com.myapp.account.database.EstimateTableAccessor;
import com.myapp.account.database.UserTableRecord;
import com.myapp.account.database.UserTableAccessor;
import com.myapp.account.utility.Utility;
import com.myapp.account.response.ResponseApplicationMenuInterface;

/**
 * @brief Import Table Data Class.
 */
public class ImportDatabaseTable {

    private AbstractExportImportDBTable importAccountMasterTable = null;
    private AbstractExportImportDBTable importAccountDataTable = null;
    private AbstractExportImportDBTable importEstimateTable = null;
    private AbstractExportImportDBTable importUserTable = null;
    private ProgressDialog progressDialog = null;
    private ResponseApplicationMenuInterface responseAppMenu = null;

    /**
     * @brief Constructor.
     *
     * @param activity Activity Instance.
     */
    public ImportDatabaseTable(Activity activity) {
        this.importAccountMasterTable = new ImportAccountMasterTableImpl(activity);
        this.importAccountDataTable = new ImportAccountDataTableImpl(activity);
        this.importEstimateTable = new ImportEstimateTableImpl(activity);
        this.importUserTable = new ImportUserTableImpl(activity);
        this.progressDialog = new ProgressDialog(activity);

        // initialize progress dialog.
        initializeProgressDialog(activity);
    }

    /**
     * @brief Initialize Progress Dialog.
     */
    private void initializeProgressDialog(Activity activity) {
        this.progressDialog.setTitle(activity.getText(R.string.import_progress_dialog_title));
        this.progressDialog.setMessage(activity.getText(R.string.import_progress_dialog_message));
        this.progressDialog.setIndeterminate(false);
        this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        this.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        this.progressDialog.setCancelable(false);    }

    /**
     * @brief Import Table Data.
     */
    public void importData(ResponseApplicationMenuInterface response) {
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
                        startImportData();
                    }
                });
            }
        }).start();
    }

    /**
     * @brief Start Import Data.
     */
    public void startImportData() {
        try {
            // import data.
            this.importAccountMasterTable.importData();
            this.importAccountDataTable.importData();
            this.importEstimateTable.importData();
            this.importUserTable.importData();

            // notify response import data.
            this.responseAppMenu.OnResponseImportData(true);
        } catch(ImportDataException exception) {
            Log.d("ImportDatabaseTable", "ImportData Exception");
            this.responseAppMenu.OnResponseImportData(false);
        } finally {
            this.progressDialog.dismiss();
        }
     }

    /**
     * @brief Export AccountData Table Class.
     */
    private class ImportAccountDataTableImpl extends AbstractExportImportDBTable {

        private AccountTableAccessor accountTable = null;
        private static final String IMPORT_FILE_NAME = "AccountData.csv";

        /**
         * @brief Constructor.
         */
        public ImportAccountDataTableImpl(Activity activity) {
            AppConfigurationData app_config = new AppConfigurationData(activity);
            this.sdCardFileManager = new SdCardFileManagerImpl();
            this.accountTable = new AccountTableAccessor(new DatabaseHelper(activity.getApplicationContext()), app_config);
        }

        /**
         * @brief Import Table Data.
         */
        @Override
        public void importData() throws ImportDataException {
            try {
                this.accountTable.startTransaction();

                // Read File.
                String line_string;
                this.sdCardFileManager.open(IMPORT_FILE_NAME);
                while( null != (line_string = this.sdCardFileManager.readOneline()) ) {
                    AccountTableRecord record = deserialize(line_string.split(LINE_END)[0]);

                    // insert record.
                    if( -1 == this.accountTable.insert(record) ) {
                        this.sdCardFileManager.close();
                        this.accountTable.endTransaction();
                        throw new ImportDataException("ImportData Exception");
                    }
                    // delete instance.
                    line_string = null;
                    record = null;
                }
                this.sdCardFileManager.close();
                this.accountTable.setTransactionSuccessful();
            } catch (FileNotFoundException not_found_exception) {
                Log.d("ImportAccountDataTableImpl", "File Not Found Exception");
            } catch (IOException io_exception) {
                throw new ImportDataException("IOException");
            } finally {
                this.accountTable.endTransaction();
            }
        }

        /**
         * @brief Deserialize AccountTable Record.
         *
         * @param csv_data CSV Data String.
         *
         * @return AccountTableRecord.
         */
        private AccountTableRecord deserialize(String csv_data) {
            AccountTableRecord record = new AccountTableRecord();

            // check null string.
            if( false == Utility.isStringNULL(csv_data) ) {
                String[] table_data = csv_data.split(CSV_DELIMITER);
                addTableDataIntoRecord(table_data, record);
            }
            return record;
        }

        /**
         * @brief Add TableData Into Record Object.
         *
         * @param table_data table data(string array).
         * @param record AccountTableRecord.
         */
        private void addTableDataIntoRecord(String[] table_data, AccountTableRecord record) {
            record.setId(Integer.valueOf(table_data[0]));
            record.setUserId(Integer.valueOf(table_data[1]));
            record.setCategoryId(Integer.valueOf(table_data[2]));
            record.setMoney(Integer.valueOf(table_data[3]));
            record.setMemo(table_data[4]);
            record.setUpdateDate(table_data[5]);
            record.setInsertDate(table_data[6]);
        }
    }

    /**
     * @brief Export AccountMaster Table Class.
     */
    private class ImportAccountMasterTableImpl extends AbstractExportImportDBTable {

        private AccountMasterTableAccessor accountMaster = null;
        private static final String IMPORT_FILE_NAME = "AccountMaster.csv";

        /**
         * @brief Constructor.
         */
        public ImportAccountMasterTableImpl(Activity activity) {
            this.sdCardFileManager = new SdCardFileManagerImpl();
            this.accountMaster = new AccountMasterTableAccessor(new DatabaseHelper(activity.getApplicationContext()));
        }

        /**
         * @brief Import Table Data.
         */
        @Override
        public void importData() throws ImportDataException {
            try {
                this.accountMaster.startTransaction();

                // read file.
                String line_string;
                this.sdCardFileManager.open(IMPORT_FILE_NAME);
                while( null != (line_string = this.sdCardFileManager.readOneline()) ) {
                    AccountMasterTableRecord record = deserialize(line_string.split(LINE_END)[0]);

                    // check same name record.
                    if( true == this.accountMaster.isExsitRecordMatchName(record.getName()) ) continue;

                    // check insert return value.
                    if( -1 == this.accountMaster.insert(record) ) {
                        this.sdCardFileManager.close();
                        this.accountMaster.endTransaction();
                        throw new ImportDataException("ImportData Exception");
                    }
                    // delete instance.
                    line_string = null;
                    record = null;
                }
                this.sdCardFileManager.close();
                this.accountMaster.setTransactionSuccessful();
            } catch (FileNotFoundException not_found_exception) {
                Log.d("ImportAccountMasterTableImpl", "File Not Found Exception");
            } catch (IOException io_exception) {
                throw new ImportDataException("IOException");
            } finally {
                this.accountMaster.endTransaction();
            }
        }

        /**
         * @brief Deserialize AccountMaster Record.
         *
         * @param csv_data CSV Data String.
         *
         * @return AccountMasterTableRecord.
         */
        private AccountMasterTableRecord deserialize(String csv_data) {
            AccountMasterTableRecord record = new AccountMasterTableRecord();

            // check null string.
            if( false == Utility.isStringNULL(csv_data) ) {
                String[] table_data = csv_data.split(CSV_DELIMITER);
                addTableDataIntoRecord(table_data, record);
            }
            return record;
        }

        /**
         * @brief Add TableData Into Record Object.
         *
         * @param table_data table data(string array).
         * @param record AccountMasterTableRecord.
         */
        private void addTableDataIntoRecord(String[] table_data, AccountMasterTableRecord record) {
            record.setId(Integer.valueOf(table_data[0]));
            record.setKindId(Integer.valueOf(table_data[1]));
            record.setName(table_data[2]);
            record.setUseDate(table_data[3]);
            record.setUpdateDate(table_data[4]);
            record.setInsertDate(table_data[5]);
        }
    }

    /**
     * @brief Export Estimate Table Class.
     */
    private class ImportEstimateTableImpl extends AbstractExportImportDBTable {

        private EstimateTableAccessor estimateTable = null;
        private static final String IMPORT_FILE_NAME = "Estimate.csv";

        /**
         * @brief Constructor.
         */
        public ImportEstimateTableImpl(Activity activity) {
            AppConfigurationData app_config = new AppConfigurationData(activity);
            this.sdCardFileManager = new SdCardFileManagerImpl();
            this.estimateTable = new EstimateTableAccessor(new DatabaseHelper(activity.getApplicationContext()), app_config);
        }

        /**
         * @brief Import Table Data.
         * @return true:Successed Export false:Failed Export.
         */
        @Override
        public void importData() throws ImportDataException {
            try {
                this.estimateTable.startTransaction();

                // read file.
                String line_string;
                this.sdCardFileManager.open(IMPORT_FILE_NAME);
                while( null != (line_string = this.sdCardFileManager.readOneline()) ) {
                    EstimateTableRecord record = deserialize(line_string.split(LINE_END)[0]);

                    // check same target date record.
                    if( true == this.estimateTable.isEstimateRecord(record.getTargetDate()) ) continue;

                    // check insert return value.
                    if( -1 == this.estimateTable.insert(record) ) {
                        this.sdCardFileManager.close();
                        this.estimateTable.endTransaction();
                        throw new ImportDataException("ImportData Exception");
                    }
                    // delete instance.
                    line_string = null;
                    record = null;
                }
                this.sdCardFileManager.close();
                this.estimateTable.setTransactionSuccessful();
            } catch (FileNotFoundException not_found_exception) {
                Log.d("ImportEstimateTableImpl", "File Not Found Exception");
            } catch (IOException io_exception) {
                throw new ImportDataException("IOException");
            } finally {
                this.estimateTable.endTransaction();
            }
        }

        /**
         * @brief Deserialize EstimateTable Record.
         *
         * @param csv_data CSV Data String.
         *
         * @return EstimateTableRecord.
         */
        private EstimateTableRecord deserialize(String csv_data) {
            EstimateTableRecord record = new EstimateTableRecord();

            // check null string.
            if( false == Utility.isStringNULL(csv_data) ) {
                String[] table_data = csv_data.split(CSV_DELIMITER);
                addTableDataIntoRecord(table_data, record);
            }
            return record;
        }

        /**
         * @brief Add TableData Into Record Object.
         *
         * @param table_data table data(string array).
         * @param record EstimateTableRecord.
         */
        private void addTableDataIntoRecord(String[] table_data, EstimateTableRecord record) {
            record.setId(Integer.valueOf(table_data[0]));
            record.setEstimateMoney(Integer.valueOf(table_data[1]));
            record.setEstimateTargetDate(table_data[2]);
            record.setUpdateDate(table_data[3]);
            record.setInsertDate(table_data[4]);
            record.setUserId(Integer.valueOf(table_data[5]));
       }
    }

    /**
     * @brief Export User Table Class.
     */
    private class ImportUserTableImpl extends AbstractExportImportDBTable {

        private UserTableAccessor userTable = null;
        private static final String IMPORT_FILE_NAME = "UserTable.csv";

        /**
         * @brief Constructor.
         */
        public ImportUserTableImpl(Activity activity) {
            this.sdCardFileManager = new SdCardFileManagerImpl();
            this.userTable = new UserTableAccessor(new DatabaseHelper(activity.getApplicationContext()));
        }

        /**
         * @brief Import Table Data.
         */
        @Override
        public void importData() throws ImportDataException {
            try {
                this.userTable.startTransaction();

                // read file.
                String line_string;
                this.sdCardFileManager.open(IMPORT_FILE_NAME);
                while( null != (line_string = this.sdCardFileManager.readOneline()) ) {
                    UserTableRecord record = deserialize(line_string.split(LINE_END)[0]);

                    // check same user name record.
                    if( true == this.userTable.isExsitRecordMatchName(record.getName()) ) continue;

                    // check insert return value.
                    if( -1 == this.userTable.insert(record) ) {
                        this.sdCardFileManager.close();
                        this.userTable.endTransaction();
                        throw new ImportDataException("ImportData Exception");
                    }
                    // delete instance.
                    line_string = null;
                    record = null;
                }
                this.sdCardFileManager.close();
                this.userTable.setTransactionSuccessful();
            } catch (FileNotFoundException not_found_exception) {
                Log.d("ImportUserTableImpl", "File Not Found Exception");
            } catch (IOException io_exception) {
                throw new ImportDataException("IOException");
            } finally {
                this.userTable.endTransaction();
            }
        }

        /**
         * @brief Deserialize UserTable Record.
         *
         * @param csv_data CSV Data String.
         *
         * @return UserTableRecord.
         */
        private UserTableRecord deserialize(String csv_data) {
            UserTableRecord record = new UserTableRecord();

            // check null string.
            if( false == Utility.isStringNULL(csv_data) ) {
                String[] table_data = csv_data.split(CSV_DELIMITER);
                addTableDataIntoRecord(table_data, record);
            }
            return record;
        }

        /**
         * @brief Add TableData Into Record Object.
         *
         * @param table_data table data(string array).
         * @param record UserTableRecord.
         */
        private void addTableDataIntoRecord(String[] table_data, UserTableRecord record) {
            record.setId(Integer.valueOf(table_data[0]));
            record.setName(table_data[1]);
            record.setUpdateDate(table_data[2]);
            record.setInsertDate(table_data[3]);
            record.setMemo(table_data[4]);
      }
    }
}
