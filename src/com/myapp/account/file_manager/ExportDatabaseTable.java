package com.myapp.account.file_manager;

import java.util.List;
import java.io.IOException;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;

import com.myapp.account.R;
import com.myapp.account.config.AppConfigurationData;
import com.myapp.account.database.DatabaseHelper;
import com.myapp.account.database.AccountMasterTableAccessor;
import com.myapp.account.database.AccountMasterTableRecord;
import com.myapp.account.database.AccountTableAccessor;
import com.myapp.account.database.AccountTableRecord;
import com.myapp.account.database.EstimateTableAccessor;
import com.myapp.account.database.EstimateTableRecord;
import com.myapp.account.database.UserTableAccessor;
import com.myapp.account.database.UserTableRecord;

/**
 * @brief  Export Table Data Class.
 */
@SuppressLint("NewApi")
public class ExportDatabaseTable extends AbstractExportImportData {

    /**
     * @brief Constructor.
     *
     * @param activity Activity Instance.
     */
    public ExportDatabaseTable(Activity activity) {
        super(activity);
        this.accountMasterTable = new ExportAccountMasterTableImpl(activity);
        this.accountDataTable = new ExportAccountDataTableImpl(activity);
        this.estimateTable = new ExportEstimateTableImpl(activity);
        this.userTable = new ExportUserTableImpl(activity);
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
        return Boolean.valueOf(start());
    }

    /**
     * @brief start Export Data.
     */
    @Override
    public boolean start() {
        boolean result = true;
        try {
            // export data.
            this.accountMasterTable.exportData();
            this.accountDataTable.exportData();
            this.estimateTable.exportData();
            this.userTable.exportData();
        } catch(ExportDataException exception) {
            Log.d("ExportDatabaseTable", "ExportData Exception");
            result = false;
        }
        return result;
    }

    /**
     * @brief Complete Disposal.
     *
     * @param complete_result Result Boolean Value.
     */
    @Override
    protected void onComplete(Boolean complete_result) {
        // notify export data complete.
        this.response.OnResponseExportData(complete_result.booleanValue());
    }

    /**
     * @brief Export AccountMaster Table Class.
     */
    static private class ExportAccountMasterTableImpl extends AbstractExportImportDBTable {

        private AccountMasterTableAccessor accountMaster = null;
        private static final String EXPORT_FILE_NAME = "AccountMaster.csv";

        /**
         * @brief Constructor.
         */
        public ExportAccountMasterTableImpl(Activity activity) {
            super(activity);
            this.accountMaster = new AccountMasterTableAccessor(new DatabaseHelper(activity.getApplicationContext()));
        }

        /**
         * @brief Delete File.
         */
        protected void deleteFile() {
            this.sdCardFileManager.deleteFile(EXPORT_FILE_NAME);
        }

        /**
         * @brief Write File.
         */
        protected void writeFile(String write_record) throws IOException {
            this.sdCardFileManager.writeFile(EXPORT_FILE_NAME, write_record);
        }

        /**
         * @brief Get Record Count.
         *
         * @return Record Counts.
         */
        @Override
        protected int getRecordCount() {
            return this.accountMaster.getRecordCount();
        }

        /**
         * @brief Get Record.
         *
         * @param count Get Record Count.
         * @param offset Start Offset.
         *
         * @return  AccountMasterTableRecord String.
         */
        protected String getRecord(int count, int offset) {
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
    static private class ExportAccountDataTableImpl extends AbstractExportImportDBTable {

        private AccountTableAccessor accountTable = null;
        private static final String EXPORT_FILE_NAME = "AccountData.csv";

        /**
         * @brief Constructor.
         */
        public ExportAccountDataTableImpl(Activity activity) {
            super(activity);
            AppConfigurationData app_config = new AppConfigurationData(activity);
            this.accountTable = new AccountTableAccessor(new DatabaseHelper(activity.getApplicationContext()), app_config);
        }

        /**
         * @brief Delete File.
         */
        protected void deleteFile() {
            this.sdCardFileManager.deleteFile(EXPORT_FILE_NAME);
        }

        /**
         * @brief Write File.
         */
        protected void writeFile(String write_record) throws IOException {
            this.sdCardFileManager.writeFile(EXPORT_FILE_NAME, write_record);
        }

        /**
         * @brief Get Record Count.
         *
         * @return Record Counts.
         */
        @Override
        protected int getRecordCount() {
            return this.accountTable.getRecordCount();
        }

        /**
         * @brief Get Record.
         *
         * @param count Get Record Count.
         * @param offset Start Offset.
         *
         * @return  AccountMasterTableRecord String.
         */
        protected String getRecord(int count, int offset) {
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
    static private class ExportEstimateTableImpl extends AbstractExportImportDBTable {

        private EstimateTableAccessor estimateTable = null;
        private static final String EXPORT_FILE_NAME = "Estimate.csv";

        /**
         * @brief Constructor.
         */
        public ExportEstimateTableImpl(Activity activity) {
            super(activity);
            AppConfigurationData app_config = new AppConfigurationData(activity);
            this.estimateTable = new EstimateTableAccessor(new DatabaseHelper(activity.getApplicationContext()), app_config);
        }

        /**
         * @brief Delete File.
         */
        protected void deleteFile() {
            this.sdCardFileManager.deleteFile(EXPORT_FILE_NAME);
        }

        /**
         * @brief Write File.
         */
        protected void writeFile(String write_record) throws IOException {
            this.sdCardFileManager.writeFile(EXPORT_FILE_NAME, write_record);
        }

        /**
         * @brief Get Record Count.
         *
         * @return Record Counts.
         */
        @Override
        protected int getRecordCount() {
            return this.estimateTable.getRecordCount();
        }

        /**
         * @brief Get Record.
         *
         * @param count Get Record Count.
         * @param offset Start Offset.
         *
         * @return  AccountMasterTableRecord String.
         */
        protected String getRecord(int count, int offset) {
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
    static private class ExportUserTableImpl extends AbstractExportImportDBTable {

        private UserTableAccessor userTable = null;
        private static final String EXPORT_FILE_NAME = "UserTable.csv";

        /**
         * @brief Constructor.
         */
        public ExportUserTableImpl(Activity activity) {
            super(activity);
            this.userTable = new UserTableAccessor(new DatabaseHelper(activity.getApplicationContext()));
        }

        /**
         * @brief Delete File.
         */
        protected void deleteFile() {
            this.sdCardFileManager.deleteFile(EXPORT_FILE_NAME);
        }

        /**
         * @brief Write File.
         */
        protected void writeFile(String write_record) throws IOException {
            this.sdCardFileManager.writeFile(EXPORT_FILE_NAME, write_record);
        }

        /**
         * @brief Get Record Count.
         *
         * @return Record Counts.
         */
        @Override
        protected int getRecordCount() {
            return this.userTable.getRecordCount();
        }

        /**
         * @brief Get Record.
         *
         * @param count Get Record Count.
         * @param offset Start Offset.
         *
         * @return  AccountMasterTableRecord String.
         */
        protected String getRecord(int count, int offset) {
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

