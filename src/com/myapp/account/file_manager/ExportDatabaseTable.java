package com.myapp.account.file_manager;

import java.util.List;
import java.io.IOException;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.os.AsyncTask;
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
@SuppressLint("NewApi")
public class ExportDatabaseTable extends AsyncTask<String, Integer, Boolean> {

    private Activity activity = null;
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
        this.activity = activity;
        this.exportAccountMasterTable = new ExportAccountMasterTableImpl(activity);
        this.exportAccountDataTable = new ExportAccountDataTableImpl(activity);
        this.exportEstimateTable = new ExportEstimateTableImpl(activity);
        this.exportUserTable = new ExportUserTableImpl(activity);
    }

    /**
     * @brief Export Database Table Data.
     */
    @SuppressLint("NewApi")
	public void exportData(ResponseApplicationMenuInterface response) {
        // if UI operation, must use Handler Thread.
        this.responseAppMenu = response;
        execute("");
    }

    /**
     * @brief First Called from UI Thread.
     */
    @Override
    protected void onPreExecute() {
        this.progressDialog = new ProgressDialog(this.activity);
        this.progressDialog.setTitle(activity.getText(R.string.export_progress_dialog_title));
        this.progressDialog.setMessage(activity.getText(R.string.export_progress_dialog_message));
        this.progressDialog.setIndeterminate(false);
        this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        this.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        this.progressDialog.setCancelable(false);
        this.progressDialog.show();
    }

    /**
     * @brief BackGround Work.
     *
     * @param params String Parameters.
     */
    @Override
    protected Boolean doInBackground(String... params) {
        return Boolean.valueOf(startExportData());
    }

    /**
     * @brief Called from Worker Thread when publishProgress called.
     *
     * @param values progress value.
     */
    @Override
    protected void onProgressUpdate(Integer... values) {
    }

    /**
     * @brief Called when Cancel.
     */
    @Override
    protected void onCancelled() {
    }

    /**
     * @brief Called when Worker Thread is Complete.
     *
     * @param result Result Parameter.
     */
    @Override
    protected void onPostExecute(Boolean result) {
        this.progressDialog.dismiss();
        this.progressDialog = null;

        // notify export data complete.
        this.responseAppMenu.OnResponseExportData(result.booleanValue());
    }

    /**
     * @brief start Export Data.
     */
    public boolean startExportData() {
        boolean result = true;
        try {
            // export data.
            this.exportAccountMasterTable.exportData();
            this.exportAccountDataTable.exportData();
            this.exportEstimateTable.exportData();
            this.exportUserTable.exportData();
        } catch(ExportDataException exception) {
            Log.d("ExportDatabaseTable", "ExportData Exception");
            result = false;
        }
        return result;
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
            // delete file.
            this.sdCardFileManager.deleteFile(EXPORT_FILE_NAME);

            // write record.
            int record_count = this.accountMaster.getRecordCount();
            for( int write_count = 0 ; write_count < record_count ; write_count += WRITE_RECORD_COUNT ) {
                try {
                    String write_record = getRecord(WRITE_RECORD_COUNT, write_count);
                    this.sdCardFileManager.writeFile(EXPORT_FILE_NAME, write_record);
                    write_record = null;
                } catch(IOException exception) {
                    throw new ExportDataException("ExportData Error");
                }
            }
        }

        /**
         * @brief Get Record.
         *
         * @param count Get Record Count.
         * @param offset Start Offset.
         *
         * @return  AccountMasterTableRecord String.
         */
        private String getRecord(int count, int offset) {
            List<AccountMasterTableRecord> record = this.accountMaster.getRecord(count, offset);
            String write_record = serialize(record);
            record.clear();
            record = null;
            return write_record;
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
            // delete file.
            this.sdCardFileManager.deleteFile(EXPORT_FILE_NAME);

            // write record.
            int record_count = this.accountTable.getRecordCount();
            for( int write_count = 0 ; write_count < record_count ; write_count += WRITE_RECORD_COUNT ) {
                try {
                    String write_record = getRecord(WRITE_RECORD_COUNT, write_count);
                    this.sdCardFileManager.writeFile(EXPORT_FILE_NAME, write_record);
                    write_record = null;
                } catch (IOException exception) {
                    throw new ExportDataException("ExportData Error");
                }
            }
        }

        /**
         * @brief Get Record.
         *
         * @param count Get Record Count.
         * @param offset Start Offset.
         *
         * @return  AccountMasterTableRecord String.
         */
        private String getRecord(int count, int offset) {
            List<AccountTableRecord> record = this.accountTable.getRecord(count, offset);
            String write_record = serialize(record);
            record.clear();
            record = null;
            return write_record;
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
            // delete file.
            this.sdCardFileManager.deleteFile(EXPORT_FILE_NAME);

            // write record.
            int record_count = this.estimateTable.getRecordCount();
            for( int write_count = 0 ; write_count < record_count ; write_count += WRITE_RECORD_COUNT ) {
                try {
                    String write_record = getRecord(WRITE_RECORD_COUNT, write_count);
                    this.sdCardFileManager.writeFile(EXPORT_FILE_NAME, write_record);
                    write_record = null;
                } catch (IOException exception) {
                    throw new ExportDataException("ExportData Error");
                }
            }
        }

        /**
         * @brief Get Record.
         *
         * @param count Get Record Count.
         * @param offset Start Offset.
         *
         * @return  AccountMasterTableRecord String.
         */
        private String getRecord(int count, int offset) {
            List<EstimateTableRecord> record = this.estimateTable.getRecord(count, offset);
            String write_record = serialize(record);
            record.clear();
            record = null;
            return write_record;
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
            // delete file.
            this.sdCardFileManager.deleteFile(EXPORT_FILE_NAME);

            // write record.
            int record_count = this.userTable.getRecordCount();
            for( int write_count = 0 ; write_count < record_count ; write_count += WRITE_RECORD_COUNT ) {
                try {
                    String write_record = getRecord(WRITE_RECORD_COUNT, write_count);
                    this.sdCardFileManager.writeFile(EXPORT_FILE_NAME, write_record);
                    write_record = null;
                } catch (IOException exception) {
                    throw new ExportDataException("ExportData Error");
                }
            }
        }

        /**
         * @brief Get Record.
         *
         * @param count Get Record Count.
         * @param offset Start Offset.
         *
         * @return  AccountMasterTableRecord String.
         */
        private String getRecord(int count, int offset) {
            List<UserTableRecord> record = this.userTable.getRecord(count, offset);
            String write_record = serialize(record);
            record.clear();
            record = null;
            return write_record;
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

