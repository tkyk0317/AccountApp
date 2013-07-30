package com.myapp.account.file_manager;

import java.util.List;
import java.util.ArrayList;
import android.app.Activity;
import android.util.Log;

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

/**
 * @brief Import Table Data Class.
 */
public class ImportDatabaseTable {

    private AbstractExportImportDBTable importAccountMasterTable = null;
    private AbstractExportImportDBTable importAccountDataTable = null;
    private AbstractExportImportDBTable importEstimateTable = null;
    private AbstractExportImportDBTable importUserTable = null;

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
    }

    /**
     * @brief Import Table Data.
     */
    public void importData() throws ImportDataException {
        try {
            this.importAccountMasterTable.importData();
            this.importAccountDataTable.importData();
            this.importEstimateTable.importData();
            this.importUserTable.importData();
        } catch(ImportDataException exception) {
            Log.d("ImportDatabaseTable", "ImportData Exception");
            throw exception;
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
            List<AccountTableRecord> account_record = deserialize();

            // insert table data.
            boolean ret = true;
            for( AccountTableRecord record : account_record ) {
                // check insert return value.
                if( -1 == this.accountTable.insert(record) ) {
                    ret = false;
                    break;
                }
            }
            // check insert result.
            if( false == ret ) {
                throw new ImportDataException("ImportData Exception");
            }
        }

        /**
         * @brief Deserialize AccountTable Record.
         * @return AccountTableRecord List.
         */
        private List<AccountTableRecord> deserialize() {
            List<AccountTableRecord> record = new ArrayList<AccountTableRecord>();
            String[] account_data = this.sdCardFileManager.readFile(IMPORT_FILE_NAME).split(LINE_END);

            // check null string.
            if( false == Utility.isStringNULL(account_data[0]) ) {
                for( int i = 0 ; i < account_data.length ; ++i ) {
                    String[] table_data = account_data[i].split(CSV_DELIMITER);
                    AccountTableRecord account_record = new AccountTableRecord();

                    // add table data.
                    addTableDataIntoRecord(table_data, account_record);
                    record.add(account_record);
                }
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
            List<AccountMasterTableRecord> master_records = deserialize();

            // insert table data.
            boolean ret = true;
            for( AccountMasterTableRecord record : master_records ) {
                // check same name record.
                if( true == this.accountMaster.isExsitRecordMatchName(record.getName()) ) continue;

                // check insert return value.
                if( -1 == this.accountMaster.insert(record) ) {
                    ret = false;
                    break;
                }
            }
            // check insert result.
            if( false == ret ) {
                throw new ImportDataException("ImportData Exception");
            }
        }

        /**
         * @brief Deserialize AccountMaster Record.
         * @return AccountMasterTableRecord List.
         */
        private List<AccountMasterTableRecord> deserialize() {
            List<AccountMasterTableRecord> record = new ArrayList<AccountMasterTableRecord>();
            String[] master_data = this.sdCardFileManager.readFile(IMPORT_FILE_NAME).split(LINE_END);

            // check null string.
            if( false == Utility.isStringNULL(master_data[0]) ) {
                for( int i = 0 ; i < master_data.length ; ++i ) {
                    String[] table_data = master_data[i].split(CSV_DELIMITER);
                    AccountMasterTableRecord master_record = new AccountMasterTableRecord();

                    // add table data.
                    addTableDataIntoRecord(table_data, master_record);
                    record.add(master_record);
                }
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
            List<EstimateTableRecord> estimate_records = deserialize();

            // insert table data.
            boolean ret = true;
            for( EstimateTableRecord record : estimate_records ) {
                // check same target date record.
                if( true == this.estimateTable.isEstimateRecord(record.getTargetDate()) ) continue;

                // check insert return value.
                if( -1 == this.estimateTable.insert(record) ) {
                    ret = false;
                    break;
                }
            }
            // check insert result.
            if( false == ret ) {
                throw new ImportDataException("ImportData Exception");
            }
        }

        /**
         * @brief Deserialize EstimateTable Record.
         * @return EstimateTableRecord List.
         */
        private List<EstimateTableRecord> deserialize() {
            List<EstimateTableRecord> record = new ArrayList<EstimateTableRecord>();
            String[] estimate_data = this.sdCardFileManager.readFile(IMPORT_FILE_NAME).split(LINE_END);

            // check null string.
            if( false == Utility.isStringNULL(estimate_data[0]) ) {
                for( int i = 0 ; i < estimate_data.length ; ++i ) {
                    String[] table_data = estimate_data[i].split(CSV_DELIMITER);
                    EstimateTableRecord estimate_record = new EstimateTableRecord();

                    // add table data.
                    addTableDataIntoRecord(table_data, estimate_record);
                    record.add(estimate_record);
                }
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
            List<UserTableRecord> user_records = deserialize();

            // insert table data.
            boolean ret = true;
            for( UserTableRecord record : user_records ) {
                // check same user name record.
                if( true == this.userTable.isExsitRecordMatchName(record.getName()) ) continue;

                // check insert return value.
                if( -1 == this.userTable.insert(record) ) {
                    ret = false;
                    break;
                }
            }
            // check insert result.
            if( false == ret ) {
                throw new ImportDataException("ImportData Exception");
            }
        }

        /**
         * @brief Deserialize UserTable Record.
         * @return UserTableRecord List.
         */
        private List<UserTableRecord> deserialize() {
            List<UserTableRecord> record = new ArrayList<UserTableRecord>();
            String[] user_data = this.sdCardFileManager.readFile(IMPORT_FILE_NAME).split(LINE_END);

            // check null string.
            if( false == Utility.isStringNULL(user_data[0]) ) {
                for( int i = 0 ; i < user_data.length ; ++i ) {
                    String[] table_data = user_data[i].split(CSV_DELIMITER);
                    UserTableRecord user_record = new UserTableRecord();

                    // add table data.
                    addTableDataIntoRecord(table_data, user_record);
                    record.add(user_record);
                }
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

