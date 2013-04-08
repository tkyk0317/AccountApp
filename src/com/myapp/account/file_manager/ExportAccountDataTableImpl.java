package com.myapp.account.file_manager;

import java.util.List;
import android.app.Activity;

import com.myapp.account.database.DatabaseHelper;
import com.myapp.account.file_manager.ExportImportDBTableInterface;
import com.myapp.account.file_manager.SdCardFileManagerImpl;
import com.myapp.account.database.AccountTableAccessor;
import com.myapp.account.database.AccountTableRecord;
import com.myapp.account.config.AppConfigurationData;

/**
 * @brief Export AccountData Table Class.
 */
public class ExportAccountDataTableImpl implements ExportImportDBTableInterface {

    private SdCardFileManagerImpl sdCardFileManager = null;
    private AccountTableAccessor accountTable = null;
    private static final String EXPORT_FILE_NAME = "AccountData.csv";
    private static final String CSV_DELIMITER = ",";
    private static final String LINE_END = "\n";

    /**
     * @brief Constractor.
     */
    public ExportAccountDataTableImpl(Activity activity) {
        AppConfigurationData app_config = new AppConfigurationData(activity);
        this.sdCardFileManager = new SdCardFileManagerImpl();
        this.accountTable = new AccountTableAccessor(new DatabaseHelper(activity.getApplicationContext()), app_config);
    }

    /**
     * @brief Import Table Data.
     * @note not support.
     */
    @Override
    public boolean importData() {
        return false;
    }

    /**
     * @brief Export AccountTableData to CSV File.
     * @return true:Successed Export false:Failed Export.
     */
    @Override
    public boolean exportData() {
        return this.sdCardFileManager.writeFile(EXPORT_FILE_NAME, getAccountTableData());
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

        for( AccountTableRecord record : record_list) {
            serialize_data += (String.valueOf(record.getId()) + CSV_DELIMITER);
            serialize_data += (String.valueOf(record.getUserId()) + CSV_DELIMITER);
            serialize_data += (String.valueOf(record.getCategoryId()) + CSV_DELIMITER);
            serialize_data += (String.valueOf(record.getMoney()) + CSV_DELIMITER);
            serialize_data += (record.getMemo() + CSV_DELIMITER);
            serialize_data += (record.getUpdateDate() + CSV_DELIMITER);
            serialize_data += (record.getInsertDate() + LINE_END);
        }
        return serialize_data;
    }
}

